package gov.hhs.gsrs.products.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


@RestClientTest(ProductsApi.class)
public class GsrsSpringProductApiTest {

    @Autowired
    private ProductsApi prodApi;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @TestConfiguration
    static class Testconfig{
        @Bean
        public ProductsApi productsApi(RestTemplateBuilder restTemplateBuilder){
            return new ProductsApi(restTemplateBuilder, "http://testing.com", new ObjectMapper());
        }
    }

    @BeforeEach
    public void setup(){
        this.mockRestServiceServer.reset();
    }

    @AfterEach
    public void verify(){
        this.mockRestServiceServer.verify();
    }

    @Test
    public void count() throws IOException {
        this.mockRestServiceServer
                .expect(requestTo("/api/v1/products/@count"))
                .andRespond(withSuccess("10", MediaType.APPLICATION_JSON));

        assertEquals(10L, prodApi.count());
    }
}
