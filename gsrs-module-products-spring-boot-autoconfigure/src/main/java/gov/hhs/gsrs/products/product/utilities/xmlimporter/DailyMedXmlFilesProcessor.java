package gov.hhs.gsrs.products.product.utilities.xmlimporter;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DailyMedXmlFilesProcessor {

    private boolean pretty = false;

    private String directoryPath;

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void setPretty(boolean pretty) {
        this.pretty = pretty;
    }
    public boolean getPretty() {
        return this.pretty;
    }

    public String _getTestDirectoryPath() {
        ClassPathResource directoryPathResource = new ClassPathResource("data");
        try {
            return directoryPathResource.getFile().getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getTestDirectoryPath() {

        return "/home/aruna/Desktop/Prescription/GSRSProductFolder/cosmetics";//""/home/aruna/Desktop/Prescription/Only_xmls/Sample_forTest";
    }

    @Test
    public void processFolderTest() {
        this.setDirectoryPath(getTestDirectoryPath());
        try {
            this.processFolder(this.directoryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processFolder(String directoryPath) throws IOException {
        DailyMedXmlFileProcessor dailyMedXmlFileProcessor = new DailyMedXmlFileProcessor();
        String globPattern = "*.xml"; // Match all .txt files
        Path directory = Paths.get(directoryPath);
        FileSystem fs = FileSystems.getDefault();
        PathMatcher matcher = fs.getPathMatcher("glob:" + globPattern);
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory))  {
            for (Path path : stream) {
                if (matcher.matches(path.getFileName())) {
                    System.out.println(path.getFileName());

                    String outputBaseName = FilenameUtils.removeExtension(path.getFileName().toString());

                    DailyMedXmlFileDataHolder holder = dailyMedXmlFileProcessor.process(path.toString());
                    AtomicInteger index= new AtomicInteger();
                    index.getAndIncrement();
                    holder.products.entrySet().forEach(p->{

                        DailyMedXmlDataHolderProductToGsrsProductEntityConverter converter = new DailyMedXmlDataHolderProductToGsrsProductEntityConverter();
                        gov.hhs.gsrs.products.product.models.Product product = converter.convert(p.getValue());

                        // This is a placeholder approach
                        // In ProductXmlTask you can do something like
                        // productsService.createEntity(product)

                        String outputFilePath = directory+"/json/"+outputBaseName+index.getAndIncrement()+".json";
                        try {
                            FileWriter fileWriter = new FileWriter(outputFilePath);
                            fileWriter.write(converter.asSerialized(product, this.getPretty()));
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // converter.asSerialized(product, this.getPretty());
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
