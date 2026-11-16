package gov.hhs.gsrs.products.product.controllers;

import gov.hhs.gsrs.products.ProductDataSourceConfig;
import gov.hhs.gsrs.products.product.models.Product;
import gov.hhs.gsrs.products.product.searcher.LegacyProductSearcher;
import gov.hhs.gsrs.products.product.services.ProductEntityService;
import gsrs.GsrsFactoryConfiguration;
import gsrs.autoconfigure.GsrsExportConfiguration;
import gsrs.controller.*;
import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.ETagRepository;
import gsrs.service.ExportService;
import gsrs.service.GsrsEntityService;
import ix.core.search.SearchOptions;
import ix.core.search.text.TextIndexer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PathVariable;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@ExposesResourceFor(Product.class)
@GsrsRestApiController(context = ProductEntityService.CONTEXT, idHelper = IdHelpers.NUMBER)
public class ProductController extends EtagLegacySearchEntityController<ProductController, Product, Long> {

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
    private JsonMapper mapper;

    @Autowired
    private GsrsFactoryConfiguration gsrsFactoryConfiguration;


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

    @Override
    public SearchOptions instrumentSearchOptions(SearchOptions so) {

        so= super.instrumentSearchOptions(so);
        so.addDateRangeFacet("root_lastModifiedDate");
        so.addDateRangeFacet("root_creationDate");

        if (gsrsFactoryConfiguration != null) {
            Optional<Map<String, Object>> conf = gsrsFactoryConfiguration
                    .getSearchSettingsFor(ProductEntityService.CONTEXT);

            String restrict = conf
                    .map(cc -> cc.get("restrictDefaultToIdentifiers"))
                    .filter(bb -> bb != null).map(bb -> bb.toString())
                    .orElse(null);
            if (restrict != null && "true".equalsIgnoreCase(restrict)) {
                so.setDefaultField(TextIndexer.FULL_IDENTIFIER_FIELD);
            }
        }

        return so;
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
