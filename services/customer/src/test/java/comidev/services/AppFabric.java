package comidev.services;

import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import comidev.components.country.Country;
import comidev.components.country.CountryRepo;
import comidev.components.customer.Customer;
import comidev.components.customer.CustomerRepo;
import comidev.components.customer.Gender;
import comidev.components.user.UserService;
import comidev.components.user.dto.UserReq;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@Getter
@AllArgsConstructor
public class AppFabric {
    private final CountryRepo countryRepo;
    private final CustomerRepo customerRepo;
    private final UserService userService;

    private String generate() {
        return UUID.randomUUID().toString();
    }

    public Country createCountry(String name) {
        String nameDB = name != null ? name : "Perú";
        return countryRepo.save(countryRepo.findByName(nameDB)
                .orElse(new Country(nameDB)));
    }

    public Customer createCustomer(String email, String username, Country country) {
        String emailDB = email != null ? email : (generate() + "@gmail.com");

        Country countryDB = country != null ? country : createCountry(null);

        String usernameDB = username != null ? username : generate();

        Long userID = userService
                .saveCliente(new UserReq(usernameDB, "password"))
                .getId();

        return customerRepo.save(new Customer("x", emailDB, Gender.MALE,
                Date.valueOf(LocalDate.now()), "X",
                userID, countryDB));
    }
}
