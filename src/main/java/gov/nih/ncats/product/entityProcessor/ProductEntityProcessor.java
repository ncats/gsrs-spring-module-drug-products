package gov.nih.ncats.product.entityProcessor;

import gov.nih.ncats.product.model.Product;
import ix.core.EntityProcessor;
import org.springframework.stereotype.Component;

//@Component
public class ProductEntityProcessor implements EntityProcessor<Product> {
    @Override
    public Class<Product> getEntityClass() {
        return Product.class;
    }

    @Override
    public void postLoad(Product obj) throws FailProcessingException {
        System.out.println("post Load Product " + obj);
    }
}
