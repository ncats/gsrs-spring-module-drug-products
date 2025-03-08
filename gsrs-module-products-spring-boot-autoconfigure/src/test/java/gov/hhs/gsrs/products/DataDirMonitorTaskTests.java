package gov.hhs.gsrs.products;

import gov.hhs.gsrs.products.processor.DailyMedXmlFileProcessor;
import gov.hhs.gsrs.products.processor.model.DailyMedXmlFileDataHolder;
import gov.hhs.gsrs.products.processor.model.ImportProduct;
import gov.hhs.gsrs.products.product.models.Product;
import gov.hhs.gsrs.products.product.tasks.DataDirMonitorTask;
import gsrs.controller.GsrsControllerConfiguration;
import gsrs.springUtils.AutowireHelper;
import gsrs.startertests.GsrsEntityTestConfiguration;
import gsrs.startertests.jupiter.AbstractGsrsJpaEntityJunit5Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@ActiveProfiles("test")
//@ContextConfiguration(classes = { GsrsEntityTestConfiguration.class, GsrsControllerConfiguration.class})
@WithMockUser(username = "admin", roles = "Admin")
@EntityScan(basePackages ={"ix","gsrs", "gov.nih.ncats"} )
//@SpringBootTest(classes = GsrsSpringApplication.class)
public class DataDirMonitorTaskTests {

    @Test
    void getProductFromFileTest() throws IOException {
        String fileName = "json/product_Nov-22-2024_10-42-07.json";
        File dataFile = new ClassPathResource(fileName).getFile();
        Product product = DataDirMonitorTask.getProductFromFile(dataFile.getAbsolutePath());
        String expectedManufacturer = "Heritage Pharmaceuticals Inc. d/b/a Avet Pharmaceuticals Inc.";
        Assertions.assertEquals(product.manufacturerName, expectedManufacturer);
    }


    @Test
    void getNullProductFromFileTest() throws IOException {
        String fileName = "json/emptyEntity.json";
        File dataFile = new ClassPathResource(fileName).getFile();
        Product product = DataDirMonitorTask.getProductFromFile(dataFile.getAbsolutePath());
        Assertions.assertNull(product);
    }

    @Test
    void readFileTest() throws IOException {
        String fileName = "xml/chewing_gum.xml";
        File dataFile = new ClassPathResource(fileName).getFile();
        DailyMedXmlFileProcessor processor = new DailyMedXmlFileProcessor();
        DailyMedXmlFileDataHolder dataHolder= processor.process(dataFile.getAbsolutePath());
        Assertions.assertEquals(1, dataHolder.getProducts().size());
    }

    @Test
    void readFile2Test() throws IOException {
        String fileName = "xml/60173abd-c2e6-4100-b465-36c60d879029.xml";
        File dataFile = new ClassPathResource(fileName).getFile();
        DailyMedXmlFileProcessor processor = new DailyMedXmlFileProcessor();
        DailyMedXmlFileDataHolder dataHolder= processor.process(dataFile.getAbsolutePath());
        for(Map.Entry<String, ImportProduct> prod : dataHolder.getProducts().entrySet()){
            System.out.printf("key: %s, code: %d\n", prod.getKey(), prod.getValue().getNdcCode());
        }
        Assertions.assertEquals(2, dataHolder.getProducts().size());
    }

    //@Test
    void processOneFileTest() throws IOException, InterruptedException {
        String fileName = "xml/chewing_gum.xml";
        File dataFile = new ClassPathResource(fileName).getFile();
        DataDirMonitorTask task = new DataDirMonitorTask();
        AutowireHelper helper = AutowireHelper.getInstance();
        helper.autowire(task);
        String currentPath = System.getProperty("user.dir");
        File currentDir = new File(currentPath);
        String scriptFilePath = currentDir.getParentFile().getParentFile().getParentFile().getAbsolutePath()
                +"/scripts/xml_parser_productlevel_mm.py";
        System.out.printf("currentPath: %s; scriptFilePath: %s\n", currentPath, scriptFilePath);
        task.setFirstScriptPath(scriptFilePath);
        String tempDir = System.getProperty("java.io.tmpdir");
        System.out.printf("tempDir: %s; scriptFilePath: %s\n", tempDir, scriptFilePath);
        task.setProcessedFilePath(tempDir+ File.pathSeparatorChar + "processed");
        StringBuilder log = new StringBuilder();
        task.processOneFile(dataFile.getAbsolutePath(), log::append);
        System.out.printf("logs: %s\n", log.toString());
        Assertions.assertTrue(true);
    }
}
