package kg.alatoo.demooauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{

        security
                .formLogin().
                loginPage("/login").permitAll().and()
                .formLogin()
                .failureForwardUrl("/register")
                .defaultSuccessUrl("/", true)
                .permitAll().and()
                .authorizeHttpRequests((requests) ->requests
                        .requestMatchers("/", "/register","/login").permitAll()
                        /*.requestMatchers("/admin").hasRole("ADMIN")*/
                        .requestMatchers("/book_register",
                                "/editBook/{id}","/deleteBook/{id}",
                                "/borrowers/*", "/borrowers" , "/admin").hasAnyAuthority("ADMIN")
                        .anyRequest().authenticated())
                .logout(LogoutConfigurer::permitAll);
        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(7);}

}