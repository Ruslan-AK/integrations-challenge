package com.yaypay.challenge.rest;

import com.yaypay.challenge.dtos.InvoiceDTO;
import com.yaypay.challenge.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvoiceRestController {
    private final DataService<InvoiceDTO> invoiceDataService;

    @Autowired
    public InvoiceRestController(DataService<InvoiceDTO> invoiceDataService) {
        this.invoiceDataService = invoiceDataService;
    }

    @PostMapping("/bulk/invoices")
    public void createOrUpdateInvoices(@RequestBody List<InvoiceDTO> invoices) {
        invoiceDataService.processData(invoices);
    }
}
