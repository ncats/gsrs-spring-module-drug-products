package gov.hhs.gsrs.products.product.exporters;

import gov.hhs.gsrs.products.product.controllers.ProductController;
import gov.hhs.gsrs.products.product.services.SubstanceApiService;

import ix.ginas.exporters.*;
import gsrs.springUtils.AutowireHelper;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.util.*;

public class ProductExporterFactory implements ExporterFactory {

    @Autowired
    private SubstanceApiService substanceApiService;

    private static final Set<OutputFormat> FORMATS;

    static {
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

        if (substanceApiService == null) {
            AutowireHelper.getInstance().autowire(this);
        }

        SpreadsheetFormat format = SpreadsheetFormat.XLSX;
        Spreadsheet spreadsheet = format.createSpreadsheet(out);

        ProductExporter.Builder builder = new ProductExporter.Builder(spreadsheet);

        configure(builder, params);

        return builder.build(substanceApiService);
    }

    protected void configure(ProductExporter.Builder builder, Parameters params) {
        builder.includePublicDataOnly(params.publicOnly());
    }

}