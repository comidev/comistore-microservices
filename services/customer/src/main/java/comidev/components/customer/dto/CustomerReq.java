package comidev.components.customer.dto;

import java.sql.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import comidev.components.customer.Gender;
import comidev.components.user.dto.UserReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReq {
    @NotEmpty(message = "No puede ser vacio")
    private String name;

    @NotEmpty(message = "No puede ser vacio")
    @Email(message = "No tiene formato de email")
    private String email;

    @NotNull(message = "No puede ser vacio")
    private Gender gender;

    @NotNull(message = "No puede ser vacio")
    private Date dateOfBirth;

    @NotEmpty(message = "No puede ser vacio")
    private String photoUrl;

    @NotNull(message = "No puede ser vacio")
    private UserReq user;

    @NotEmpty(message = "No puede ser vacio")
    private String country;
}
