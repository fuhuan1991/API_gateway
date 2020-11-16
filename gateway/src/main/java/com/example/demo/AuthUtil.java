package com.example.demo;

import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class AuthUtil {

    @Autowired
    private YAMLConfig myConfig; // through this YAMLConfig instance, we can get environment configurations.
    private String auth_url;
    private String JWT_header;
    private String JWT_header_prefix;
    private String JWT_subject;
    private int JWT_valid_time;
    private String JWT_key;

    public AuthUtil(YAMLConfig myConfig) {
        this.myConfig = myConfig;
        this.auth_url = myConfig.getAuthentication_url();
        this.JWT_header = myConfig.getJWT_header();
        this.JWT_header_prefix = myConfig.getJWT_header_prefix();
        this.JWT_subject = myConfig.getJWT_subject();
        this.JWT_valid_time = myConfig.getJWT_valid_time();
        this.JWT_key = myConfig.getJWT_key();
    }

    /**
     *  This method verify the JWT get from user request.
     */
    public boolean verifyJWT(HttpServletRequest request) {
        String JWTString = request.getHeader(this.JWT_header);

        if (JWTString != null && JWTString.length() > 0) {
            JWTString = JWTString.replace(this.JWT_header_prefix, "");
            try {
                Jws<Claims> jwtClaims = Jwts.parserBuilder()
                        .setSigningKey(this.JWT_key)
                        .build()
                        .parseClaimsJws(JWTString);
                String subject = (String) jwtClaims.getBody().get("sub");
                Long exp = new Long((int)jwtClaims.getBody().get("exp")) * 1000;
                Date expiration = new Date(exp);
                Date current = new Date(System.currentTimeMillis());
                if (expiration.after(current) && subject.equals(this.JWT_subject)) {
                    // JWT is valid
                    System.out.println("JWT is valid");
                    return true;
                } else {
                    return false;
                }
            } catch (JwtException ex) {
                // JWT is not valid
                System.out.println(ex);
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     *  This method verify the api key get from user request.
     */
    public boolean verifyAPIKey (String apiKey, RequestContext ctx) {
        if (apiKey == null || apiKey.length() == 0) return false;
        try {
            // send the api key to authentication service
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<AuthRequest> authRequestEntity = new HttpEntity<>(new AuthRequest(apiKey));
            ResponseEntity<String> authResponseEntity = restTemplate.postForEntity(this.auth_url, authRequestEntity, String.class);
            System.out.println("Authentication with api key succeed");
            return true;
        } catch (Exception e) {
            // api key not valid, request will be denied
            System.out.println("Authentication failed");
            this.forbiddenUserAccess(ctx, "invalid api key!");
            return false;
        }
    }

    /**
     *  This method is called when the user has neither JWT nor api key.
     *  if returns a response with status 403
     */
    public void forbiddenUserAccess(RequestContext ctx, String msg) {
        ctx.setResponseBody(msg);
        ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        ctx.setSendZuulResponse(false);
    }

    /**
     *  This method generate a new JWT and attach it to the response
     *  and to the redirected request as a header
     */
    public void attachJWT(RequestContext ctx) {
        String token = Jwts.builder()
                .setSubject(this.JWT_subject)
                .setExpiration(new Date(System.currentTimeMillis() + this.JWT_valid_time))
                .signWith(SignatureAlgorithm.HS256, this.JWT_key)
                .compact();

        System.out.println("new JWT: " + token);
        HttpServletResponse response = ctx.getResponse();
        response.addHeader(this.JWT_header, this.JWT_header_prefix + " " + token);
        ctx.addZuulRequestHeader(this.JWT_header, this.JWT_header_prefix + " " + token);
    }


}
