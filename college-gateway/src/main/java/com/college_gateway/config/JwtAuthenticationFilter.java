package com.college_gateway.config;

import com.college_gateway.util.ResponseModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter, GatewayFilterFactory<Object> {

    Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String SECRET_KEY = "secretssecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${api.auth-service}")
    private String authServiceUrl;

    private boolean isValidToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<ResponseModel> responseOfValidateToken = restTemplate.exchange(authServiceUrl + "/validate-token", HttpMethod.GET, entity, ResponseModel.class);
            log.info("Response for validate token : {}", responseOfValidateToken);

            if (responseOfValidateToken != null && responseOfValidateToken.getBody() != null) {
                return responseOfValidateToken.getBody().getCode().is2xxSuccessful();
            }
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
        }
        return false;
    }

    private String extractRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token.substring(7))
                    .getBody();
            return (String) claims.get("role");
        } catch (Exception e) {
            log.error("Error extracting role from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Return unauthorized if token is missing or invalid
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extract JWT token and validate (Implement JWT validation here)
        if (!isValidToken(authHeader)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extract roles from the token
        String role = extractRoleFromToken(authHeader);
        log.info("Extracted from Request, Role : {}", role);

        if (role != null) {
            exchange.getRequest().mutate()
                    .header("role", role)
                    .build();
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
//
//        // CACHING WITH RADIS
//        String cacheKey = exchange.getRequest().getURI().toString();
//        String cachedResponse = redisTemplate.opsForValue().get(cacheKey);
//
//        if (cachedResponse != null) {
//            exchange.getResponse().getHeaders().set("X-Cache", "HIT");
//            return exchange.getResponse().setComplete();
//        }
//
//        return chain.filter(exchange).doOnTerminate(() -> {
//            // After the response is sent, cache it
//            redisTemplate.opsForValue().set(cacheKey, exchange.getResponse().toString());
//        });

        return chain.filter(exchange);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return new JwtAuthenticationFilter();
    }

    @Override
    public Class<Object> getConfigClass() {
        return Object.class;
    }
}