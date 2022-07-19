package comidev.services.routes;

import java.util.List;

import lombok.Getter;

@Getter
public class Route {
    private String uri;
    private String method;
    private List<String> roles;
}
