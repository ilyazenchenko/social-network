package ru.zenchenko.socialsecuritymicrocervice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.zenchenko.socialsecuritymicrocervice.services.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain
            (HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/auth/**", "/error", "/process_login")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(fl -> fl.loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .successHandler(
                                myAuthenticationSuccessHandler()
                        )
                        .failureUrl(
                                "/auth/login?error")
                )
                .logout(l -> l
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"));

        return http.build();
    }

    //Конфигурируем аутентификацию
    @Bean
    public AuthenticationManager authManager
    (HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder
                authenticationManagerBuilder =
                http.getSharedObject(
                        AuthenticationManagerBuilder.class
                );
        authenticationManagerBuilder
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(getPasswordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler
    myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

}

