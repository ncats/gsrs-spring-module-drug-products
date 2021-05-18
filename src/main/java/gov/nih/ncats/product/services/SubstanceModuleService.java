package gov.nih.ncats.product.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class SubstanceModuleService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    final RestTemplate restTemplate = new RestTemplate();

    @Value("${substanceAPI.BaseUrl}")
    private String baseUrl;
    // for testing override see:
    // https://www.baeldung.com/spring-tests-override-properties

    public Boolean substanceExists(String uuid) {
        // is there a way to make this final and use property?
        String urlTemplate1 = baseUrl +  "api/v1/substances(%s)";
        Boolean exists;
        if (uuid == null) return null;
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.getForEntity(String.format(urlTemplate1, uuid), String.class);
        } catch(HttpClientErrorException e) {
            // this is wierd. 404 will generate exception.
            e.printStackTrace();
            return null;
        }
        if(response == null) return null;

        HttpStatus statusCode = response.getStatusCode();
        if (statusCode == null)  return null;

        if (statusCode.equals(HttpStatus.valueOf(404))) {
            return false;
        }
        if (statusCode.equals(HttpStatus.OK)) {
            JsonNode root = null;
            try {
                root = objectMapper.readTree(response.getBody());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
            // Should not be necessary, but possible to get 200 and valid json
            // on a redirect.
            JsonNode name = root.path("uuid");
            if (name != null &&  name.asText().equals(uuid)) {
                return true;
            }
        }
        return null;
    }

    // using this temporarily to get around auth/CORS issues.
    // actually not needed; at least GSRS public version is not giving me CORS problem

    public ResponseEntity<String> getSubstanceDetailsFromUUID(String uuid) {
        System.out.println("Inside "+ "getSubstanceDetailsFromUUID " + uuid);
        // is there a way to make this final and use property?
        String urlTemplate1 = baseUrl +  "api/v1/substances(%s)";
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
        } catch(HttpClientErrorException e) {
            // this is weird. 404 will generate exception.
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{'errors': 'There were errors.'}");
    }

    // using this temporarily to get around auth/CORS issues.
    // actually not needed; at least GSRS public version is not giving me CORS problem

    public ResponseEntity<String> getSubstanceDetailsFromName(String name) {
        String urlTemplate1 = baseUrl +  "api/v1/substances/search?q=root_names_name:\"^%s$\"&fdim=1";
        Boolean exists;
        if (name == null) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{'errors': 'Name is required.'}");
        }
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.getForEntity(String.format(urlTemplate1, encodeParameterValue(name)), String.class);
            return response;
        } catch(HttpClientErrorException e) {
            // this is weird. 404 will generate exception.
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{'errors': 'There were errors.'}");
    }

    public ResponseEntity<String> getSubstanceDetailsFromSubstanceKey(String substanceKey) {
        String urlTemplate1 = baseUrl +  "api/v1/substances(%s)";
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
        } catch(HttpClientErrorException e) {
            // this is weird. 404 will generate exception.
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

    private String encodeParameterValue(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}

/*
import gov.nih.ncats.application.models.Application;
import gov.nih.ncats.application.repositories.ApplicationRepository;

import gsrs.module.substance.autoconfigure.GsrsSubstanceModuleAutoConfiguration;
import gsrs.module.substance.SubstanceEntityService;
import gsrs.service.ExportService;
import ix.ginas.models.v1.Substance;
import gsrs.module.substance.SubstanceEntityServiceImpl;

import gsrs.controller.IdHelpers;
import gsrs.events.AbstractEntityCreatedEvent;
import gsrs.events.AbstractEntityUpdatedEvent;

// import gsrs.module.substance.events.SubstanceCreatedEvent;
// import gsrs.module.substance.events.SubstanceUpdatedEvent;
// import gsrs.module.substance.repository.SubstanceRepository;
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

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
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

// import springfox.documentation.spring.web.json.Json;
import ix.utils.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



@Service
public class SubstanceModuleService {

    @Autowired
    protected SubstanceEntityService substanceEntityService;

    final RestTemplate restTemplate = new RestTemplate();

    @Value("${substanceApi.url }")
    private String substanceApiUrl;

    public Optional<Substance> getSubstanceDetails(String someKindOfId) {
        final String substanceApiUrl = "http://fdadev.ncats.io:9000/ginas/app/api/v1/substances(94cc1092-e454-4343-aeb3-1c9d6aba6877";

      //  Optional<Substance> substance = this.substanceEntityService.flexLookup(someKindOfId);

        return substance;
    }

    public SubstanceEntityService getSubstanceEntityService() {
        return substanceEntityService;
    }
}
*/
