package comidev.services;

import org.springframework.stereotype.Service;

import java.util.List;

import comidev.components.invoice.Invoice;
import comidev.components.invoice.InvoiceRepo;
import comidev.components.invoice_item.InvoiceItem;
import comidev.components.invoice_item.InvoiceItemRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@Getter
@AllArgsConstructor
public class AppFabric {
    private final InvoiceItemRepo invoiceItemRepo;
    private final InvoiceRepo invoiceRepo;

    private Long id() {
        return (long) (Math.random() * 10_000);
    }

    public InvoiceItem createInvoiceItem(Long productID) {
        Long productDB = productID != null ? productID : id();
        return invoiceItemRepo.save(new InvoiceItem(3, productDB));
    }

    public Invoice createInvoice(Long customerID, InvoiceItem invoiceItem) {
        Long customerDB = customerID != null
                ? customerID
                : id();

        List<InvoiceItem> invoiceItemsDB = List.of(invoiceItem != null
                ? invoiceItem
                : createInvoiceItem(null));

        return invoiceRepo.save(new Invoice("x", 10f,
                customerDB, invoiceItemsDB));
    }
}
