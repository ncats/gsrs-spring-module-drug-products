package gov.nih.ncats.product.productelist;

import gov.nih.ncats.product.productelist.controllers.ProductElistController;
import gov.nih.ncats.product.productelist.searcher.LegacyProductElistSearcher;
import gov.nih.ncats.product.productelist.services.ProductElistEntityService;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ProductElistSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                ProductElistEntityService.class.getName(),
                LegacyProductElistSearcher.class.getName(),
                ProductElistController.class.getName()
        };
    }
}
