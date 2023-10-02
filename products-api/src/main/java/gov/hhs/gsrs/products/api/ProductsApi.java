package gov.hhs.gsrs.products.api;

import gsrs.api.AbstractLegacySearchGsrsEntityRestTemplate;
import gsrs.api.GsrsEntityRestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.web.client.RestTemplateBuilder;

public class ProductsApi extends AbstractLegacySearchGsrsEntityRestTemplate<ProductDTO, Long> {
    public ProductsApi(RestTemplateBuilder restTemplateBuilder, String baseUrl, ObjectMapper mapper) {
        super(restTemplateBuilder, baseUrl, "products", mapper);
    }

    @Override
    protected ProductDTO parseFromJson(JsonNode node) {
        return getObjectMapper().convertValue(node, ProductDTO.class);
    }

    @Override
    protected Long getIdFrom(ProductDTO produtDTO) {
        return produtDTO.getId();
    }

}