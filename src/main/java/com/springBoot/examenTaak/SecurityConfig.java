package com.springBoot.examenTaak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository()))
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/login**").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/i18n/**").permitAll()
                                .requestMatchers("/favicon/**").permitAll()
                                .requestMatchers("/img/**").permitAll()
                                .requestMatchers("/403**").permitAll()
                                .requestMatchers("/rest/**").permitAll()
                                .requestMatchers("/placeholder/**").hasAnyRole("USER")
                                .requestMatchers("/tickets/**").hasAnyRole("USER")
                                .requestMatchers("/koopTickets/**").hasAnyRole("USER")
                                .requestMatchers("/changeLocale/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/sports/**").hasAnyRole("USER", "ADMIN")
                				.requestMatchers("/wedstrijden/**").hasAnyRole("USER", "ADMIN")
                				.requestMatchers("/wedstrijden/add/**").hasAnyRole("ADMIN")
                				.requestMatchers("/toevoegenWedstrijd/**").hasAnyRole("ADMIN"))
                .formLogin(form ->
                        form.defaultSuccessUrl("/sports", true)
                                .loginPage("/login")
                                .usernameParameter("username").passwordParameter("password")
                )
                .exceptionHandling(handling -> handling.accessDeniedPage("/403"));

        return http.build();
    }

}