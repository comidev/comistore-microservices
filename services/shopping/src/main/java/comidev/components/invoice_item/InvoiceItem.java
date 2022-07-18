package comidev.components.invoice_item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
// import javax.persistence.JoinColumn;
// import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import comidev.components.invoice_item.dto.InvoiceItemReq;
import comidev.components.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoice_items")
@NoArgsConstructor
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "product_id", nullable = false)
    private Long productID;
    // @OneToOne
    // @JoinColumn(name = "product_id", nullable = false)
    @Transient
    private Product product = new Product();

    public InvoiceItem(Integer quantity, Long productID) {
        this.quantity = quantity;
        this.productID = productID;
    }

    public InvoiceItem(InvoiceItemReq invoiceItemReq) {
        this.quantity = invoiceItemReq.getQuantity();
    }
}
