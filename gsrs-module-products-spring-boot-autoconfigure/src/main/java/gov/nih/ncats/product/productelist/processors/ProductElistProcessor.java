package gov.nih.ncats.product.productelist.processors;

import gov.nih.ncats.product.productelist.models.*;
import gov.nih.ncats.product.productelist.controllers.*;

import gsrs.springUtils.AutowireHelper;
import ix.core.EntityProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
public class ProductElistProcessor implements EntityProcessor<ProductElist> {

    @Autowired
    public ProductElistController productController;

    @Override
    public Class<ProductElist> getEntityClass() {
        return ProductElist.class;
    }

    @Override
    public void postLoad(ProductElist obj) throws FailProcessingException {
    }
}
