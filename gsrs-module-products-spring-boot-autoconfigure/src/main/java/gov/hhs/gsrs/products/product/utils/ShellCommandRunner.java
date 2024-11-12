package gov.hhs.gsrs.products.product.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/*
Copied from Resolver
 */
@Slf4j
public class ShellCommandRunner {
    List<String> commandParams;
    File startDir = new File("./");

    public static boolean isWindows(){
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        return isWindows;

    }
    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    public Monitor run() throws IOException, InterruptedException{
        log.trace("startDir: " + this.startDir.getAbsolutePath());
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commandParams.toArray(new String[0]));
        builder.directory(startDir);
        Process process = builder.start();

        return new Monitor(process);
    }


    public static class Builder{
        List<String> commandParams = new ArrayList<String>();
        File startDir=null;


        public Builder command(String... cmd){
            commandParams= Arrays.stream(cmd)
                    .collect(Collectors.toList());
            return this;
        }

        public Builder activeDir(File f){
            this.startDir=f;
            return this;
        }

        public Builder activeDir(String s){
            this.startDir=new File(s);
            return this;
        }


        public ShellCommandRunner build(){
            ShellCommandRunner scr = new ShellCommandRunner();
            scr.commandParams=this.commandParams;
            scr.startDir=startDir;
            return scr;
        }
    }

    public static class Monitor{
        Process p;

        PrintWriter pw =null;

        Consumer<String> onIn=null;
        Consumer<String> onErr=(l)-> log.error(l);

        private Monitor(Process process){
            this.p=process;
            StreamGobbler streamGobblerIn =
                    new StreamGobbler(process.getInputStream(), l->_onInput(l));
            Executors.newSingleThreadExecutor().submit(streamGobblerIn);
            StreamGobbler streamGobblerErr =
                    new StreamGobbler(process.getErrorStream(), l->_onErr(l));
            Executors.newSingleThreadExecutor().submit(streamGobblerErr);
            pw = new PrintWriter(this.p.getOutputStream());
        }

        private void _onInput(String line){
            if(onIn!=null){
                onIn.accept(line);
            }
        }
        private void _onErr(String line){
            if(onErr!=null){
                onErr.accept(line);
            }
        }

        public OutputStream getOut(){
            return p.getOutputStream();
        }
        public InputStream getIn(){
            return p.getInputStream();
        }
        public InputStream getErr(){
            return p.getErrorStream();
        }
        public Process getProcess(){
            return p;
        }

        public Monitor onInput(Consumer<String> oni){
            log.trace("oninput");
            this.onIn=oni;
            return this;
        }

        public Monitor onError(Consumer<String> one){
            this.onErr=one;
            return this;
        }

        public synchronized Monitor writeLine(String line){
            pw.println(line);
            pw.flush();
            return this;
        }

    }
}
