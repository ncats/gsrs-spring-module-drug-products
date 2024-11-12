package gov.hhs.gsrs.products.product.tasks;

import gov.hhs.gsrs.products.product.utils.ShellCommandRunner;
import gov.hhs.gsrs.products.product.utils.ShellCommandRunner.Monitor;
import gsrs.scheduledTasks.ScheduledTaskInitializer;
import gsrs.scheduledTasks.SchedulerPlugin;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Data
public class DataDirMonitorTask extends ScheduledTaskInitializer {

    private LinkedHashMap<Integer, String> dataFileDirectories = new LinkedHashMap<>();

    private String PROCESSED_FILES_DIR = "processed";

    private String BASE_COMMAND = "D:\\app\\python\\python.exe";

    private String SCRIPT_NAME = "D:\\temp\\script1.py";

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
            for( String fileName : directory.list()) {
                String fullFilePath = directory.getAbsolutePath() + File.separator + fileName;
                log.info("full file path: {}", fullFilePath);
                File fullFile = new File(fullFilePath);
                if( fullFile.isDirectory() ) {
                    log.info("omitting dir {}", fullFilePath);
                    continue;
                }
                StringBuilder results = new StringBuilder();
                try {
                    processOneFile("d:\\temp", fullFilePath, results::append);
                    log.info("results: {}", results);
                    String destinationPath = processedFilePath + File.separator + fileName;
                    Files.move(fullFile.toPath(), new File(destinationPath).toPath());
                    log.info("moved file to {}", destinationPath);
                } catch (IOException | InterruptedException e) {
                    log.error("Error during file processing: {}", e.getMessage(), e);
                }
            }
        });
    }

    private void processOneFile(String activeDir, String fileName, Consumer<String> consumer) throws IOException, InterruptedException {
        log.info("processOneFile");
        Monitor monitor=(new ShellCommandRunner.Builder())
                .activeDir(activeDir)
                .command(BASE_COMMAND, SCRIPT_NAME, fileName)
                .build()
                .run()
                .onInput(l->consumer.accept(l));
    }
}
