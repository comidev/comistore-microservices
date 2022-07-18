package comidev.components.invoice_item;

import org.springframework.stereotype.Service;

import comidev.components.invoice_item.dto.InvoiceItemReq;
import comidev.components.product.Product;
import comidev.components.product.ProductRepo;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvoiceItemService {
    private final InvoiceItemRepo invoiceItemRepo;
    private final ProductRepo productRepo;

    public InvoiceItem saveInvoiceItem(InvoiceItemReq item) {
        InvoiceItem invoiceItemNew = new InvoiceItem(item);
        Long productID = item.getProductId();

        // ? Se comunica con product-service: actualiza STOCK
        Product productDB = productRepo.updateStock(productID, (item.getQuantity()) * (-1));
        invoiceItemNew.setProductID(productID);

        InvoiceItem itemDB = invoiceItemRepo.save(invoiceItemNew);
        itemDB.setProduct(productDB);
        return itemDB;
    }
}
