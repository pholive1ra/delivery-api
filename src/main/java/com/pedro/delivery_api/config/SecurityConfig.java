package com.pedro.delivery_api.config;
import com.pedro.delivery_api.entity.Role;
import com.pedro.delivery_api.security.JwtAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig (JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        .requestMatchers(HttpMethod.GET, "/customers/me")
                        .hasRole(Role.CUSTOMER.name())

                        .requestMatchers(HttpMethod.PUT, "/customers/me")
                        .hasRole(Role.CUSTOMER.name())

                        .requestMatchers(HttpMethod.GET, "/customers")
                        .hasRole(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/customers/{id}")
                        .hasRole(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/customers/{id}")
                        .hasRole(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.POST, "/orders")
                        .hasRole(Role.CUSTOMER.name())

                        .requestMatchers(HttpMethod.GET, "/orders/me")
                        .hasRole(Role.CUSTOMER.name())

                        .requestMatchers(HttpMethod.GET, "/orders")
                        .hasRole(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/orders/{id}")
                        .hasRole(Role.ADMIN.name())

                        .anyRequest().authenticated()) //Atenção: Qualquer endpoint que esquecer de configurar acima será acessível por qualquer usuário autenticado, independente de ser ADMIN ou CUSTOMER!!!
                        .build();
        }

        @Bean
        public PasswordEncoder passwordEncoder () {
            return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
