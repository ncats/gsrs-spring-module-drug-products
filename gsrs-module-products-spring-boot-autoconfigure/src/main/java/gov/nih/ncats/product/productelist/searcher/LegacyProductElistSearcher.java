package gov.nih.ncats.product.productelist.searcher;

import gov.nih.ncats.product.productelist.models.*;
import gov.nih.ncats.product.productelist.repositories.ProductElistRepository;

import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.GsrsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyProductElistSearcher extends LegacyGsrsSearchService<ProductElist> {

    @Autowired
    public LegacyProductElistSearcher(ProductElistRepository repository) {
        super(ProductElist.class, repository);
    }
}
