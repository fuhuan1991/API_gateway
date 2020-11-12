package com.example.demo.filter;

import com.example.demo.AuthRequest;
import com.example.demo.AuthUtil;
import com.example.demo.YAMLConfig;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(AuthFilter.class);
    public static final long EXPIRATION_TIME = 86400000; // 1 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER = "API_token";
    public static final String JWT_SUBJECT = "api permission";
    private HashSet<Pattern> authFree = new HashSet<>();

    @Autowired
    private YAMLConfig myConfig; // through this YAMLConfig instance, we can get environment configurations.

    @Autowired
    private AuthUtil authUtil;

    public AuthFilter(YAMLConfig myConfig) {
        this.myConfig = myConfig;
        // get white list from yaml file.
        // all the endpoints in white list is free from authentication
        List<String> authFreeList = this.myConfig.getAuthFree();
        Iterator<String> iter = authFreeList.iterator();
        while (iter.hasNext()) {
            Pattern p = Pattern.compile("^" + iter.next() + "(.*)");
            this.authFree.add(p);
        }
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        Iterator<Pattern> iter = this.authFree.iterator();

        while (iter.hasNext()) {
            Pattern p = iter.next();
            Matcher m = p.matcher(path);
            if (m.find()) return false;
        }
        return true;
    }

    @Override
    public Object run() {
        System.out.println("-----Auth Filter-----");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        System.out.println(request.getMethod() + "  " + request.getRequestURL());

        // check JWT in request
        System.out.println("check JWT in request...");
        String JWTString = request.getHeader(JWT_HEADER);
        // get jwt key from YAML file
        String jwtKey = this.myConfig.getJWT_key();

        if (JWTString != null && JWTString.length() > 0) {
            JWTString = JWTString.replace(TOKEN_PREFIX, "");
            try {
                Jws<Claims> jwtClaims = Jwts.parserBuilder()
                        .setSigningKey(jwtKey)
                        .build()
                        .parseClaimsJws(JWTString);
                String subject = (String) jwtClaims.getBody().get("sub");
                Long exp = new Long((int)jwtClaims.getBody().get("exp")) * 1000;
                Date expiration = new Date(exp);
                Date current = new Date(System.currentTimeMillis());
                if (expiration.after(current) && subject.equals(JWT_SUBJECT)) {
                    // JWT is valid
                    System.out.println("JWT is valid");
                    return null;
                }
            } catch (JwtException ex) {
                // JWT is not valid
                System.out.println(ex);
            }
        }

        // no valid JWT in request, check api key
        String apiKey = request.getHeader("api_key");
        System.out.println("no valid JWT in request, check api key: " + apiKey);

        boolean isApiValid = this.authUtil.verifyAPIKey(apiKey, ctx);

        if (isApiValid) {
            // generate a new JWT
            String token = Jwts.builder()
                    .setSubject(JWT_SUBJECT)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS256, jwtKey)
                    .compact();

            System.out.println("new JWT: " + token);
            HttpServletResponse response = ctx.getResponse();
            response.addHeader(JWT_HEADER, TOKEN_PREFIX + token);
        } else {
            this.authUtil.forbiddenUserAccess(ctx, "invalid api key!");
        }

        return null;
    }

}