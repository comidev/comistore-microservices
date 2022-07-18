package comidev.components.invoice;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
// import javax.persistence.JoinColumn;
// import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
// import javax.persistence.Transient;

// import comidev.components.customer.Customer;
import comidev.components.invoice.dto.InvoiceReq;
import comidev.components.invoice_item.InvoiceItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoices")
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Float total;


    @Column(name = "customer_id", nullable = false)
    private Long customerID;
    // @ManyToOne
    // @JoinColumn(name = "customer_id", nullable = false)
    // @Transient
    // private Customer customer = new Customer();

    @OneToMany
    private List<InvoiceItem> invoiceItems;

    public Invoice(String description, Float total, Long customerID, List<InvoiceItem> invoiceItems) {
        this.description = description;
        this.total = total;
        this.customerID = customerID;
        this.invoiceItems = invoiceItems;
    }

    public Invoice(InvoiceReq invoiceReq) {
        this.description = invoiceReq.getDescription();
    }
}
