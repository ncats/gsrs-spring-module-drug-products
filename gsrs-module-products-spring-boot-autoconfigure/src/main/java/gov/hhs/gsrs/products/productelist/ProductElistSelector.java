package gov.hhs.gsrs.products.productelist;

import gov.hhs.gsrs.products.productelist.controllers.ProductElistController;
import gov.hhs.gsrs.products.productelist.searcher.LegacyProductElistSearcher;
import gov.hhs.gsrs.products.productelist.services.ProductElistEntityService;

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
