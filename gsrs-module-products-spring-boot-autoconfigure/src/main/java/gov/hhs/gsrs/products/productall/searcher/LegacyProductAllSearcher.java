package gov.hhs.gsrs.products.productall.searcher;

import gov.hhs.gsrs.products.productall.models.*;
import gov.hhs.gsrs.products.productall.repositories.*;

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
