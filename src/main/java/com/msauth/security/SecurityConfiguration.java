package com.msauth.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.msauth.user.UserService;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserPrincipalDetailsService userPrincipalDetailsService; 
	@Autowired
	UserService userService;
	@Autowired
	JwtFilter jwtFilter;
	
	
    /* Configurations for authentication - login */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userPrincipalDetailsService).passwordEncoder(new BCryptPasswordEncoder());

    }
    
    /* Encryption type */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }	
    
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /* Configuration for authorization - access */
    @Override
    protected void configure(HttpSecurity http) throws Exception  {
    	
    	
        http
                /* Remove csrf and state in session because in jwt we do not need them */
        		.cors().and() // NUNCA ESQUECER DE COLOCAR PARA FAZER O CORS
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)	
                .and()
                
                /* Add jwt filters (1. authentication, 2. authorization) */                
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) 
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),  this.userService))
                
                .authorizeRequests()

                /* configure access roles */
                .antMatchers(HttpMethod.POST, "/api/authenticate", "/api/pass").permitAll()                 
                .antMatchers("/api/index", "/api/logout", "/logout", "/eureka").permitAll()  
                .antMatchers("/api/users", "/api/users/**").hasAnyAuthority("ROLE_ADMIN")
                                                
                /* Block if you are not authenticated */
                .antMatchers("/**").denyAll()
                .anyRequest().authenticated() 
        		.and()
                
        		/* Access denied */
		        .exceptionHandling()
		        .accessDeniedPage("/api/access-denied")
        		.and()
        		        		        		
        		/* Logout */
		        .logout()
		        .logoutUrl("/logout")
		        .logoutSuccessUrl("/api/logout")
		        .invalidateHttpSession(true)
		        .deleteCookies("Authorization");
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    @Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

    /* Configuration for static resources - CSS, Materialize... */
    @Override
    public void configure(WebSecurity web) throws Exception {
    	web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }
}
