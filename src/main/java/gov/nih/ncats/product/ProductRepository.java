package gov.nih.ncats.product;

import gov.nih.ncats.product.model.Product;
import gsrs.repository.GsrsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ProductRepository extends GsrsRepository<Product, Long> {

    Optional<Product> findBySource(String source);
}
