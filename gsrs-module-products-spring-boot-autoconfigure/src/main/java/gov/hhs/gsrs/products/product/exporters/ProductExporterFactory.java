package gov.hhs.gsrs.products.product.exporters;

import gov.hhs.gsrs.products.product.controllers.ProductController;

import gsrs.DefaultDataSourceConfig;
import ix.ginas.exporters.*;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.util.*;

public class ProductExporterFactory implements ExporterFactory {

  @PersistenceContext(unitName =  DefaultDataSourceConfig.NAME_ENTITY_MANAGER)
  public EntityManager entityManager;

  //  @Autowire
  //  public EntityManagerSubstanceKeyResolver substanceKeyResolver;

    private static final Set<OutputFormat> FORMATS;

    static{
        Set<OutputFormat> set = new LinkedHashSet<>();
        set.add(SpreadsheetFormat.XLSX);

        FORMATS = Collections.unmodifiableSet(set);
    }

    @Override
    public Set<OutputFormat> getSupportedFormats() {
        return FORMATS;
    }

    @Override
    public boolean supports(Parameters params) {
        return params.getFormat() instanceof SpreadsheetFormat;
    }

    @Override
    public ProductExporter createNewExporter(OutputStream out, Parameters params) throws IOException {

        SpreadsheetFormat format = SpreadsheetFormat.XLSX;
        Spreadsheet spreadsheet = format.createSpreadsheet(out);

        ProductExporter.Builder builder = new ProductExporter.Builder(spreadsheet);

        configure(builder, params);

        return builder.build(entityManager);
    }
    
    protected void configure(ProductExporter.Builder builder, Parameters params){
        builder.includePublicDataOnly(params.publicOnly());
    }

}