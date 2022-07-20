package comidev.components.invoice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import comidev.components.customer.CustomerService;
import comidev.components.invoice.dto.InvoiceReq;
import comidev.components.invoice_item.InvoiceItem;
import comidev.components.invoice_item.dto.InvoiceItemReq;
import comidev.components.product.Product;
import comidev.components.product.ProductService;
import comidev.config.ApiIntegrationTest;
import comidev.services.AppFabric;
import comidev.services.Json;

@ApiIntegrationTest
public class InvoiceControllerTest {
    @Autowired
    private AppFabric fabric;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private ProductService productService;

    @Autowired
    private Json json;

    @BeforeEach
    void setUp() {
        fabric.getInvoiceRepo().deleteAll();
        fabric.getInvoiceItemRepo().deleteAll();
    }

    // * GET, /invoices
    @Test
    void NO_CONTENT_CuandoNoHayCompras_findAll() throws Exception {
        ResultActions res = mockMvc.perform(get("/invoices"));

        res.andExpect(status().isNoContent());
    }

    @Test
    void OK_CuandoHayAlMenosUnaCompra_findAll() throws Exception {
        fabric.createInvoice(null, null);

        ResultActions res = mockMvc.perform(get("/invoices"));

        res.andExpect(status().isOk());
    }

    // * GET, /invoices/customer/{id}
    @Test
    void NO_CONTENT_CuandoElClienteNoTieneCompras_findByCustomerId() throws Exception {
        Long customerID = 123l;

        ResultActions res = mockMvc
                .perform(get("/invoices/customer/" + customerID));

        res.andExpect(status().isNoContent());
    }

    @Test
    void OK_CuandoElClienteTieneAlMenosUnaCompra_findByCustomerId() throws Exception {
        Long customerID = 123l;
        InvoiceItem invoiceItemDB = fabric.createInvoiceItem(null);
        fabric.createInvoice(customerID, invoiceItemDB);

        ResultActions res = mockMvc
                .perform(get("/invoices/customer/" + customerID));

        res.andExpect(status().isOk());
    }

    // * POST, /invoices
    @Test
    void BAD_REQUEST_CuandoHayErrorDeValidacion_save() throws Exception {
        InvoiceReq body = new InvoiceReq("", -1l, List.of());

        ResultActions res = mockMvc.perform(post("/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(body)));

        res.andExpect(status().isBadRequest());
    }

    @Test
    void CREATED_CuandoSeRegistraCorrectamenteLaCompra_save() throws Exception {
        Long customerID = 123l;
        Long productID = 123l;
        InvoiceReq body = new InvoiceReq("x", customerID,
                List.of(new InvoiceItemReq(1, productID)));

        Product product = new Product();
        product.setPrice(20f);
        when(productService.updateStock(any(), any())).thenReturn(product);

        ResultActions res = mockMvc.perform(post("/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toJson(body)));

        res.andExpect(status().isCreated());
    }
}
