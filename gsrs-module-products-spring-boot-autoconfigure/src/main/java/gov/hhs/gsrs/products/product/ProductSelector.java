package gov.hhs.gsrs.products.product;

import gov.hhs.gsrs.products.product.controllers.ProductController;
import gov.hhs.gsrs.products.product.searcher.LegacyProductSearcher;
import gov.hhs.gsrs.products.product.services.ProductEntityService;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ProductSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                ProductEntityService.class.getName(),
                LegacyProductSearcher.class.getName(),
                ProductController.class.getName()
        };
    }
}
