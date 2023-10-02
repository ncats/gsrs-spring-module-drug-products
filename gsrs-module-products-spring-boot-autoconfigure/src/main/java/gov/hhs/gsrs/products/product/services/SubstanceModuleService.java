package gov.hhs.gsrs.products.product.services;

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

    public ResponseEntity<String> getSubstanceDetailsFromUUID(String uuid) {
        System.out.println("Inside "+ "getSubstanceDetailsFromUUID " + uuid);
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
