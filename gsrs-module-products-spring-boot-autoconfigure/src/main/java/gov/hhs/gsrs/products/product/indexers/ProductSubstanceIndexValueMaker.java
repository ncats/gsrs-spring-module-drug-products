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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

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

    @PersistenceContext(unitName = DefaultDataSourceConfig.NAME_ENTITY_MANAGER)
    public EntityManager entityManager;

    @Value("${substance.product.ivm.substancekey.resolver.touse}")
    private String substanceKeyResolverToUseFromConfig;


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

                                // Get from Config which Substance Key Resolver to use. Substance API or Entity Mananger
                                if (substanceKeyResolverToUseFromConfig != null) {
                                    if (substanceKeyResolverToUseFromConfig.equalsIgnoreCase("api")) {
                                        // Call SUBSTANCE API Substance Resolver
                                        createIndexableValuesBySubstanceApiResolver(consumer, substanceKey, substanceKeyType);
                                    } else {
                                        // Call ENTITY MANAGER Substance Resolver
                                        createIndexableValuesByEntityManagerResolver(consumer, substanceKey, substanceKeyType);
                                    }
                                }

                            }  // product substance key not null
                        } // product Ingredient is not null
                    }  // for productIngredients
                } // for productLots
            } // for productManufactureItems


            // Facet: Application Type Number - Create a facet by combining Application Type and Application Number
            for (ProductProvenance prodProv : product.productProvenances) {
                if (prodProv != null) {
                    if ((prodProv.applicationType != null) && (prodProv.applicationNumber != null)) {
                        consumer.accept(IndexableValue.simpleFacetStringValue("Application Type Number", prodProv.applicationType + " " + prodProv.applicationNumber).suggestable().setSortable());
                    }
                }

                // The Product Code is stored as String in the database, and need this IVM so can
                // sort by numeric values on the frontend.
                for (ProductCode prodCode : prodProv.productCodes) {
                    if (prodCode != null) {
                        if (prodCode.productCode != null) {
                            // Remove hypen - from Product Code. Change 13334-155 to 13334155
                            String codeRemovedHypen = prodCode.productCode.replaceAll("-", "");
                            // Check if code has numeric values or not. If has number values, create IVM
                            if (StringUtils.isNumeric(codeRemovedHypen) == true) {
                                Long codeLong = Long.parseLong(codeRemovedHypen);
                                consumer.accept(IndexableValue.simpleLongValue("Product Code", codeLong).suggestable().setSortable());
                            }
                        }
                    }
                }
            } // for ProductProvenance

        } catch (Exception e) {
            log.warn("Error indexing ProductSubstanceIndexValueMaker:" + product.fetchKey(), e);
        }
    }

    public void createIndexableValuesBySubstanceApiResolver(Consumer<IndexableValue> consumer, String substanceKey, String substanceKeyType) {

        // If Substance Key Type is APPROVAL_ID, BDNUM, or other key type, get the Substance record by Resolver
        if ((substanceKeyType != null) && (!substanceKeyType.equalsIgnoreCase("UUID"))) {
            // SUBSTANCE API Substance Key Resolver, if Substance Key Type is UUID, APPROVAL_ID, BDNUM, Other keys
            Optional<SubstanceDTO> substance = substanceApiService.getSubstanceBySubstanceKeyResolver(substanceKey, substanceKeyType);

            if (substance.get() != null) {
                if (substance.get().getUuid() != null) {
                    consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", substance.get().getUuid().toString()));
                }
            }
        } else {
            // If Substance Key Type is UUID, use that substanceKey
            consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", substanceKey));
        }

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
    }

    public void createIndexableValuesByEntityManagerResolver(Consumer<IndexableValue> consumer, String substanceKey, String substanceKeyType) {

        // ENTITY MANAGER Substance Key Resolver, if Substance Key Type is UUID, APPROVAL_ID, BDNUM, Other keys
        Optional<Substance> substance = substanceApiService.getEntityManagerSubstanceBySubstanceKeyResolver(substanceKey, substanceKeyType);

        if (substance.get() != null) {
            if (substance.get().uuid != null) {
                consumer.accept(IndexableValue.simpleStringValue("entity_link_substances", substance.get().uuid.toString()));

                // Get ALL Substance Names
                if (substance.get().names.size() > 0) {
                    substance.get().names.forEach(nameObj -> {

                        if (nameObj.name != null) {
                            // Facet: "Ingredient Name" gets all Ingredient Names
                            consumer.accept(IndexableValue.simpleFacetStringValue("Ingredient Name", nameObj.name).suggestable().setSortable());
                        }

                        if (nameObj.isDisplayName() == true) {
                            // Facet: "Ingredient Name (Preferred)" gets Preferred Name
                            consumer.accept(IndexableValue.simpleFacetStringValue("Ingredient Name (Preferred)", nameObj.name).suggestable().setSortable());
                        }
                    });
                }
            }
        }
    }

}
