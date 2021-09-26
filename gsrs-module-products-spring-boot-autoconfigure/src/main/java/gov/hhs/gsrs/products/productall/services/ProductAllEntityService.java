package gov.hhs.gsrs.products.productall.services;

import gov.hhs.gsrs.products.productall.models.*;
import gov.hhs.gsrs.products.productall.repositories.*;

import gsrs.controller.IdHelpers;
import gsrs.events.AbstractEntityCreatedEvent;
import gsrs.events.AbstractEntityUpdatedEvent;
import gsrs.repository.GroupRepository;
import gsrs.service.AbstractGsrsEntityService;

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
public class ProductAllEntityService extends AbstractGsrsEntityService<ProductMainAll, String> {
    public static final String  CONTEXT = "productsall";

    public ProductAllEntityService() {
        super(CONTEXT,  IdHelpers.STRING_NO_WHITESPACE, null, null, null);
    }

    @Autowired
    private ProductAllRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Class<ProductMainAll> getEntityClass() {
        return ProductMainAll.class;
    }

    @Override
    public String parseIdFromString(String idAsString) {
        return idAsString;
    }

    @Override
    protected ProductMainAll fromNewJson(JsonNode json) throws IOException {
        return objectMapper.convertValue(json, ProductMainAll.class);
    }

    @Override
    public Page page(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    protected ProductMainAll create(ProductMainAll product) {
        try {
            return repository.saveAndFlush(product);
        }catch(Throwable t){
            t.printStackTrace();
            throw t;
        }
    }

    @Override
    @Transactional
    protected ProductMainAll update(ProductMainAll application) {
        return repository.saveAndFlush(application);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    protected AbstractEntityUpdatedEvent<ProductMainAll> newUpdateEvent(ProductMainAll updatedEntity) {
        return null;
    }

    @Override
    protected AbstractEntityCreatedEvent<ProductMainAll> newCreationEvent(ProductMainAll createdEntity) {
        return null;
    }

    @Override
    public String getIdFrom(ProductMainAll entity) {
        return entity.productId;
    }

    @Override
    protected List<ProductMainAll> fromNewJsonList(JsonNode list) throws IOException {
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
    protected ProductMainAll fromUpdatedJson(JsonNode json) throws IOException {
        //TODO should we make any edits to remove fields?
        return objectMapper.convertValue(json, ProductMainAll.class);
    }

    @Override
    protected List<ProductMainAll> fromUpdatedJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected JsonNode toJson(ProductMainAll product) throws IOException {
        return objectMapper.valueToTree(product);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Optional<ProductMainAll> get(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ProductMainAll> flexLookup(String someKindOfId) {
        if (someKindOfId == null){
            return Optional.empty();
        }
        return repository.findById(someKindOfId);
    }

    @Override
    protected Optional<String> flexLookupIdOnly(String someKindOfId) {
        //easiest way to avoid deduping data is to just do a full flex lookup and then return id
        Optional<ProductMainAll> found = flexLookup(someKindOfId);
        if(found.isPresent()){
            return Optional.of(found.get().productId);
        }
        return Optional.empty();
    }

    public List<String> findProvenanceBySubstanceUuid(String substanceUuid) {
        List<String> provenanceList = repository.findProvenanceBySubstanceUuid(substanceUuid);
        return provenanceList;
    }
}
