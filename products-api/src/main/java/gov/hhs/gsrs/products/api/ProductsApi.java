package gov.hhs.gsrs.products.api;

import gsrs.api.AbstractLegacySearchGsrsEntityRestTemplate;

import org.springframework.boot.restclient.RestTemplateBuilder;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.JsonNode;

public class ProductsApi extends AbstractLegacySearchGsrsEntityRestTemplate<ProductDTO, Long> {
    public ProductsApi(RestTemplateBuilder restTemplateBuilder, String baseUrl, JsonMapper mapper) {
        super(restTemplateBuilder, baseUrl, "products", mapper);
    }

    @Override
    protected ProductDTO parseFromJson(JsonNode node) {
        return getMapper().convertValue(node, ProductDTO.class);
    }

    @Override
    protected Long getIdFrom(ProductDTO produtDTO) {
        return produtDTO.getId();
    }

}