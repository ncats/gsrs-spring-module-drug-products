package gov.hhs.gsrs.products.autoconfigure;

import gov.hhs.gsrs.products.product.services.SubstanceApiService;
import gsrs.api.substances.SubstanceRestApi;
import gsrs.EnableGsrsApi;
import gsrs.EnableGsrsJpaEntities;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Primary;

@EnableGsrsJpaEntities
@EnableGsrsApi
@AutoConfiguration
@Import({
         SubstanceApiService.class
})
public class GsrsProductsAutoConfiguration {
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SubstanceRestApi substanceRestApi(SubstancesApiConfiguration substancesApiConfiguration){
        return new SubstanceRestApi(substancesApiConfiguration.createNewRestTemplateBuilder(), substancesApiConfiguration.getBaseURL(), objectMapper());
    }
}
