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
    private HashSet<Pattern> authFree = new HashSet<>();

    @Autowired
    private YAMLConfig myConfig; // through this YAMLConfig instance, we can get environment configurations.

    @Autowired
    private AuthUtil authUtil;

    public AuthFilter(YAMLConfig myConfig, AuthUtil authUtil) {
        this.myConfig = myConfig;
        this.authUtil = authUtil;
        // get white list from yaml file.
        // all the endpoints in white list is free from authentication
        List<String> authFreeList = this.myConfig.getAuth_free_list();
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
        if (this.authUtil.verifyJWT(request)) {
            // if JWT is valid, no need to check the api key.
            return null;
        }

        // no valid JWT in request, check api key
        String apiKey = request.getHeader("api_key");
        System.out.println("no valid JWT in request, check api key: " + apiKey);
        boolean isApiKeyValid = this.authUtil.verifyAPIKey(apiKey, ctx);

        if (isApiKeyValid) {
            // generate a new JWT and attach it to the response and to the redirected request
            this.authUtil.attachJWT(ctx);
        } else {
            this.authUtil.forbiddenUserAccess(ctx, "invalid api key!");
        }

        return null;
    }

}