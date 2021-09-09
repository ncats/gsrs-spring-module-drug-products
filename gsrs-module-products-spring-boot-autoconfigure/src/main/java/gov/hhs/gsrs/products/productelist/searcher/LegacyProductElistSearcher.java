package gov.hhs.gsrs.products.productelist.searcher;

import gov.hhs.gsrs.products.productelist.models.*;
import gov.hhs.gsrs.products.productelist.repositories.ProductElistRepository;

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
