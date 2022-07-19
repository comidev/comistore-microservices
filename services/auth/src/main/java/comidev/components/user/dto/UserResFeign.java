package comidev.components.user.dto;

import comidev.components.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResFeign {
    private Long id;
    private String username;

    public UserResFeign(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
