package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                registry ->{
                    registry.requestMatchers("/").permitAll();
                    registry.requestMatchers("/users/**").hasRole("ADMIN");
                    registry.requestMatchers("/admin/**").hasRole("USER");
                    registry.anyRequest().permitAll();
                }
        )
                .formLogin(form ->
                        form.loginPage("/showLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied"))
                .build();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails adminUser  = User.builder()
                .username("anhTuan")
                .password("$2a$12$6TfBeCot4AOYLGNHDk9XyOVEcsDLjLnAG5pkbT4z/u65kdwTPPaba") // anhTuan
                .roles("ADMIN")
                .build();
        UserDetails normalUser  = User.builder()
                .username("anhKiet")
                .password("$2a$12$fAY6PaltvgIFhp1GFeKzceFhCBvu5hPphkKI3MPvY2mhjPzhQ2ZEK") // anhKiet
                .roles("USER")
                .build();
    return new InMemoryUserDetailsManager(adminUser,normalUser);

    }
}
