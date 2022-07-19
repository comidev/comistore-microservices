package comidev.components.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import comidev.components.user.dto.UserReq;
import comidev.components.user.dto.Username;

@FeignClient(name = "users")
public interface UserFeign {

    @PostMapping("/users/save/cliente")
    User saveCliente(@RequestBody UserReq userReq);

    @PatchMapping("/users/{usernamePrev}/username")
    void updateUsername(@PathVariable(name = "usernamePrev") String usernamePrev,
            @RequestBody Username usernameNew);

    @GetMapping("/users/{id}")
    User getById(@PathVariable(name = "id") Long id);
}
