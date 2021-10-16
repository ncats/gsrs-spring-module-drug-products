package gov.hhs.gsrs.products.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gsrs.api.AbstractLegacySearchGsrsEntityRestTemplate;
import gsrs.api.GsrsEntityRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class ProductsApi extends AbstractLegacySearchGsrsEntityRestTemplate<ProductMainAllDTO, String> {
    public ProductsApi(RestTemplateBuilder restTemplateBuilder, String baseUrl, ObjectMapper mapper) {
        super(restTemplateBuilder, baseUrl, "productsall", mapper);
    }

    @Override
    protected ProductMainAllDTO parseFromJson(JsonNode node) {
        return getObjectMapper().convertValue(node, ProductMainAllDTO.class);
    }

    @Override
    protected String getIdFrom(ProductMainAllDTO dto) {
        return dto.getProductId();
    }

}