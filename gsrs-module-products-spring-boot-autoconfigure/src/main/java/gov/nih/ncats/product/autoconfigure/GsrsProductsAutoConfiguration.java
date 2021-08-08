package gov.nih.ncats.product.autoconfigure;

import gov.nih.ncats.product.productall.services.SubstanceModuleService;
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
