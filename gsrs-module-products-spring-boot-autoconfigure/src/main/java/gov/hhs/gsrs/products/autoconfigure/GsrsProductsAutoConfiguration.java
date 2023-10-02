package gov.hhs.gsrs.products.autoconfigure;

import gov.hhs.gsrs.products.product.services.SubstanceModuleService;

import gsrs.EnableGsrsApi;
import gsrs.EnableGsrsJpaEntities;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableGsrsJpaEntities
@EnableGsrsApi
@Configuration
@Import({
         SubstanceModuleService.class
})
public class GsrsProductsAutoConfiguration {
}
