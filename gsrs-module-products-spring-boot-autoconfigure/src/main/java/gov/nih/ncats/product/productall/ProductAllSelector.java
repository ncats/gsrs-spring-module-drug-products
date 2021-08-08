package gov.nih.ncats.product.productall;

import gov.nih.ncats.product.productall.controllers.ProductAllController;
import gov.nih.ncats.product.productall.searcher.LegacyProductAllSearcher;
import gov.nih.ncats.product.productall.services.ProductAllEntityService;

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
