package comidev.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.server.ServerWebExchange;

import comidev.apigateway.dto.ErrorMessage;
import comidev.apigateway.dto.RequestDTO;
import comidev.apigateway.dto.Tokens;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private WebClient.Builder webClient;

    public AuthFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders httpHeaders = request.getHeaders();

            String AUTHORIZATION = HttpHeaders.AUTHORIZATION;
            String bearerToken;
            if (!httpHeaders.containsKey(AUTHORIZATION)) {
                bearerToken = "no-token";
            } else {
                bearerToken = httpHeaders.get(AUTHORIZATION).get(0);
            }

            String uri = request.getPath().toString();
            String method = request.getMethod().toString();

            return webClient.build()
                    .post()
                    .uri("http://auth/users/route/validate?token=" + bearerToken)
                    .bodyValue(new RequestDTO(uri, method))
                    .exchangeToMono(client -> {
                        if (client.statusCode().is2xxSuccessful()) {
                            return client.bodyToMono(Tokens.class)
                                    .map(token -> exchange)
                                    .flatMap(chain::filter);
                        } else {
                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            return response.setComplete();
                        }

                    });
        });
    }

    public static class Config {
    }
}
