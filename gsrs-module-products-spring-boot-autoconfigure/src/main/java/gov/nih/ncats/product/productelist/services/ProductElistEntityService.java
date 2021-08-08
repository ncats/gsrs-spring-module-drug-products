package gov.nih.ncats.product.productelist.services;

import gov.nih.ncats.product.productelist.models.*;
import gov.nih.ncats.product.productelist.repositories.ProductElistRepository;

import gsrs.controller.IdHelpers;
import gsrs.events.AbstractEntityCreatedEvent;
import gsrs.events.AbstractEntityUpdatedEvent;
// import gsrs.module.substance.events.SubstanceCreatedEvent;
// import gsrs.module.substance.events.SubstanceUpdatedEvent;
// import gsrs.module.substance.repository.SubstanceRepository;
import gsrs.module.substance.SubstanceEntityService;
import gsrs.repository.GroupRepository;
import gsrs.service.AbstractGsrsEntityService;
import gsrs.validator.ValidatorConfig;
import ix.core.validator.GinasProcessingMessage;
import ix.core.validator.ValidationResponse;
import ix.core.validator.ValidationResponseBuilder;
import ix.core.validator.ValidatorCallback;
// import ix.ginas.models.v1.Substance;
// import ix.ginas.utils.GinasProcessingStrategy;
// import ix.ginas.utils.JsonSubstanceFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ix.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductElistEntityService extends AbstractGsrsEntityService<ProductElist, String> {
    public static final String CONTEXT = "productelist";

    public ProductElistEntityService() {
        super(CONTEXT, IdHelpers.STRING_NO_WHITESPACE, null, null, null);
    }

    @Autowired
    private ProductElistRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Class<ProductElist> getEntityClass() {
        return ProductElist.class;
    }

    @Override
    public String parseIdFromString(String idAsString) {
        return idAsString;
    }

    @Override
    protected ProductElist fromNewJson(JsonNode json) throws IOException {
        return objectMapper.convertValue(json, ProductElist.class);
    }

    @Override
    public Page page(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    protected ProductElist create(ProductElist product) {
        try {
            return repository.saveAndFlush(product);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    @Override
    @Transactional
    protected ProductElist update(ProductElist product) {
        return repository.saveAndFlush(product);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    protected AbstractEntityUpdatedEvent<ProductElist> newUpdateEvent(ProductElist updatedEntity) {
        return null;
    }

    @Override
    protected AbstractEntityCreatedEvent<ProductElist> newCreationEvent(ProductElist createdEntity) {
        return null;
    }

    @Override
    public String getIdFrom(ProductElist entity) {
        return entity.productId;
    }

    @Override
    protected List<ProductElist> fromNewJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected ProductElist fromUpdatedJson(JsonNode json) throws IOException {
        //TODO should we make any edits to remove fields?
        return objectMapper.convertValue(json, ProductElist.class);
    }

    @Override
    protected List<ProductElist> fromUpdatedJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected JsonNode toJson(ProductElist product) throws IOException {
        return objectMapper.valueToTree(product);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Optional<ProductElist> get(String id) {
        // Elist table has multiple records for same Product Id. Combing multiple columns/rows into one row.
        // returning only one record here.
        return productCombineMultipleRows(id);
      //  List<ProductElist> prodList = repository.findByProductId(id);
    }

    @Override
    public Optional<ProductElist> flexLookup(String someKindOfId) {
        if (someKindOfId == null) {
            return Optional.empty();
        }
        return productCombineMultipleRows(someKindOfId);
       // return repository.findById(someKindOfId);
    }

    @Override
    protected Optional<String> flexLookupIdOnly(String someKindOfId) {
        //easiest way to avoid deduping data is to just do a full flex lookup and then return id
        Optional<ProductElist> found = flexLookup(someKindOfId);
        if (found.isPresent()) {
            return Optional.of(found.get().productId);
        }
        return Optional.empty();
    }

    public Optional<ProductElist> productCombineMultipleRows(String id) {
        ProductElist prodFirst = null;
        StringBuilder status = new StringBuilder();

        List<ProductElist> productList = repository.findByProductId(id);

        if (productList.size() > 0) {
            for (int i = 0; i < productList.size(); i++) {
                ProductElist prod = productList.get(i);
                if (status.length() != 0) {
                    status.append("|");
                }
                status.append((prod.marketingStatus != null) ? prod.marketingStatus : "");
            }
            // Store all the combined data of each row in the first row.ProductElist prod = productList.get(i);
            prodFirst = productList.get(0);
            prodFirst.marketingStatus = status.toString();
            productList.set(0, prodFirst);
        }
        return Optional.ofNullable(prodFirst);
    }

}
