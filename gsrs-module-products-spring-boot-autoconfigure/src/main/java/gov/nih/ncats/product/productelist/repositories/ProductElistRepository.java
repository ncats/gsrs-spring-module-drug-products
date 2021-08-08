package gov.nih.ncats.product.productelist.repositories;

import gov.nih.ncats.product.productelist.models.*;

import gsrs.repository.GsrsVersionedRepository;
import gsrs.repository.GsrsRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductElistRepository extends GsrsVersionedRepository<ProductElist, String> {

    List<ProductElist> findByProductId(String productId);
}
