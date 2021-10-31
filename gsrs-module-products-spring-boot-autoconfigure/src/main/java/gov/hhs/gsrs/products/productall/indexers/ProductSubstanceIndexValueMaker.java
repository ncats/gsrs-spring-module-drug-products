package gov.hhs.gsrs.products.productall.indexers;

import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import gov.hhs.gsrs.products.productall.models.ProductIngredientAll;
import gov.hhs.gsrs.products.productall.models.ProductMainAll;
import gsrs.DefaultDataSourceConfig;
import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import ix.ginas.models.v1.Substance;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductSubstanceIndexValueMaker implements IndexValueMaker<ProductMainAll> {

    @PersistenceContext(unitName = DefaultDataSourceConfig.NAME_ENTITY_MANAGER)
    public EntityManager entityManager;

    @Override
    public Class<ProductMainAll> getIndexedEntityClass() {
        return ProductMainAll.class;
    }

    @Override
    public void createIndexableValues(ProductMainAll product, Consumer<IndexableValue> consumer) {
        try {
            for (ProductIngredientAll ing : product.productIngredientAllList) {
                if (ing != null) {
                    if (ing.substanceKey != null) {
                        String subKey = ing.substanceKey;

                        //Get Substance Object by Substance Key
                        Query query = entityManager.createQuery("SELECT s FROM Substance s JOIN s.codes c WHERE c.type = 'PRIMARY' and c.code=:subKey");
                        query.setParameter("subKey", subKey);
                        Substance s = (Substance) query.getSingleResult();

                        if (s != null) {
                            if (s.uuid != null) {
                                consumer.accept(IndexableValue.simpleStringValue("entity_link_substances",  s.uuid.toString()));
                            }

                            // All Ingredient Names
                            s.names.forEach(nameObj -> {
                                if (nameObj.name != null) {
                                    consumer.accept(IndexableValue.simpleFacetStringValue("Ingredient Name", nameObj.name).suggestable().setSortable());
                                }
                            });
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.warn("Error indexing product:" + product.fetchKey(), e);
        }
    }
}
