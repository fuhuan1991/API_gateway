package com.example.demo.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PostFilter  extends ZuulFilter {
    @Override
    public String filterType() {
        return "post";
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
        System.out.println("-----Post Filter-----");

//        RequestContext ctx = RequestContext.getCurrentContext();
//        HttpServletResponse response = ctx.getResponse();
//        System.out.println("Authorization header:" + response.getHeaders("Authorization"));

        return null;
    }
}
