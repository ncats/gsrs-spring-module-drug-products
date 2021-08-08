package gov.nih.ncats.product.productall.repositories;

import gov.nih.ncats.product.productall.models.ProductMainAll;
import gsrs.repository.GsrsVersionedRepository;
import gsrs.repository.GsrsRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductAllRepository extends GsrsVersionedRepository<ProductMainAll, String> {

    Optional<ProductMainAll> findById(String id);

    @Query("SELECT DISTINCT p.provenance FROM ProductMainAll p WHERE p.productId in (SELECT productId FROM ProductIngredientAll WHERE substanceUuid = ?1)")
    List<String> findProvenanceBySubstanceUuid(String substanceUuid);
}
