package ua.kusakabe.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ua.kusakabe.dto.AuthRR;
import ua.kusakabe.service.TokenCacheService;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TokenCacheService tokenCacheService;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            String token = null;

            if (routeValidator.isSecured.test(exchange.getRequest())) {
                //header contains token ?
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Authorization header not present");
                }
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.contains("Bearer")) {
                    token = authHeader.substring(7);
                }
                //Check if token is present in cache
                if(tokenCacheService.isTokenCached(token)){
                    //If yes -> grant access with that token
                    return chain.filter(exchange);
                } else {
                    //If no -> make request to auth-service for validation
                    AuthRR req = new AuthRR();
                    req.setToken(token);
                    String resBody = restTemplate.postForObject("http://localhost:8080/auth/validate", req, String.class);
                    if(resBody != null && !resBody.isEmpty()) {
                        if(resBody.equals("200")){  //If response is 200 status code -> grant access;
                            tokenCacheService.cacheToken(token);
                            return chain.filter(exchange);
                        } else {    //Or else validation failed
                            throw new RuntimeException("Token validation failed!");
                        }
                    } else {
                        throw new RuntimeException("Invalid validation request!");
                    }
                }
            }
            return chain.filter(exchange);
        }));
    }

    public static class Config {

    }

}
