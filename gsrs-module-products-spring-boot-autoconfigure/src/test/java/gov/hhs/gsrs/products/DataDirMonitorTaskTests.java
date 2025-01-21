package gov.hhs.gsrs.products;

import gov.hhs.gsrs.products.product.models.Product;
import gov.hhs.gsrs.products.product.tasks.DataDirMonitorTask;
import gsrs.startertests.GsrsJpaTest;
import gsrs.startertests.jupiter.AbstractGsrsJpaEntityJunit5Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.IOException;

@ActiveProfiles("test")
@GsrsJpaTest(dirtyMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = GsrsProductsSpringApplication.class)
public class DataDirMonitorTaskTests extends AbstractGsrsJpaEntityJunit5Test {
//, GsrsEntityTestConfiguration.class, , GsrsControllerConfiguration.class
    // extends AbstractGsrsJpaEntityJunit5Test


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
    void processOneFileTest() throws IOException, InterruptedException {
        String fileName = "xml/chewing_gum.xml";
        File dataFile = new ClassPathResource(fileName).getFile();
        DataDirMonitorTask task = new DataDirMonitorTask();
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
