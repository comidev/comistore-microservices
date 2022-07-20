package comidev.components.customer;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import comidev.components.country.Country;
import comidev.components.country.CountryRepo;
import comidev.components.customer.dto.CustomerReq;
import comidev.components.customer.dto.CustomerRes;
import comidev.components.customer.dto.CustomerUpdate;
import comidev.components.user.User;
import comidev.components.user.UserService;
import comidev.exceptions.HttpException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final UserService userService;
    private final CountryRepo countryRepo;

    public List<CustomerRes> findAll() {
        return customerRepo.findAll().stream()
                .map(CustomerRes::new)
                .toList();
    }

    public Customer getById(Long id) {
        return customerRepo.findById(id).orElseThrow(() -> {
            String message = "El cliente no existe!";
            return new HttpException(HttpStatus.NOT_FOUND, message);
        });
    }

    public CustomerRes findById(Long id) {
        Customer customerDB = getById(id);
        // ? se comunica con auth-service: obtiene el user (id? y username)
        customerDB.setUser(userService.getById(customerDB.getUserID()));
        return new CustomerRes(customerDB);
    }

    private Country findCountryByName(String name) {
        return countryRepo.findByName(name).orElseThrow(() -> {
            String message = "El pais no existe!";
            return new HttpException(HttpStatus.NOT_FOUND, message);
        });
    }

    public CustomerRes save(CustomerReq customerReq) {
        boolean existsEmail = customerRepo.existsByEmail(customerReq.getEmail());
        if (existsEmail) {
            String message = "El email ya existe";
            throw new HttpException(HttpStatus.CONFLICT, message);
        }

        Customer customerNew = new Customer(customerReq);

        // ? se comunica con auth-service: guardar un usuario cliente
        User userDB = userService.saveCliente(customerReq.getUser());
        customerNew.setUserID(userDB.getId());

        Country countryDB = findCountryByName(customerReq.getCountry());
        customerNew.setCountry(countryDB);

        Customer customerSaved = customerRepo.save(customerNew);
        customerSaved.setUser(userDB);
        return new CustomerRes(customerSaved);
    }

    public CustomerRes update(CustomerUpdate customerUpdate, Long id) {
        Customer customerDB = getById(id);

        String emailNew = customerUpdate.getEmail();
        if (!customerDB.getEmail().equals(emailNew)) {
            boolean existsEmail = customerRepo.existsByEmail(emailNew);
            if (existsEmail) {
                String message = "El email ya existe";
                throw new HttpException(HttpStatus.CONFLICT, message);
            }
            customerDB.setEmail(emailNew);
        }

        Long userID = customerDB.getUserID();
        // ? se comunica con auth-service: obtiene el User (id? y username)
        User userDB = userService.getById(userID);

        String usernamePrev = userDB.getUsername();
        String usernameNew = customerUpdate.getUsername();
        if (!usernamePrev.equals(usernameNew)) {
            // ? se comunica con auth-service: actualiza
            userService.updateUsername(usernamePrev, usernameNew);
        }

        String countryNew = customerUpdate.getCountry();
        if (!customerDB.getCountry().getName().equals(countryNew)) {
            Country countryDB = findCountryByName(countryNew);
            customerDB.setCountry(countryDB);
        }

        customerDB.setName(customerUpdate.getName());
        customerDB.setGender(customerUpdate.getGender());
        customerDB.setDateOfBirth(customerUpdate.getDateOfBirth());
        customerDB.setPhotoUrl(customerUpdate.getPhotoUrl());

        return new CustomerRes(customerRepo.save(customerDB));
    }

    public void deleteById(Long id) {
        if (!customerRepo.existsById(id)) {
            String message = "El usuario no existe!!";
            throw new HttpException(HttpStatus.NOT_FOUND, message);
        }
        customerRepo.deleteById(id);
    }

    public void existsEmail(String email) {
        if (!customerRepo.existsByEmail(email)) {
            String message = "El email no existe!";
            throw new HttpException(HttpStatus.NOT_FOUND, message);
        }
    }
}
