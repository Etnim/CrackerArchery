package org.deeper.crackerarchery.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class SecurityConfiguration {
    @Value("${spring.security.user.name}")
    private String usernameFile;

    @Value("${spring.security.user.password}")
    private String passwordFile;

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) throws IOException {
        System.out.println(readFile(usernameFile));
        System.out.println(readFile(passwordFile));
        UserDetails reviewer = User
                .withUsername(readFile(usernameFile))
                .password(encoder.encode(readFile(passwordFile)))
                .build();
        return new InMemoryUserDetailsManager(reviewer);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/archery/intersection/check").permitAll();
                    authorize.requestMatchers("/archery/stats/requests").authenticated();
                    authorize.anyRequest().denyAll();
                }
        ).httpBasic(Customizer.withDefaults()).build();
    }

    private String readFile(String path) throws IOException {
        return Files.readString(Path.of(path)).trim();
    }
}