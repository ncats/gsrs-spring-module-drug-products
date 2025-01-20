package gov.hhs.gsrs.products.processor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.*;

@Setter
@Slf4j
public class DailyMedXmlFilesProcessor {

    private String directoryPath;

    public String getTestDirectoryPath() {

        return "/home/aruna/Desktop/Prescription/Only_xmls/Sample_forTest";
    }

    @Test
    public void processFolderTest() {
        this.setDirectoryPath(getTestDirectoryPath());
        try {
            this.processFolder(this.directoryPath);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void processFolder(String directoryPath) throws IOException {
        DailyMedXmlFileProcessor dailyMedXmlFileProcessor = new DailyMedXmlFileProcessor();
        String globPattern = "*.xml"; // Match all .txt files
        Path directory = Paths.get(directoryPath);
        FileSystem fs = FileSystems.getDefault();
        PathMatcher matcher = fs.getPathMatcher("glob:" + globPattern);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                if (matcher.matches(path.getFileName())) {
                    log.trace("processing file {}", path.getFileName());
                    DailyMedXmlFileDataHolder holder = dailyMedXmlFileProcessor.process(path.toString());
                    holder.products.forEach((key, value) -> {
                        DailyMedXmlDataHolderProductToGsrsProductEntityConverter converter = new DailyMedXmlDataHolderProductToGsrsProductEntityConverter();
                        gov.hhs.gsrs.products.product.models.Product product = converter.convert(value);
                        converter.printSerialized(product);
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
