package gov.nih.ncats.product.product.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import gov.nih.ncats.product.product.models.*;
import gov.nih.ncats.product.product.services.*;
import gov.nih.ncats.product.product.searcher.LegacyProductSearcher;

import gov.nih.ncats.common.util.Unchecked;
import gsrs.autoconfigure.GsrsExportConfiguration;
import gsrs.controller.*;
import gsrs.legacy.LegacyGsrsSearchService;
import gsrs.repository.ETagRepository;
import gsrs.repository.EditRepository;
import gsrs.service.EtagExportGenerator;
import gsrs.service.ExportService;
import gsrs.service.GsrsEntityService;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Autowired
    private ETagRepository eTagRepository;
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

    public ResponseEntity<Object> createExport(@PathVariable("etagId") String etagId, @PathVariable("format") String format, @RequestParam(value = "publicOnly", required = false) Boolean publicOnlyObj, @RequestParam(value = "filename", required = false) String fileName, Principal prof, @RequestParam Map<String, String> parameters) throws Exception {
        Optional<ETag> etagObj = this.eTagRepository.findByEtag(etagId);
        boolean publicOnly = publicOnlyObj == null ? true : publicOnlyObj;
        if (!etagObj.isPresent()) {
            return new ResponseEntity("could not find etag with Id " + etagId, this.gsrsControllerConfiguration.getHttpStatusFor(HttpStatus.BAD_REQUEST, parameters));
        } else {
            ExportMetaData emd = new ExportMetaData(etagId, ((ETag) etagObj.get()).uri, prof.username, publicOnly, format);
            Stream<Product> mstream = (Stream)(new EtagExportGenerator(this.entityManager, this.transactionManager)).generateExportFrom(this.getEntityService().getContext(), (ETag)etagObj.get()).get();
            Stream<Product> effectivelyFinalStream = this.filterStream(mstream, publicOnly, parameters);

            if (fileName != null) {
                emd.setDisplayFilename(fileName);
            }

            ExportProcess<Product> p = this.exportService.createExport(emd, () -> {
                return effectivelyFinalStream;
            });
            p.run(this.taskExecutor, (out) -> {
                return (Exporter) Unchecked.uncheck(() -> {
                    return this.getExporterFor(format, out, publicOnly, parameters);
                });
            });
            return new ResponseEntity(p.getMetaData(), HttpStatus.OK);
        }
    }

    private Exporter<Product> getExporterFor(String extension, OutputStream pos, boolean publicOnly, Map<String, String> parameters) throws IOException {
        ExporterFactory.Parameters params = this.createParamters(extension, publicOnly, parameters);
        ExporterFactory<Product> factory = this.gsrsExportConfiguration.getExporterFor(this.getEntityService().getContext(), params);
        if (factory == null) {
            throw new IllegalArgumentException("could not find suitable factory for " + params);
        } else {
            return factory.createNewExporter(pos, params);
        }
    }

    public Optional<Product> injectSubstanceDetails(Optional<Product> product) {

        try {
            if (product.isPresent()) {

                /*
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

                 */
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return product;
    }

}
