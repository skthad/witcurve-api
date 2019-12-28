package com.witcurve.web.rest;

import com.witcurve.service.util.InvoiceUtil;
import com.witcurve.web.rest.vm.InvoiceVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    @Autowired
    InvoiceUtil invoiceUtil;

    @PostMapping("/generatePdf")
    public File generatePdf(@RequestBody InvoiceVM invoiceVM) {
        return invoiceUtil.generateInvoice(invoiceVM);
    }
}
