package gov.hhs.gsrs.products.product.repositories;

import gov.hhs.gsrs.products.product.models.Product;

import gsrs.repository.GsrsVersionedRepository;
import gsrs.repository.GsrsRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ProductRepository extends GsrsVersionedRepository<Product, Long> {

    Optional<Product> findById(Long id);
}
