package gov.nih.ncats.product.productall.processors;

import gov.nih.ncats.product.productall.models.*;
import gov.nih.ncats.product.productall.controllers.*;

import gsrs.springUtils.AutowireHelper;
import ix.core.EntityProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
public class ProductAllProcessor implements EntityProcessor<ProductMainAll> {

    @Autowired
    public ProductAllController productController;

    @Override
    public Class<ProductMainAll> getEntityClass() {
        return ProductMainAll.class;
    }

    @Override
    public void postLoad(ProductMainAll obj) throws FailProcessingException {
        /*
        if(productController==null) {
            AutowireHelper.getInstance().autowire(this);
        }

        if (productController != null) {
            Optional<Product> objInjectSubstance = productController.injectSubstanceDetails(Optional.of(obj));
        }
         */
    }
}
