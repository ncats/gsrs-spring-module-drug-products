package gov.hhs.gsrs.products.productall.exporters;

import gov.hhs.gsrs.products.productall.controllers.ProductAllController;

import ix.ginas.exporters.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;;
import java.util.*;

public class ProductTextExporterFactory implements ExporterFactory {

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
        return new ProductTextExporter(out, params);
    }

    /*
    protected void configure(ProductTextExporter.Builder builder, Parameters params){
        builder.includePublicDataOnly(params.publicOnly());
    }
    */
}