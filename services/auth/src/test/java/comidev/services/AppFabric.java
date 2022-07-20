package comidev.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import comidev.components.role.RoleRepo;
import comidev.components.user.User;
import comidev.components.user.UserRepo;
import comidev.services.jwt.JwtService;
import comidev.services.jwt.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@Getter
@AllArgsConstructor
public class AppFabric {
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bcrypt;
    private final RoleRepo roleRepo;

    private String generate() {
        return UUID.randomUUID().toString();
    }

    public User createUser(String username, String password) {
        String usernameDB = username != null ? username : generate();
        String passwordDB = password != null ? bcrypt.encode(password) : "comidev123";
        return userRepo.save(new User(usernameDB, passwordDB));
    }

    public String createToken(String... roles) {
        List<String> rolesName = roles.length > 0 ? List.of(roles) : List.of("ADMIN");
        String token = jwtService
                .createTokens(new Payload(1l, "comidev", rolesName))
                .getAccess_token();
        return "Bearer " + token;
    }
}
