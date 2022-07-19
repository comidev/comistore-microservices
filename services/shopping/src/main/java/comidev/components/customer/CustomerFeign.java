package comidev.components.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// name = nombre de la instancia en Eureka o nombre del application
@FeignClient(path = "/customers", name = "customer")
public interface CustomerFeign {
     @GetMapping("/id/{id}")
     public Customer getById(@PathVariable(name = "id") Long id);
}
