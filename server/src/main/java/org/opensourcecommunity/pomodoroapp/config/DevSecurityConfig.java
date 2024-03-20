
package org.opensourcecommunity.pomodoroapp.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// To use the dev profile run ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
// and spring.main.allow-bean-definition-overriding=true
// Full: ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev -Dspring.main.allow-bean-definition-overriding=true

@Configuration
@Profile("dev")
@Primary
public class DevSecurityConfig {

	@Bean
	public SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorize -> {
					authorize.anyRequest().permitAll();
				});

		return httpSecurity
				.build();
	}

	@Bean
	public CorsConfigurationSource test() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		// configuration.setAllowedOrigins(Arrays.asList("*"));
		// configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH",
		// "DELETE"));
		// configuration.setAllowedHeaders(Arrays.asList("Authorization",
		// "Content-Type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
