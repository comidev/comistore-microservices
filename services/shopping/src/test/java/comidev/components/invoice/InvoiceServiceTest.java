package comidev.components.invoice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import comidev.components.customer.CustomerService;
import comidev.components.invoice.dto.InvoiceReq;
import comidev.components.invoice.dto.InvoiceRes;
import comidev.components.invoice_item.InvoiceItem;
import comidev.components.invoice_item.InvoiceItemService;
import comidev.components.invoice_item.dto.InvoiceItemReq;
import comidev.components.product.Product;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {
    private InvoiceService invoiceService;
    @Mock
    private InvoiceRepo invoiceRepo;
    @Mock
    private CustomerService customerService;
    @Mock
    private InvoiceItemService invoiceItemService;

    @BeforeEach
    void setUp() {
        this.invoiceService = new InvoiceService(invoiceRepo, customerService,
                invoiceItemService);
    }

    @Test
    void testFindAll_PuedeDevolverLasCompras() {
        // Arreglar
        Long productID = 1l;
        InvoiceItem invoiceItem = new InvoiceItem(1, productID);
        Invoice invoice = new Invoice();
        invoice.setInvoiceItems(List.of(invoiceItem));

        when(invoiceRepo.findAll()).thenReturn(List.of(invoice));

        // Actuar
        List<InvoiceRes> invoicesRes = invoiceService.findAll();

        // Afirmar
        assertEquals(invoicesRes.get(0).getDescription(), invoice.getDescription());
        verify(invoiceRepo).findAll();
    }

    @Test
    void testFindByCustomerId_PuedeDevolverLasComprasDeUnCliente() {
        // Arreglar
        Long id = 1l;
        Long productID = 1l;
        InvoiceItem invoiceItem = new InvoiceItem(1, productID);
        Invoice invoice = new Invoice();
        invoice.setInvoiceItems(List.of(invoiceItem));

        when(invoiceRepo.findByCustomerID(id)).thenReturn(List.of(invoice));

        // Actuar
        List<InvoiceRes> invoicesRes = invoiceService.findByCustomerId(id);

        // Afirmar
        assertEquals(invoicesRes.get(0).getDescription(), invoice.getDescription());
        verify(invoiceRepo).findByCustomerID(id);
    }

    @Test
    void testSave_PuedeGuardarUnaCompraDeUnCliente() {
        // Arreglar

        Long id = 1l;
        Integer quantity = 1;

        Product product = new Product();
        Long productID = 1l;
        product.setStock(1);
        product.setPrice(1f);

        InvoiceItemReq invoiceItemReq = new InvoiceItemReq(quantity, id);

        InvoiceReq invoiceReq = new InvoiceReq();
        invoiceReq.setCustomerId(id);
        invoiceReq.setItems(List.of(invoiceItemReq));

        InvoiceItem invoiceItem = new InvoiceItem(quantity, productID);
        invoiceItem.setProduct(product);


        when(invoiceItemService.saveInvoiceItem(invoiceItemReq)).thenReturn(invoiceItem);

        // Actuar
        invoiceService.save(invoiceReq);

        // Afirmar
        verify(invoiceItemService).saveInvoiceItem(invoiceItemReq);
        ArgumentCaptor<Invoice> invoiceAC = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceRepo).save(invoiceAC.capture());
    }
}
