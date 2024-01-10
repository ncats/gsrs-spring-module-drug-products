package gov.hhs.gsrs.products.autoconfigure;

import gov.hhs.gsrs.products.product.services.SubstanceApiService;
import gov.hhs.gsrs.products.autoconfigure.SubstancesApiConfiguration;

import gsrs.api.substances.SubstanceRestApi;
import gsrs.EnableGsrsApi;
import gsrs.EnableGsrsJpaEntities;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableGsrsJpaEntities
@EnableGsrsApi
@Configuration
@Import({
         SubstanceApiService.class
})
public class GsrsProductsAutoConfiguration {
    private ObjectMapper mapper = new ObjectMapper();

    @Bean
    public SubstanceRestApi substanceRestApi(SubstancesApiConfiguration substancesApiConfiguration){
        return new SubstanceRestApi(substancesApiConfiguration.createNewRestTemplateBuilder(), substancesApiConfiguration.getBaseURL(), mapper);
    }
}
