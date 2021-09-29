package gov.hhs.gsrs.products.product.services;

import gov.hhs.gsrs.products.product.models.Product;
import gov.hhs.gsrs.products.product.repositories.ProductRepository;

import gsrs.controller.IdHelpers;
import gsrs.events.AbstractEntityCreatedEvent;
import gsrs.events.AbstractEntityUpdatedEvent;
import gsrs.repository.GroupRepository;
import gsrs.service.AbstractGsrsEntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ix.utils.Util;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductEntityService extends AbstractGsrsEntityService<Product, Long> {
    public static final String  CONTEXT = "products";

    public ProductEntityService() {
        super(CONTEXT,  IdHelpers.NUMBER, null, null, null);
    }

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Class<Product> getEntityClass() {
        return Product.class;
    }

    @Override
    public Long parseIdFromString(String idAsString) {
        return Long.parseLong(idAsString);
    }

    @Override
    protected Product fromNewJson(JsonNode json) throws IOException {
        return objectMapper.convertValue(json, Product.class);
    }

    @Override
    public Page page(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    protected Product create(Product application) {
        try {
            return repository.saveAndFlush(application);
        }catch(Throwable t){
            t.printStackTrace();
            throw t;
        }
    }

    @Override
    @Transactional
    protected Product update(Product product) {
        return repository.saveAndFlush(product);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    protected AbstractEntityUpdatedEvent<Product> newUpdateEvent(Product updatedEntity) {
        return null;
    }

    @Override
    protected AbstractEntityCreatedEvent<Product> newCreationEvent(Product createdEntity) {
        return null;
    }

    @Override
    public Long getIdFrom(Product entity) {
        return entity.getId();
    }

    @Override
    protected List<Product> fromNewJsonList(JsonNode list) throws IOException {
        return null;
    }

    /*
    @Override
    protected Application fixUpdatedIfNeeded(Application oldEntity, Application updatedEntity) {
        //force the "owner" on all the updated fields to point to the old version so the uuids are correct
        return updatedEntity;
    }
    */

    @Override
    protected Product fromUpdatedJson(JsonNode json) throws IOException {
        //TODO should we make any edits to remove fields?
        return objectMapper.convertValue(json, Product.class);
    }

    @Override
    protected List<Product> fromUpdatedJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected JsonNode toJson(Product product) throws IOException {
        return objectMapper.valueToTree(product);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Optional<Product> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Product> flexLookup(String someKindOfId) {
        if (someKindOfId == null){
            return Optional.empty();
        }
        return repository.findById(Long.parseLong(someKindOfId));
    }

    @Override
    protected Optional<Long> flexLookupIdOnly(String someKindOfId) {
        //easiest way to avoid deduping data is to just do a full flex lookup and then return id
        Optional<Product> found = flexLookup(someKindOfId);
        if(found.isPresent()){
            return Optional.of(found.get().id);
        }
        return Optional.empty();
    }

}
