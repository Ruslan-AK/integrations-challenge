package com.yaypay.challenge.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaypay.challenge.dtos.InvoiceDTO;
import com.yaypay.challenge.dtos.InvoiceLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class InvoiceDataService implements DataService<InvoiceDTO> {
    @Autowired
    private ObjectMapper mapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final int batchSize;
    private final String SOURSE_SYSTEM_NAME = "YayPay";
    private final String SOURSE_SYSTEM_CONTACT = "support@yaypay.com";
    private final String SELECT_UPLOAD_INVOICE_MAX_ID_QUERY =
            "select id from upload_invoice order by id desc limit 1";
    private final String SELECT_UPLOAD_INVOICE_ITEM_MAX_ID_QUERY =
            "select id from upload_invoice_item order by id desc limit 1";
    private final String INSERT_UPLOAD_INVOICE_ITEM_QUERY = "insert into upload_invoice_item " +
            "(amount, custom_fields, description, internal_id, name, quantity, rate, source_system) values " +
            "(:amount, :custom_fields, :description, :internal_id, :name, :quantity, :rate, :source_system)";
    private final String INSERT_UPLOAD_INVOICE_QUERY = "insert into upload_invoice " +
            "(close_date, currency, customer_internal_id, custom_fields, discount, due_date, exchange_rate, " +
                "internal_id, invoice_date, invoice_number, paid, status, tax, terms, total, source_system) values " +
            "(:close_date, :currency, :customer_internal_id, :custom_fields, :discount, :due_date, " +
                ":exchange_rate, :internal_id, :invoice_date, :invoice_number, :paid, :status, :tax, :terms, :total, :source_system)";


    public InvoiceDataService(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              int batchSize) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.batchSize = batchSize;
    }

    @Override
    public void processData(List<InvoiceDTO> payload) {
        long start = System.nanoTime();
        try {
            List<List<InvoiceDTO>> batches = new LinkedList<>();
            int from = 0;
            while (from < payload.size()) {
                int to = payload.size() < from + batchSize ? payload.size() : from + batchSize;
                List<InvoiceDTO> batch = payload.subList(from, to);
                from = to;
                batches.add(batch);
            }
            batches.forEach(b -> processBatchData(b));
        } catch (Exception e) {
            System.out.println(e);
        }
        long finish = System.nanoTime();
        System.out.println("Payload size:" + payload.size());
        System.out.printf("Process took %d second\n", (finish - start)/1000000000);
    }

    private void processBatchData(List<InvoiceDTO> payload) {
        if (payload == null || payload.isEmpty())
            throw new RuntimeException("Payload can't be empty");
        List<Map<String, Object>> batch = new LinkedList<>();
        List<InvoiceLineDTO> nestedBatch = new LinkedList<>();
        for (InvoiceDTO p : payload) {
            if (p == null) {
                continue;
            }
            Map<String, Object> paramMap = new LinkedHashMap<>();
            paramMap.put("close_date", p.getCloseDate());
            paramMap.put("currency", p.getCurrency());
            paramMap.put("customer_internal_id", p.getCustomerId());
            try {
                paramMap.put("custom_fields", mapper.writeValueAsString(p.getCustomFields()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            paramMap.put("discount", p.getDiscount());
            paramMap.put("due_date", p.getDueDate());
            paramMap.put("exchange_rate", p.getExchangeRate());
            paramMap.put("internal_id", p.getInternalId());
            paramMap.put("invoice_date", p.getInvoiceDate());
            paramMap.put("invoice_number", p.getInvoiceNumber());
            paramMap.put("paid", p.getPaid());
            paramMap.put("status", p.getStatus());
            paramMap.put("tax", p.getTax());
            paramMap.put("terms", p.getTerms());
            paramMap.put("total", p.getTotal());
            paramMap.put("source_system", SOURSE_SYSTEM_NAME);
            batch.add(paramMap);
            nestedBatch.add(p.getInvoiceLines() == null ? null : p.getInvoiceLines().get(0));
        }
        namedParameterJdbcTemplate.batchUpdate(INSERT_UPLOAD_INVOICE_QUERY, batch.toArray(new Map[batch.size()]));
        System.out.println("Batch inserted, size : " + batch.size());
        processNestedData(nestedBatch);
    };

    private void processNestedData(List<InvoiceLineDTO> invoiceLines) {
        if (invoiceLines == null || invoiceLines.isEmpty())
            throw new RuntimeException("Invoice lines can't be empty");
        List<Map<String, Object>> batch = new LinkedList<>();
        for (InvoiceLineDTO p : invoiceLines) {
            if (p == null) {
                continue;
            }
            Map<String, Object> nParamMap = new LinkedHashMap<>();
            nParamMap.put("amount", p.getAmount());
            try {
                nParamMap.put("custom_fields", mapper.writeValueAsString(p.getCustomFields()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            nParamMap.put("description", p.getDescription());
            nParamMap.put("internal_id", p.getInvoiceId());
            nParamMap.put("name", p.getName());
            nParamMap.put("quantity", p.getQuantity());
            nParamMap.put("rate", p.getRate());
            nParamMap.put("source_system", SOURSE_SYSTEM_CONTACT);
            batch.add(nParamMap);
        }
        namedParameterJdbcTemplate.batchUpdate(INSERT_UPLOAD_INVOICE_ITEM_QUERY,batch.toArray(new Map[batch.size()]));
        System.out.println("Nested batch inserted, size : " + batch.size());
    }
}
