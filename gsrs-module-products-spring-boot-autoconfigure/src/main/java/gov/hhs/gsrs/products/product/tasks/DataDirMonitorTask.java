package gov.hhs.gsrs.products.product.tasks;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.gsrs.products.processor.DailyMedXmlDataHolderProductToGsrsProductEntityConverter;
import gov.hhs.gsrs.products.processor.DailyMedXmlFileProcessor;
import gov.hhs.gsrs.products.processor.model.DailyMedXmlFileDataHolder;
import gov.hhs.gsrs.products.product.models.Product;
import gov.hhs.gsrs.products.product.services.ProductEntityService;
import gov.nih.ncats.common.util.CachedSupplier;
import gsrs.scheduledTasks.ScheduledTaskInitializer;
import gsrs.scheduledTasks.SchedulerPlugin;
import gsrs.springUtils.AutowireHelper;
import ix.ginas.utils.validation.ValidatorFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class DataDirMonitorTask extends ScheduledTaskInitializer {

    private LinkedHashMap<Integer, String> dataFileDirectories = new LinkedHashMap<>();

    private String PROCESSED_FILES_DIR = "processed";

    private String dictionaryCsvSourceFilePath;

    private String substanceApiBaseUrl;

    private String firstScriptPath;

    private String processedFilePath;

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
        AtomicInteger totalFilesProcessed = new AtomicInteger(0);
        FilenameFilter filter = (f, name) -> name.endsWith(".xml");
        dataDirs.forEach(dir -> {
            log.info("files in dir {}", dir);
            File directory = new File(dir);
            //make sure there's a place to put processed files
            this.processedFilePath = directory.getAbsolutePath() + File.separator + PROCESSED_FILES_DIR;
            log.trace("looking for {}", processedFilePath);
            File processedFile = new File(processedFilePath);
            if(!processedFile.exists()) {
                processedFile.mkdirs();
                log.trace("created {}", processedFilePath);
            }

            for( String fileName : directory.list(filter)) {
                String fullFilePath = directory.getAbsolutePath() + File.separator + fileName;
                log.info("full file path: {}", fullFilePath);
                File fullFile = new File(fullFilePath);
                if( fullFile.isDirectory() ) {
                    log.info("omitting dir {}", fullFilePath);
                    continue;
                }
                StringBuilder results = new StringBuilder();
                try {
                    processOneFile(fullFilePath, results::append);
                } catch (IOException | InterruptedException e) {
                    log.error("Error during file processing: {}", e.getMessage(), e);
                }
                log.info("results: {}", results);

                String destinationPath = processedFilePath + File.separator + fileName;
                try {
                    Files.move(fullFile.toPath(), new File(destinationPath).toPath());
                } catch (IOException e) {
                    log.error("Error during moving file named {}: {}",fullFile.getAbsolutePath(),  e.getMessage(), e);
                }
                totalFilesProcessed.incrementAndGet();
                log.info("Moved file to {}", destinationPath);
            }
        });
        log.info("Total files processed: {}", totalFilesProcessed.get());
    }

    public void processOneFile(String fileName, Consumer<String> consumer) throws IOException, InterruptedException {
        log.info("processOneFile fileName: {}", fileName);
        DailyMedXmlFileProcessor processor = new DailyMedXmlFileProcessor();
        DailyMedXmlFileDataHolder dataHolder= processor.process(fileName);
        ProductEntityService productEntityService = new ProductEntityService();
        AutowireHelper.getInstance().autowire(productEntityService);
        setUpValidator(productEntityService);
        DailyMedXmlDataHolderProductToGsrsProductEntityConverter converter = new DailyMedXmlDataHolderProductToGsrsProductEntityConverter();
        dataHolder.getProducts().forEach((key, value) -> {
            log.info("processOneFile key: {}, value: {}", key, value);
            Product product = converter.convert(value);
            ObjectMapper mapper = new ObjectMapper();
            try {
                productEntityService.createEntity(mapper.valueToTree(product));
                log.info("Product created: {}", product);
            } catch (IOException e) {
                log.error("Error during processing of product: {}", e.getMessage(), e);
                consumer.accept("Error creating product: " + e.getMessage());
            }

        });
    }

    public static Product getProductFromFile(String filePath) throws IOException {
        File jsonFile = new File(filePath);
        if(!jsonFile.exists() || jsonFile.length() == 0) {
            log.warn("file missing or empty!");
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(jsonFile, Product.class);
    }

    private void setUpValidator(ProductEntityService productEntityService) {
        log.trace("instantiated entity service. context: {}", productEntityService.getContext());
        String methodName = "initValidator";
        try {
            Method initValidatorMethod= productEntityService.getClass().getSuperclass().getDeclaredMethod(methodName);
            initValidatorMethod.setAccessible(true);
            initValidatorMethod.invoke(productEntityService);
        } catch (NoSuchMethodException e) {
            log.error("no method found {}", methodName);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            log.error("error running method {}: {}", methodName, e);
        } catch (IllegalAccessException e) {
            log.error("access error running method {}: {}", methodName, e);
            throw new RuntimeException(e);
        }
    }
}
