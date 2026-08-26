package com.example.quanlytruonghoc.security;


import com.example.quanlytruonghoc.security.exceptions.AccessDenied;
import com.example.quanlytruonghoc.security.exceptions.JwtEntryPoint;
import com.example.quanlytruonghoc.security.jwt.JwtTokenFilter;
import com.example.quanlytruonghoc.security.principal.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final JwtEntryPoint entryPoint;
    private final AccessDenied accessDenied;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         return http
                 .csrf(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(auth -> auth
                         .requestMatchers(
                                 "/api/auth/active-user",
                                 "/api/auth/login",
                                 "/api/auth/register",
                                 "/api/auth/logout"
                         ).permitAll()
                         .requestMatchers("/api/auth/me/**").authenticated()
                         .requestMatchers("/api/auth/verify/**").authenticated()
                         .requestMatchers(HttpMethod.GET,"/api/users/**").hasAnyAuthority("ADMIN","SUPER_ADMIN")
                         .requestMatchers(HttpMethod.POST,"/api/users/**").hasAnyAuthority("ADMIN","SUPER_ADMIN")
                         .requestMatchers(HttpMethod.DELETE,"/api/users/**").hasAnyAuthority("ADMIN","SUPER_ADMIN")
                         .requestMatchers(HttpMethod.POST, "/api/schools/**").authenticated()
                         .requestMatchers(HttpMethod.PUT, "/api/schools/**").hasAuthority("ADMIN")
                         .requestMatchers("/api/reports/**").hasAuthority("ADMIN")
                         .requestMatchers(HttpMethod.POST, "/api/courses").hasAuthority("ADMIN")
                         .requestMatchers(HttpMethod.PUT, "/api/courses/**").hasAuthority("ADMIN")
                         .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasAuthority("ADMIN")
                         .requestMatchers(HttpMethod.GET, "/api/departments/**").hasAnyAuthority("ADMIN","SUPER_ADMIN","TEACHER","STUDENT")
                         .requestMatchers(HttpMethod.POST, "/api/departments/**").hasAnyAuthority("ADMIN","SUPER_ADMIN")
                         .requestMatchers(HttpMethod.PUT, "/api/departments/**").hasAnyAuthority("ADMIN","SUPER_ADMIN")
                         .requestMatchers(HttpMethod.DELETE, "/api/departments/**").hasAnyAuthority("ADMIN","SUPER_ADMIN")
                         .requestMatchers(HttpMethod.POST, "/api/courses/**").hasAnyAuthority("ADMIN","SUPER_ADMIN")


                 )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDenied))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
