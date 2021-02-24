package gov.nih.ncats.product;

import gsrs.controller.*;
import gov.nih.ncats.product.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

@GsrsRestApiController(context = "product", idHelper = IdHelpers.NUMBER)
public class ProductController extends EtagLegacySearchEntityController<Product, Long> {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private LegacyProductSearcher legacyProductSearcher;
    private Object Product;

    public ProductController() {
        super("product", IdHelpers.NUMBER);
    }

    @Override
    protected Product fromNewJson(JsonNode json) throws IOException {
        //remove id?
        return objectMapper.convertValue(json, Product.class);
    }

    @Override
    protected List<Product> fromNewJsonList(JsonNode list) throws IOException {
        return null;
    }

    @Override
    protected Product fromUpdatedJson(JsonNode json) throws IOException {
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
    protected Product create(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @Override
    protected long count() {
        return productRepository.count();
    }

    @Override
    protected Optional<Product> get(Long id) {
        return productRepository.findById(id);
    }

    @Override
    protected Long parseIdFromString(String idAsString) {
        return Long.parseLong(idAsString);
    }

    @Override
    protected Optional<Product> flexLookup(String someKindOfId) {
        return productRepository.findBySource(someKindOfId);
    }

    @Override
    protected Class<Product> getEntityClass() {
        return Product.class;
    }

    @Override
    protected Page page(long offset, long numOfRecords, Sort sort) {
        return productRepository.findAll(new OffsetBasedPageRequest(offset, numOfRecords, sort));
    }

    @Override
    protected void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    protected Long getIdFrom(Product entity) {
        return entity.getId();
    }

    @Override
    protected Product update(Product product) {
       return productRepository.saveAndFlush(product);
    }

    @Override
    protected LegacyProductSearcher getlegacyGsrsSearchService() {
        return legacyProductSearcher;
    }

}
