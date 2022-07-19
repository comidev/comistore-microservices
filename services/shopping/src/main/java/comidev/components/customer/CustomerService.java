package comidev.components.customer;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerFeign customerFeign;

    public void validateCustomerID(Long id) {
        customerFeign.getById(id);
    }
}
