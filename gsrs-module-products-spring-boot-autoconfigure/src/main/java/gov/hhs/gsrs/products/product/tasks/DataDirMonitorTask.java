package gov.hhs.gsrs.products.product.tasks;

import gov.hhs.gsrs.products.product.utils.ShellCommandRunner;
import gsrs.scheduledTasks.ScheduledTaskInitializer;
import gsrs.scheduledTasks.SchedulerPlugin;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class DataDirMonitorTask extends ScheduledTaskInitializer {

    private LinkedHashMap<Integer, String> dataFileDirectories = new LinkedHashMap<>();

    private String PROCESSED_FILES_DIR = "processed";

    private String pythonExecutablePath;

    private String dictionaryCsvSourceFilePath;

    private String substanceApiBaseUrl;

    private String firstScriptPath;

    @Override
    public void run(SchedulerPlugin.JobStats stats, SchedulerPlugin.TaskListener l) {
      log.info("Starting in DataDirMonitorTask. dirs: {}", dataFileDirectories);
      listDataFiles();
    }

    @Override
    public String getDescription() {
        return "Monitor data directories for new files to process";
    }

    private void listDataFiles() {
        if( dataFileDirectories == null || dataFileDirectories.isEmpty()) {
            log.error("No data file directories found");
            return;
        }
        List<String> dataDirs = new ArrayList<>(dataFileDirectories.values());
        dataDirs.forEach(dir -> {
            log.info("files in dir {}", dir);
            File directory = new File(dir);
            //make sure there's a place to put processed files
            String processedFilePath = directory.getAbsolutePath() + File.separator + PROCESSED_FILES_DIR;
            log.trace("looking for {}", processedFilePath);
            File processedFile = new File(processedFilePath);
            if(!processedFile.exists()) {
                processedFile.mkdirs();
                log.trace("created {}", processedFilePath);
            }
            StringBuilder results = new StringBuilder();
            try {
                processOneDirectory(dir, results::append);
            } catch (IOException | InterruptedException e) {
                log.error("Error during file processing: {}", e.getMessage(), e);
            }
            log.info("results: {}", results);

            for( String fileName : directory.list()) {
                String fullFilePath = directory.getAbsolutePath() + File.separator + fileName;
                log.info("full file path: {}", fullFilePath);
                File fullFile = new File(fullFilePath);
                if( fullFile.isDirectory() ) {
                    log.info("omitting dir {}", fullFilePath);
                    continue;
                }
                String currentDirectoryPath = System.getProperty("user.dir");
                String destinationPath = processedFilePath + File.separator + fileName;
                //Files.move(fullFile.toPath(), new File(destinationPath).toPath());
                log.info("skipped move of file to {}", destinationPath);
            }
        });
    }

    private void processOneDirectory(String fileName, Consumer<String> consumer) throws IOException, InterruptedException {
        log.info("processOneFile fileName: {}", fileName);
        String logFilePath = File.createTempFile("project_data_processing", ".log").getAbsolutePath();
        StringBuilder commandBuilder = new StringBuilder();
        File scriptFile = new File(firstScriptPath);
        String scriptFileName = getFileName(firstScriptPath);
        String activatorScriptName = "central_script.py";

        String temporaryDirectory = Files.createTempDirectory("python_processing").toAbsolutePath().toString();
        log.info("temporaryDirectory: {}", temporaryDirectory);
        File temporaryScriptFile =new File(temporaryDirectory + File.separator + scriptFileName + ".py");
        Files.copy(scriptFile.toPath(), temporaryScriptFile.toPath());
        File activatorScriptFile = new File(temporaryDirectory + File.separator + activatorScriptName);
        FileWriter writer = new FileWriter(activatorScriptFile);
        writer.write("from " + scriptFileName + " import *\n");

        File jsonFile = File.createTempFile(fileName,".json");
        String jsonFilePath = jsonFile.getAbsolutePath();

        commandBuilder.append("start_processing(\"");
        commandBuilder.append(fileName.replace("\\", "\\\\"));
        commandBuilder.append("\", \"");
        commandBuilder.append(logFilePath.replace("\\", "\\\\"));
        //commandBuilder.append("\" \"");
        //commandBuilder.append(dictionaryCsvSourceFilePath);
        commandBuilder.append("\", \"");
        commandBuilder.append(jsonFilePath.replace("\\", "\\\\"));
        commandBuilder.append("\", \"");
        commandBuilder.append(substanceApiBaseUrl);
        commandBuilder.append("\")");

        log.info("write processing script to {}", activatorScriptFile.getAbsolutePath());

        String commandInPython = commandBuilder.toString();
        log.info("commandInPython: {}", commandInPython);
        writer.write(commandInPython);
        writer.close();

        (new ShellCommandRunner.Builder())
                .activeDir(temporaryDirectory)
                .command(pythonExecutablePath, activatorScriptName)
                .build()
                .run()
                .onInput(consumer::accept);
        log.info("file written? {}", jsonFile.exists());
    }

    public static String getFileName(String path) {
        if(path == null || path.isEmpty()) {
            return null;
        }
        int pos = path.lastIndexOf(File.separator);
        if( pos > 0 ){
            String fullFileName= path.substring(pos+1);
            return fullFileName.replaceAll(".py", "");
        }
        return path;
    }
}
