package gov.nih.ncats.product.productall.searcher;

import gov.nih.ncats.product.productall.models.*;
import gov.nih.ncats.product.productall.repositories.*;

import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.GsrsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyProductAllSearcher extends LegacyGsrsSearchService<ProductMainAll> {

    @Autowired
    public LegacyProductAllSearcher(ProductAllRepository repository) {
        super(ProductMainAll.class, repository);
    }
}
