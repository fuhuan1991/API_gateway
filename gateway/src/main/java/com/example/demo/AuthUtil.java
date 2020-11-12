package com.example.demo;

import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.JwtBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AuthUtil {

    public static final String AUTH_URL = "http://localhost:9000/auth";

    /**
     *  This method verify the api key get from user request.
     */
    public boolean verifyAPIKey (String apiKey, RequestContext ctx) {
        if (apiKey.length() == 0) return false;
        try {
            // send the api key to authentication service
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<AuthRequest> authRequestEntity = new HttpEntity<>(new AuthRequest(apiKey));
            ResponseEntity<String> authResponseEntity = restTemplate.postForEntity(AUTH_URL, authRequestEntity, String.class);
            System.out.println("Authentication with api key succeed");
            return true;
        } catch (Exception e) {
            // api key not valid, request will be denied
            System.out.println("Authentication failed");
            this.forbiddenUserAccess(ctx, "invalid api key!");
            return false;
        }
    }

    public void forbiddenUserAccess(RequestContext ctx, String msg) {
        ctx.setResponseBody(msg);
        ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        ctx.setSendZuulResponse(false);
    }
}
