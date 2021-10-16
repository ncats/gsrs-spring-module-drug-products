package gov.hhs.gsrs.products.productall.indexers;

import gov.hhs.gsrs.products.productall.models.*;

import gsrs.DefaultDataSourceConfig;
import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import ix.ginas.models.v1.Substance;

import javax.persistence.*;
import java.util.function.Consumer;

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
            String result = "HAS_NO_INGREDIENT";
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
                                result = s.uuid.toString();
                                consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", result));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
