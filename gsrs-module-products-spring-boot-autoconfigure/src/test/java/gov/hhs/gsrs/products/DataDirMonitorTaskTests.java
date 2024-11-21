package gov.hhs.gsrs.products;

import gov.hhs.gsrs.products.product.models.Product;
import gov.hhs.gsrs.products.product.tasks.DataDirMonitorTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class DataDirMonitorTaskTests {

    @Test
    void getFileNameTest() {
        String fullFilePath = "D:\\temp\\product_data\\23dbde51-ded9-505e-e063-6294a90a46aa.xml";
        String expected = "23dbde51-ded9-505e-e063-6294a90a46aa.xml";
        String fileName= DataDirMonitorTask.getFileName(fullFilePath);
        Assertions.assertEquals(expected, fileName);
    }

    @Test
    void testSubstitution() {
        String fullFilePath = "D:\\temp\\product_data\\23dbde51-ded9-505e-e063-6294a90a46aa.xml";
        String result = fullFilePath.replace("\\", "\\\\");
        int totalBackSlash = 0;
        for(char c : result.toCharArray() ) {
            if(c == '\\') totalBackSlash++;
        }
        Assertions.assertEquals(6, totalBackSlash);
    }

    @Test
    void getProductFromFileTest() throws IOException {
        String fileName = "json/product.2.json";
        File dataFile = new ClassPathResource(fileName).getFile();
        Product product = DataDirMonitorTask.getProductFromFile(dataFile.getAbsolutePath());
        String expectedManufacturer = "Smith";
        Assertions.assertEquals(product.manufacturerName, expectedManufacturer);
    }


    @Test
    void getNullProductFromFileTest() throws IOException {
        String fileName = "json/emptyEntity.json";
        File dataFile = new ClassPathResource(fileName).getFile();
        Product product = DataDirMonitorTask.getProductFromFile(dataFile.getAbsolutePath());
        Assertions.assertNull(product);
    }
}
