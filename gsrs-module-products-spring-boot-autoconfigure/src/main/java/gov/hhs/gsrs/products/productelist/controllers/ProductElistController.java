package gov.hhs.gsrs.products.productelist.controllers;

import gov.hhs.gsrs.products.ProductDataSourceConfig;
import gov.hhs.gsrs.products.productelist.models.*;
import gov.hhs.gsrs.products.productelist.services.*;
import gov.hhs.gsrs.products.productelist.searcher.*;

import gov.nih.ncats.common.util.Unchecked;
import gsrs.autoconfigure.GsrsExportConfiguration;
import gsrs.controller.*;
import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.ETagRepository;
import gsrs.repository.EditRepository;
import gsrs.service.EtagExportGenerator;
import gsrs.service.ExportService;
import gsrs.service.GsrsEntityService;

import ix.core.models.ETag;
import ix.core.search.SearchResult;
import ix.ginas.exporters.*;
import ix.ginas.models.v1.Substance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

@ExposesResourceFor(ProductElist.class)
@GsrsRestApiController(context = ProductElistEntityService.CONTEXT, idHelper = IdHelpers.STRING_NO_WHITESPACE)
public class ProductElistController extends EtagLegacySearchEntityController<ProductElistController, ProductElist, String> {

    @Autowired
    private ETagRepository eTagRepository;

    @PersistenceContext(unitName = ProductDataSourceConfig.NAME_ENTITY_MANAGER)
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private GsrsControllerConfiguration gsrsControllerConfiguration;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private ExportService exportService;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private GsrsExportConfiguration gsrsExportConfiguration;


    @Autowired
    private ProductElistEntityService productEntityService;

    @Autowired
    private LegacyProductElistSearcher legacyProductElistSearcher;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public GsrsEntityService<ProductElist, String> getEntityService() {
        return productEntityService;
    }

    @Override
    protected LegacyGsrsSearchService<ProductElist> getlegacyGsrsSearchService() {
        return legacyProductElistSearcher;
    }

    @Override
    protected Stream<ProductElist> filterStream(Stream<ProductElist> stream, boolean publicOnly, Map<String, String> parameters) {
        return stream;
    }

    public Optional<ProductElist> injectSubstanceDetails(Optional<ProductElist> product) {

        try {
            /*
            if (product.isPresent()) {

                if (product.get().productComponentList.size() > 0) {
                    for (int i = 0; i < product.get().productComponentList.size(); i++) {
                        ProductComponent prodComp = product.get().productComponentList.get(i);
                        if (prodComp != null) {

                            if (prodComp.productLotList.size() > 0) {
                                for (int j = 0; j < prodComp.productLotList.size(); j++) {
                                    ProductLot prodLot = prodComp.productLotList.get(j);
                                    if (prodLot != null) {

                                        if (prodLot.productIngredientList.size() > 0) {
                                            for (int k = 0; k < prodLot.productIngredientList.size(); k++) {
                                                ProductIngredient ingred = prodLot.productIngredientList.get(k);

                                                if (ingred != null) {
                                                    if (ingred.substanceKey != null) {

                                                        // ********* Get Substance Module/Details by Substance Code ***********
                                                        // Using this for local Substance Module:  0017298AA
                                                        // Use this for NCAT FDA URL API:   0126085AB
                                                        ResponseEntity<String> response = this.substanceModuleService.getSubstanceDetailsFromSubstanceKey(ingred.substanceKey);

                                                        String jsonString = response.getBody();
                                                        if (jsonString != null) {
                                                            ObjectMapper mapper = new ObjectMapper();
                                                            JsonNode actualObj = mapper.readTree(jsonString);

                                                            ingred._substanceUuid = actualObj.path("uuid").textValue();
                                                            ingred._approvalID = actualObj.path("approvalID").textValue();
                                                            ingred._name = actualObj.path("_name").textValue();
                                                        }
                                                    }

                                                    if (ingred.basisOfStrengthSubstanceKey != null) {

                                                        ResponseEntity<String> response = this.substanceModuleService.getSubstanceDetailsFromSubstanceKey(ingred.basisOfStrengthSubstanceKey);

                                                        String jsonString = response.getBody();
                                                        if (jsonString != null) {
                                                            ObjectMapper mapper = new ObjectMapper();
                                                            JsonNode actualObj = mapper.readTree(jsonString);

                                                            ingred._basisOfStrengthSubstanceUuid = actualObj.path("uuid").textValue();
                                                            ingred._basisOfStrengthApprovalID = actualObj.path("approvalID").textValue();
                                                            ingred._basisOfStrengthName = actualObj.path("_name").textValue();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

             */
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return product;
    }

}
