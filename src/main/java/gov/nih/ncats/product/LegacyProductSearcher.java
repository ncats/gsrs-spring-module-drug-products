package gov.nih.ncats.product;

import gov.nih.ncats.product.models.Product;
import gov.nih.ncats.product.repositories.ProductRepository;

import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.GsrsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyProductSearcher extends LegacyGsrsSearchService<Product> {

    @Autowired
    public LegacyProductSearcher(ProductRepository repository) {
        super(Product.class, repository);
    }
}
