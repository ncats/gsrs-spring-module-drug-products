package gov.hhs.gsrs.products.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.gsrs.products.product.services.SubstanceApiService;
import gsrs.api.substances.SubstanceRestApi;
import gsrs.EnableGsrsApi;
import gsrs.EnableGsrsJpaEntities;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;

import tools.jackson.databind.json.JsonMapper;
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
    public JsonMapper objectMapper() {
        return JsonMapper.builderWithJackson2Defaults().build();
    }

    @Bean
    public SubstanceRestApi substanceRestApi(SubstancesApiConfiguration substancesApiConfiguration){
        //temporarily using ObjectMapper
        return new SubstanceRestApi(substancesApiConfiguration.createNewRestTemplateBuilder(), substancesApiConfiguration.getBaseURL(), new ObjectMapper());
    }
}
