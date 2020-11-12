package com.example.demo.filter;

import com.example.demo.AuthRequest;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;

public class RouteURLFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        System.out.println("-----Router Filter-----");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        System.out.println("Route Host:" + ctx.getRouteHost());
        HttpServletResponse response = ctx.getResponse();
//        System.out.println("Authorization header:" + response.getHeaders("Authorization"));
//        URIRequest uriRequest;
//        uriRequest = getURIRedirection(ctx);


        return null;
    }
}
