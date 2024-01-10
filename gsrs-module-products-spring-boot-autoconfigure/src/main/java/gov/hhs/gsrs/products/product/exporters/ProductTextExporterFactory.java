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

public class ProductTextExporterFactory implements ExporterFactory {

    @Autowired
    private SubstanceApiService substanceApiService;

    OutputFormat format = new OutputFormat("txt", "Tab-delimited (.txt)");

    @Override
    public boolean supports(ExporterFactory.Parameters params) {
        return params.getFormat().equals(format);
    }

    @Override
    public Set<OutputFormat> getSupportedFormats() {
        return Collections.singleton(format);
    }

    @Override
    public ProductTextExporter createNewExporter(OutputStream out, Parameters params) throws IOException {

        if (substanceApiService == null) {
            AutowireHelper.getInstance().autowire(this);
        }
        return new ProductTextExporter(out, params, substanceApiService);
    }

}