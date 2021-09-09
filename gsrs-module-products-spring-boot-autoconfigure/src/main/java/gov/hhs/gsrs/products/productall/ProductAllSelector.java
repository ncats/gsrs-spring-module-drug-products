package gov.hhs.gsrs.products.productall;

import gov.hhs.gsrs.products.productall.controllers.ProductAllController;
import gov.hhs.gsrs.products.productall.searcher.LegacyProductAllSearcher;
import gov.hhs.gsrs.products.productall.services.ProductAllEntityService;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ProductAllSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                ProductAllEntityService.class.getName(),
                LegacyProductAllSearcher.class.getName(),
                ProductAllController.class.getName()
        };
    }
}
