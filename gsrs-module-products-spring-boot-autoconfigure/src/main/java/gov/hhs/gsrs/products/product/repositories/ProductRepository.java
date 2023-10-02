package gov.hhs.gsrs.products.product.repositories;

import gov.hhs.gsrs.products.product.models.Product;

import gsrs.repository.GsrsVersionedRepository;
import gsrs.repository.GsrsRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductRepository extends GsrsVersionedRepository<Product, Long> {

    Optional<Product> findById(Long id);

    @Query("SELECT DISTINCT pp.provenance FROM ProductProvenance pp WHERE pp.owner.id in (SELECT p.id FROM Product p JOIN p.productManufactureItems pmi JOIN pmi.productLots l JOIN l.productIngredients pin where pin.substanceKey = ?1)")
    List<String> findProvenanceBySubstance(String substanceUuid);
}
