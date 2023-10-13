package gov.hhs.gsrs.products.product.indexers;

import gov.hhs.gsrs.products.product.models.*;
import gov.hhs.gsrs.products.product.services.SubstanceApiService;

import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import ix.ginas.models.v1.Substance;
import gsrs.substances.dto.SubstanceDTO;
import gsrs.substances.dto.NameDTO;
import gsrs.DefaultDataSourceConfig;
import gsrs.springUtils.AutowireHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.function.Consumer;
import java.util.Optional;
import java.util.List;

@Component
@Slf4j
public class ProductSubstanceIndexValueMaker implements IndexValueMaker<Product> {

    @Autowired
    private SubstanceApiService substanceApiService;

    //@Autowire
    //  public EntityManagerSubstanceKeyResolver substanceKeyResolver;

    @PersistenceContext(unitName = DefaultDataSourceConfig.NAME_ENTITY_MANAGER)
    public EntityManager entityManager;

    @Override
    public Class<Product> getIndexedEntityClass() {
        return Product.class;
    }

    @Override
    public void createIndexableValues(Product product, Consumer<IndexableValue> consumer) {
        try {
            for (ProductManufactureItem prodManuItems : product.productManufactureItems) {
                for (ProductLot prodLot : prodManuItems.productLots) {
                    for (ProductIngredient prodIng : prodLot.productIngredients) {

                        // If Ingredient Object is not null
                        if (prodIng != null) {

                            // If substanceKey is not null
                            if (prodIng.substanceKey != null && prodIng.substanceKeyType != null) {
                                String substanceKey = prodIng.substanceKey;
                                String substanceKeyType = prodIng.substanceKeyType;

                                // Facet: "entity_link_substance" gets any Substance Key (UUID, APPROVAL_ID, BDNUM)
                               // consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", subKey);

                                // Optional.ofNullable(sub);
                                // if subKeyType == UUID or bdnum
                                // Substance API, calls Substance Key and Substance Key Type Resolver
                                //Optional<SubstanceDTO> s = substanceApiService.getSubstanceBySubstanceKeyResolver(subKey, subKeyType);

                                // Call the key resolver function, if Substance Key Type is not UUID
                                if (substanceKeyType.equalsIgnoreCase("APPROVAL_ID")) {
                                    consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", substanceKey));
                                } else {
                                    // ENTITY MANAGER Substance Key Resolver
                                    Optional<Substance> substance = substanceApiService.getEntityManagerSubstanceBySubstanceKeyResolver(substanceKey, substanceKeyType);
                                    if (substance.get() != null) {
                                        if (substance.get().uuid != null) {
                                            consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", substance.get().uuid.toString()));
                                        }
                                    }
                                }

                                /*
                                if (s.get() != null) {
                                    if (s.get().getUuid() != null) {
                                        consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", s.get().getUuid().toString()));
                                    }
                                } */

                                // Call Substance API to get Substance Names by any Substance Key
                                Optional<List<NameDTO>> names = substanceApiService.getNamesOfSubstance(substanceKey);

                                // get Names by Any Substance Key (UUID, APPROVAL_ID, BDNUM)
                                if (names.isPresent()) {
                                    names.get().forEach(nameObj -> {
                                        if (nameObj.getName() != null) {
                                            // Facet: "Ingredient Name" gets all Ingredient Names
                                            consumer.accept(IndexableValue.simpleFacetStringValue("Ingredient Name", nameObj.getName()).suggestable().setSortable());


                                            if (nameObj.isDisplayName() == true) {
                                                // Facet: "Ingredient Name (Preferred)" gets Preferred Name
                                                consumer.accept(IndexableValue.simpleFacetStringValue("Ingredient Name (Preferred)", nameObj.getName()).suggestable().setSortable());
                                            }
                                        }
                                    });
                                }

                                /*  OLD CODE
                                //Get Substance Object by Substance Key
                                Query query = entityManager.createQuery("SELECT s FROM Substance s JOIN s.codes c WHERE c.type = 'PRIMARY' and c.code=:subKey");
                                query.setParameter("subKey", subKey);
                                Substance s = (Substance) query.getSingleResult();

                                if (s != null) {
                                    if (s.uuid != null) {
                                        consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", s.uuid.toString()));
                                    }

                                    // All Ingredient Names
                                    s.names.forEach(nameObj -> {
                                        if (nameObj.name != null) {
                                            consumer.accept(IndexableValue.simpleFacetStringValue("Ingredient Name", nameObj.name).suggestable().setSortable());
                                        }
                                    });

                                    // Facet: "Ingredient Name (Preferred)"
                                    if (s.getName() != null) {
                                        consumer.accept(IndexableValue.simpleFacetStringValue("Ingredient Name (Preferred)", s.getName()).suggestable().setSortable());
                                    }
                                } */
                            }
                        }
                    }  // productIngredients
                } // for productLots
            } // for productManufactureItems

        } catch (Exception e) {
            log.warn("Error indexing ProductSubstanceIndexValueMaker:" + product.fetchKey(), e);
        }
    }

    /*
    public void createIndexableValuesByEntityManagerResolver(consumer, String substanceKey, String substanceKeyType) {

    }

    public void createIndexableValuesBySubstanceApiResolver(Product product, Consumer<IndexableValue> consumer) {

    }*/
}
