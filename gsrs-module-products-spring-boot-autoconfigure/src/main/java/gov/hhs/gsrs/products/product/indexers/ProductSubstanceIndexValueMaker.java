package gov.hhs.gsrs.products.product.indexers;

import gov.hhs.gsrs.products.product.models.*;

import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import ix.ginas.models.v1.Substance;
import gsrs.DefaultDataSourceConfig;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class ProductSubstanceIndexValueMaker implements IndexValueMaker<Product> {

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
                        if (prodIng != null) {
                            if (prodIng.substanceKey != null) {
                                String subKey = prodIng.substanceKey;

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
                                }
                            }
                        }
                    }  // productIngredients
                } // for productLots
            } // for productManufactureItems

        } catch (Exception e) {
            log.warn("Error indexing ProductSubstanceIndexValueMaker:" + product.fetchKey(), e);
        }
    }
}
