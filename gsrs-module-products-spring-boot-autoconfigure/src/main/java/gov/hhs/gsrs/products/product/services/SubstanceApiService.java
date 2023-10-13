package gov.hhs.gsrs.products.product.services;

import ix.ginas.models.v1.Substance;
import gsrs.api.substances.SubstanceRestApi;
import gsrs.module.substance.services.EntityManagerSubstanceKeyResolver;
import gsrs.substances.dto.SubstanceDTO;
import gsrs.substances.dto.NameDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SubstanceApiService {

    @Autowired
    private SubstanceRestApi substanceRestApi;

    @Autowired
    public EntityManagerSubstanceKeyResolver entityManagerSubstanceKeyResolver;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    final RestTemplate restTemplate = new RestTemplate();

    @Value("${substanceAPI.BaseUrl}")
    private String baseUrl;

    @Value("${substance.linking.keyType.productKeyType}")
    private String substanceKeyTypeConfig;

    // Entity Manager, Substance Key Resolver
    public Optional<Substance> getEntityManagerSubstanceBySubstanceKeyResolver(String substanceKey, String substanceKeyType) {

        if ((substanceKey == null) && (substanceKeyType == null)) {
            return null;
        }

        ResponseEntity<String> response = null;
        Optional<Substance> substance = null;

        try {
            // Get ENTITY MANAGER Substance Key resolver by Substance Key and substanceKeyType (UUID, APPROVAL_ID, BDNUM)
            substance = entityManagerSubstanceKeyResolver.resolveEMSubstance(substanceKey, substanceKeyType);

           // JsonNode root = null;
           // root = objectMapper.readTree(substance);
          //  System.out.println("AAAAAAAAAAAAAAAAAAAAAA " + root.toPrettyString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (substance.get().uuid != null) {
            return substance;
        } else {
            log.debug("The Substance is not null, but could not retrieve substance uuid");
        }

        return null;
    }

    // Substance API, Substance Key Resolver
    public Optional<SubstanceDTO> getSubstanceBySubstanceKeyResolver(String substanceKey, String substanceKeyType) {

        if ((substanceKey == null) && (substanceKeyType == null)) {
            return null;
        }

        ResponseEntity<String> response = null;
        Optional<SubstanceDTO> substanceDTO = null;

        try {
            // Substance API resolver by Substance Key and substanceKeyType (UUID, APPROVAL_ID, BDNUM)
            substanceDTO = substanceRestApi.resolveSubstance(substanceKey, substanceKeyType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (substanceDTO == null || !substanceDTO.isPresent()) {
            return null;
        }

        if (substanceDTO.get().getUuid() != null) {
            return substanceDTO;
        } else {
            log.debug("The SubstanceDTO is not null, but could not retrieve substance uuid");
        }

        return null;
    }

    public Optional<List<NameDTO>> getNamesOfSubstance(String anyKindOfSubstanceId) {
        if (anyKindOfSubstanceId == null) {
            return null;
        }

        ResponseEntity<String> response = null;
        Optional<List<NameDTO>> nameDTO = null;

        try {
            return substanceRestApi.getNamesOfSubstance(anyKindOfSubstanceId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (nameDTO == null || !nameDTO.isPresent()) {
            return null;
        }

        if (nameDTO.get().size() > 0) {
            return nameDTO;
        } else {
            log.debug("The nameDTO is not null, but could not retrieve data from getNamesOfSubstance(anyKindOfSubstanceId)");
        }

        return null;
    }

    public ResponseEntity<String> getSubstanceDetailsFromUUID(String uuid) {
        String urlTemplate1 = baseUrl + "api/v1/substances(%s)";
        Boolean exists;

        if (uuid == null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{'errors': 'Substance UUID is required.'}");
        }

        ResponseEntity<String> response = null;

        try {
            response = restTemplate.getForEntity(String.format(urlTemplate1, uuid), String.class);
            return response;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{'errors': 'There were errors.'}");
    }

    public ResponseEntity<String> getSubstanceDetailsFromSubstanceKey(String substanceKey) {
        String urlTemplate1 = baseUrl + "api/v1/substances(%s)";

        if (substanceKey == null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{'errors': 'Substance Code is required.'}");
        }

        ResponseEntity<String> response = null;

        try {
            response = restTemplate.getForEntity(String.format(urlTemplate1, substanceKey), String.class);
            return response;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        if (response == null) return null;

        HttpStatus statusCode = response.getStatusCode();
        if (statusCode.equals(HttpStatus.valueOf(404))) {
            return null;
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{'errors': 'There were errors.'}");
    }
}
