package gov.hhs.gsrs.products.productelist.repositories;

import gov.hhs.gsrs.products.productelist.models.*;

import gsrs.repository.GsrsVersionedRepository;
import gsrs.repository.GsrsRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductElistRepository extends GsrsVersionedRepository<ProductElist, String> {

    List<ProductElist> findByProductId(String productId);
}
