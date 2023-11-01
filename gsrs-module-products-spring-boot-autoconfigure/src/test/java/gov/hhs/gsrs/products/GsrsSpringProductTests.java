package gov.hhs.gsrs.products;

import gov.hhs.gsrs.products.product.models.Product;
import gov.hhs.gsrs.products.product.services.ProductEntityService;
import gov.hhs.gsrs.products.product.repositories.ProductRepository;

import gsrs.controller.GsrsControllerConfiguration;
import gsrs.service.AbstractGsrsEntityService;

import gsrs.startertests.GsrsEntityTestConfiguration;
import gsrs.startertests.GsrsJpaTest;
import gsrs.startertests.jupiter.AbstractGsrsJpaEntityJunit5Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.time.LocalDate;

//@Disabled
@ActiveProfiles("test")
@GsrsJpaTest(classes = {GsrsSpringApplication.class, GsrsControllerConfiguration.class, GsrsEntityTestConfiguration.class, ProductRepository.class})
@Import({Product.class, ProductEntityService.class})
public class GsrsSpringProductTests extends AbstractGsrsJpaEntityJunit5Test {

    @Autowired
    private ProductEntityService productEntityService;

    @Autowired
    private ProductRepository repository;

    private JacksonTester<Product> productJson;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void createRecord() throws Exception {
        Product prod = new Product();
        prod.routeAdmin = "ORAL";
        prod.id = 10000000L;


        AbstractGsrsEntityService.CreationResult<Product> result = productEntityService.createEntity(objectMapper.valueToTree(prod));
        assertTrue(result.isCreated());
        Product prodCreated = result.getCreatedEntity();

        assertNotNull(prodCreated.getId());

    }
}
