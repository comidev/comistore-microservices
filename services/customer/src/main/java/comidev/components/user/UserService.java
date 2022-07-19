package comidev.components.user;

import org.springframework.stereotype.Service;

import comidev.components.user.dto.UserReq;
import comidev.components.user.dto.Username;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserFeign userFeign;

    public User saveCliente(UserReq user) {
        return userFeign.saveCliente(user);
    }

    public void updateUsername(String usernamePrev, String usernameNew) {
        userFeign.updateUsername(usernamePrev, new Username(usernameNew));
    }

    public User getById(Long id) {
        return userFeign.getById(id);
    }
}
