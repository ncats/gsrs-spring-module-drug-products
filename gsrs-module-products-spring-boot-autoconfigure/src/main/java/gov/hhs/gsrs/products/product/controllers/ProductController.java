package gov.hhs.gsrs.products.product.controllers;

import gov.hhs.gsrs.products.ProductDataSourceConfig;
import gov.hhs.gsrs.products.product.models.*;
import gov.hhs.gsrs.products.product.services.*;
import gov.hhs.gsrs.products.product.searcher.LegacyProductSearcher;
import gov.hhs.gsrs.products.product.services.SubstanceApiService;

import gov.nih.ncats.common.util.Unchecked;
import gsrs.autoconfigure.GsrsExportConfiguration;
import gsrs.controller.*;
import gsrs.controller.hateoas.HttpRequestHolder;
import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.ETagRepository;
import gsrs.repository.EditRepository;
import gsrs.service.EtagExportGenerator;
import gsrs.service.ExportService;
import gsrs.service.GsrsEntityService;
import gsrs.controller.hateoas.HttpRequestHolder;
import ix.core.models.Principal;
import ix.core.models.ETag;
import ix.core.search.SearchResult;
import ix.ginas.exporters.ExportMetaData;
import ix.ginas.exporters.ExportProcess;
import ix.ginas.exporters.Exporter;
import ix.ginas.exporters.ExporterFactory;
import ix.ginas.models.v1.Substance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

@ExposesResourceFor(Product.class)
@GsrsRestApiController(context = ProductEntityService.CONTEXT, idHelper = IdHelpers.NUMBER)
public class ProductController extends EtagLegacySearchEntityController<ProductController, Product, Long> {

    //@Autowired
    //private SubstanceApiService substanceApiService;

    @Autowired
    private ETagRepository eTagRepository;

    @PersistenceContext(unitName = ProductDataSourceConfig.NAME_ENTITY_MANAGER)
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
    private ProductEntityService productEntityService;

    @Autowired
    private LegacyProductSearcher legacyProductSearcher;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public GsrsEntityService<Product, Long> getEntityService() {
        return productEntityService;
    }

    @Override
    protected LegacyGsrsSearchService<Product> getlegacyGsrsSearchService() {
        return legacyProductSearcher;
    }

    @Override
    protected Stream<Product> filterStream(Stream<Product> stream, boolean publicOnly, Map<String, String> parameters) {
        return stream;
    }

    @GetGsrsRestApiMapping("/distinctprovenance/{substanceId}")
    public ResponseEntity<String> findProvenanceBySubstance(@PathVariable("substanceId") String substanceId) throws Exception {
        List<String> provenanceList = productEntityService.findProvenanceBySubstance(substanceId);
        if (substanceId == null) {
            throw new IllegalArgumentException("There is no Substance Key provided in function findProvenanceBySubstanceUuid()");
        }
        return new ResponseEntity(provenanceList, HttpStatus.OK);
    }
}
