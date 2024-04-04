package gov.hhs.gsrs.products.product.services;

import ix.ginas.models.v1.Substance;
import gsrs.api.substances.SubstanceRestApi;
import gsrs.module.substance.services.EntityManagerSubstanceKeyResolver;
import gsrs.substances.dto.SubstanceDTO;
import gsrs.substances.dto.NameDTO;
import gsrs.substances.dto.CodeDTO;

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

    public static final String SUBSTANCE_KEY_TYPE_UUID = "UUID";
    public static final String SUBSTANCE_KEY_TYPE_APPROVAL_ID = "APPROVAL_ID";
    public static final String SUBSTANCE_KEY_TYPE_BDNUM = "BDNUM";

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

    @Value("${substance.linking.keyType.productKeyType:UUID}")
    private String substanceKeyTypeFromConfig;


    public String getSubstanceKeyTypeFromConfig() {
        return this.substanceKeyTypeFromConfig;
    }

    private String substanceKey;

    public String getSubstanceKey() {
        return this.substanceKey;
    }

    private String substanceKeyType;

    public String getSubstanceKeyType() {
        return this.substanceKeyType;
    }

    // Class to store the Substance Key and Substance Key Type
    public class SubstanceKeyPair {
        public String substanceKey;
        public String substanceKeyType;
    }

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

    // Substance API, get the Names for the Substance
    public Optional<List<NameDTO>> getNamesOfSubstance(String anyKindOfSubstanceId) {
        if (anyKindOfSubstanceId == null) {
            return null;
        }

        ResponseEntity<String> response = null;
        Optional<List<NameDTO>> names = null;

        try {
            return substanceRestApi.getNamesOfSubstance(anyKindOfSubstanceId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (names == null || !names.isPresent()) {
            return null;
        }

        if (names.get().size() > 0) {
            return names;
        } else {
            log.debug("The names is not null, but could not retrieve data from names");
        }

        return null;
    }

    // Substance API, get the Names for the Substance
    public Optional<List<CodeDTO>> getCodesOfSubstance(String anyKindOfSubstanceId) {
        if (anyKindOfSubstanceId == null) {
            return null;
        }

        ResponseEntity<String> response = null;
        Optional<List<CodeDTO>> codes = null;

        try {
            return substanceRestApi.getCodesOfSubstance(anyKindOfSubstanceId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (codes == null || !codes.isPresent()) {
            return null;
        }

        if (codes.get().size() > 0) {
            return codes;
        } else {
            log.debug("The codes is not null, but could not retrieve data from codes");
        }

        return null;
    }

    // Convert Substance Key and Substance Key Type by Substance Key Resolver
    public SubstanceKeyPair convertSubstanceKeyBySubstanceKeyResolver(String substanceKey, String substanceKeyType) {

        if ((substanceKey == null) && (substanceKeyType == null)) {
            return null;
        }

        SubstanceKeyPair subKey = new SubstanceKeyPair();

        subKey.substanceKeyType = substanceKeyTypeFromConfig;

        // UUID or APPROVAL_ID
        if ((substanceKeyTypeFromConfig.equalsIgnoreCase(SUBSTANCE_KEY_TYPE_UUID)) ||
                (substanceKeyTypeFromConfig.equalsIgnoreCase(SUBSTANCE_KEY_TYPE_APPROVAL_ID))) {

            // Get Substance by Substance Key Resolver
            Optional<SubstanceDTO> substance = getSubstanceBySubstanceKeyResolver(substanceKey, substanceKeyType);

            if (substance.isPresent()) {

                if (substanceKeyTypeFromConfig.equalsIgnoreCase(SUBSTANCE_KEY_TYPE_UUID)) {
                    if (substance.get().getUuid() != null) {
                        subKey.substanceKey = substance.get().getUuid().toString();
                    } else {
                        log.debug("The Substance is not null, but could not retrieve uuid from Substance");
                    }
                } else if (substanceKeyTypeFromConfig.equalsIgnoreCase(SUBSTANCE_KEY_TYPE_APPROVAL_ID)) {
                    if (substance.get().getApprovalID() != null) {
                        subKey.substanceKey = substance.get().getApprovalID();
                    } else {
                        log.debug("The Substance is not null, but could not retrieve Approval ID from Substance");
                    }
                }
            }

        } else if (substanceKeyTypeFromConfig.equalsIgnoreCase(SUBSTANCE_KEY_TYPE_BDNUM)) {
            Optional<List<CodeDTO>> codes = getCodesOfSubstance(substanceKey);
            if (codes.isPresent()) {
                codes.get().forEach(codeObj -> {
                    if ((codeObj.getCodeSystem() != null) && (codeObj.getCodeSystem().equalsIgnoreCase(SUBSTANCE_KEY_TYPE_BDNUM))) {
                        if (codeObj.getCode() != null) {
                            subKey.substanceKey = codeObj.getCode();
                        }
                    }
                });
            }

        } else {
            log.debug("Could not retrieve Substance since no valid Substance Key Type found");
        }

        return subKey;
    }
}
