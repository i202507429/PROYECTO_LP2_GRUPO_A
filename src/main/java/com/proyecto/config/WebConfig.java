package com.proyecto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AdminInterceptor())
			.addPathPatterns(
				"/home",
				"/pizza/**",
				"/tamanio/**",
				"/pizza-tamanio/**",
				"/adicional/**",
				"/usuario/**"
			);
	}
}