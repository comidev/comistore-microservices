package comidev.components.user;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import comidev.components.role.RoleName;
import comidev.components.role.RoleRepo;
import comidev.components.user.dto.Passwords;
import comidev.components.user.dto.RequestDTO;
import comidev.components.user.dto.UserReq;
import comidev.components.user.dto.UserRes;
import comidev.components.user.dto.UserResFeign;
import comidev.exceptions.HttpException;
import comidev.services.jwt.JwtService;
import comidev.services.jwt.Payload;
import comidev.services.jwt.Tokens;
import comidev.services.routes.RouteValidator;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bcrypt;
    private final RouteValidator routeValidator;

    private User save(UserReq userReq, RoleName roleName) {
        boolean existsUsername = userRepo.existsByUsername(userReq.getUsername());
        if (existsUsername) {
            String message = "El username ya existe!!!";
            throw new HttpException(HttpStatus.CONFLICT, message);
        }

        String passwordEncode = bcrypt.encode(userReq.getPassword());
        User userNew = new User(userReq.getUsername(), passwordEncode);
        userNew.getRoles().add(roleRepo.findByName(roleName));
        return userRepo.save(userNew);
    }

    public List<UserRes> findAll() {
        return userRepo.findAll().stream()
                .map(UserRes::new)
                .toList();
    }

    public UserResFeign getById(Long id) {
        return userRepo.findById(id)
                .map(UserResFeign::new)
                .orElseThrow(() -> {
                    String message = "El usuario no existe!!!";
                    return new HttpException(HttpStatus.NOT_FOUND, message);
                });
    }

    public UserRes saveAdmin(UserReq userReq) {
        return new UserRes(save(userReq, RoleName.ADMIN));
    }

    public UserResFeign saveCliente(UserReq userReq) {
        return new UserResFeign(save(userReq, RoleName.CLIENTE));
    }

    public boolean existsUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    public boolean updatePassword(Long id, Passwords passwords) {
        User userDB = userRepo.findById(id)
                .orElseThrow(() -> {
                    String message = "El usuario con es id no existe!!!!";
                    throw new HttpException(HttpStatus.NOT_FOUND, message);
                });

        boolean passwordIsCorrect = bcrypt
                .matches(passwords.getCurrentPassword(), userDB.getPassword());

        if (!passwordIsCorrect) {
            String message = "Password incorrecto!!!";
            throw new HttpException(HttpStatus.UNAUTHORIZED, message);
        }

        userDB.setPassword(passwords.getNewPassword());
        userRepo.save(userDB);
        return true;
    }

    private User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> {
                    String message = "Credenciales incorrectas";
                    return new HttpException(HttpStatus.UNAUTHORIZED, message);
                });

    }

    public Tokens login(UserReq userReq) {
        User userDB = findByUsername(userReq.getUsername());
        String passwordDB = userDB.getPassword();

        if (!bcrypt.matches(userReq.getPassword(), passwordDB)) {
            String message = "Credenciales incorrectas";
            throw new HttpException(HttpStatus.UNAUTHORIZED, message);
        }

        List<String> roles = userDB.getRoles().stream()
                .map(item -> item.getName().toString())
                .toList();

        return jwtService.createTokens(new Payload(
                userDB.getId(),
                userDB.getUsername(),
                roles));
    }

    public Tokens tokenGenerate(String roleName) {
        Long id = (long) (Math.random() * 1000);
        String username = "Test Swagger: " + UUID.randomUUID().toString();
        List<String> roles = List.of(roleName);
        return jwtService.createTokens(new Payload(id, username, roles));
    }

    public Tokens tokenRefresh(String bearerToken) {
        Payload payload = jwtService.verify(bearerToken);
        return jwtService.createTokens(payload);
    }

    public void tokenValidate(String bearerToken) {
        jwtService.verify(bearerToken);
    }

    public void updateUsername(String usernamePrev, String usernameNew) {
        if (userRepo.existsByUsername(usernameNew)) {
            String message = "El nuevo username ya existe!!";
            throw new HttpException(HttpStatus.CONFLICT, message);
        }
        User userDB = findByUsername(usernamePrev);
        userDB.setUsername(usernameNew);
        userRepo.save(userDB);
    }

    public Tokens routeValidateToken(String token, RequestDTO requestDTO) {
        if (!routeValidator.routeHasProtection(requestDTO)) {
            return new Tokens(token, token);
        }

        System.out.println("Es protegido!! Tendremos Token?: " + token);
        
        Payload payload = jwtService.verify(token);
        boolean isValid = routeValidator.validate(payload.getRoles(), requestDTO);
        if (!isValid) {
            String message = "No tiene permisos...";
            throw new HttpException(HttpStatus.FORBIDDEN, message);
        }
        return new Tokens(token, token);
    }
}
