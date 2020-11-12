package com.example.demo;

import com.example.demo.filter.AuthFilter;
import com.example.demo.filter.PostFilter;
import com.example.demo.filter.RouteURLFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
public class ZuulServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulServerApplication.class, args);
	}

	@Bean
	public AuthFilter authFilter() { return new AuthFilter(yamlConfig()); }

	@Bean
	public RouteURLFilter routerFilter() {
		return new RouteURLFilter();
	}

	@Bean
	public PostFilter postFilter() {
		return new PostFilter();
	}

	@Bean
	public YAMLConfig yamlConfig() { return new YAMLConfig(); }

	@Bean
	public AuthUtil authUtil() { return new AuthUtil(); }
}


