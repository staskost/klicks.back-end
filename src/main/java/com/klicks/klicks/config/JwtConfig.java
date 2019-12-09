package com.klicks.klicks.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.klicks.klicks.filters.JwtFilter;

@Configuration
public class JwtConfig {
	
	private JwtFilter jwtFilter;

	public JwtConfig(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}
	
	@Bean 
	public FilterRegistrationBean<JwtFilter> filterRegistrationBean(){
		FilterRegistrationBean<JwtFilter> filterRegistrationBean= new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(jwtFilter);
		filterRegistrationBean.addUrlPatterns("/secured/*");
		return filterRegistrationBean;
	}
	
	

}
