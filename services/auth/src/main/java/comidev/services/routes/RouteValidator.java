package comidev.services.routes;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import comidev.components.user.dto.RequestDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "role-path")
public class RouteValidator {

    private List<Route> paths;

    private List<Route> findRoute(RequestDTO request) {
        return paths.stream().filter(item -> {
            boolean equalUri = Pattern.matches(item.getUri(), request.getUri());
            boolean equalMethod = item.getMethod().equals(request.getMethod());
            return equalUri && equalMethod;
        }).toList();
    }

    public boolean routeHasProtection(RequestDTO request) {
        return !findRoute(request).isEmpty();
    }

    public boolean validate(List<String> roles, RequestDTO request) {
        boolean isAllowed = false;

        List<Route> routeFind = findRoute(request);

        if (!routeFind.isEmpty()) {
            Route route = routeFind.get(0);
            List<String> allowedRoles = route.getRoles();

            isAllowed = allowedRoles.stream().anyMatch(roles::contains);
        }

        return isAllowed;
    }
}
