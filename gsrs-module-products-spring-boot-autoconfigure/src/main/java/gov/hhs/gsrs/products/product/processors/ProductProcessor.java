package gov.hhs.gsrs.products.product.processors;

import gov.hhs.gsrs.products.product.models.Product;
import gov.hhs.gsrs.products.product.controllers.ProductController;

import gsrs.springUtils.AutowireHelper;
import ix.core.EntityProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
public class ProductProcessor implements EntityProcessor<Product> {

    @Autowired
    public ProductController productController;

    @Override
    public Class<Product> getEntityClass() {
        return Product.class;
    }

    @Override
    public void postLoad(Product obj) throws FailProcessingException {
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
