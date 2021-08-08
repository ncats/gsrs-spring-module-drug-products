package gov.nih.ncats.product.product;

import gov.nih.ncats.product.product.controllers.ProductController;
import gov.nih.ncats.product.product.searcher.LegacyProductSearcher;
import gov.nih.ncats.product.product.services.ProductEntityService;

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
