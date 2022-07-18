package comidev.components.invoice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import comidev.components.customer.CustomerRepo;
import comidev.components.invoice.dto.InvoiceReq;
import comidev.components.invoice.dto.InvoiceRes;
import comidev.components.invoice_item.InvoiceItem;
import comidev.components.invoice_item.InvoiceItemService;
import comidev.components.invoice_item.dto.InvoiceItemReq;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoiceService {
    private final InvoiceRepo invoiceRepo;
    private final InvoiceItemService invoiceItemService;
    private final CustomerRepo customerRepo;

    public List<InvoiceRes> findAll() {
        return invoiceRepo.findAll().stream().map(InvoiceRes::new).toList();
    }

    public List<InvoiceRes> findByCustomerId(Long id) {
        // ? Se comunica con customer-service: verifica si el Cliente existe
        customerRepo.validateCustomerID(id);

        List<Invoice> invoicesDB = invoiceRepo.findByCustomerID(id);

        return invoicesDB.stream().map(InvoiceRes::new).toList();
    }

    public void save(InvoiceReq invoiceReq) {
        Invoice invoiceNew = new Invoice(invoiceReq);

        Long customerID = invoiceReq.getCustomerId();
        // ? Se comunica con customer-service: verifica si el Cliente existe
        customerRepo.validateCustomerID(customerID);

        Float total = 0f;
        List<InvoiceItem> items = new ArrayList<>();

        for (InvoiceItemReq item : invoiceReq.getItems()) {
            InvoiceItem itemDB = invoiceItemService.saveInvoiceItem(item);
            total += itemDB.getQuantity() * itemDB.getProduct().getPrice();
            items.add(itemDB);
        }

        invoiceNew.setCustomerID(customerID);
        invoiceNew.setInvoiceItems(items);
        invoiceNew.setTotal(total);

        invoiceRepo.save(invoiceNew);
    }
}
