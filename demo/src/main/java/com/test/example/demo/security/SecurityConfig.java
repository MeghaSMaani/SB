package com.test.example.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserAuthProvider userAuthProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JWTAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) ->
                        requests.requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                                .anyRequest().authenticated());
        return http.build();
    }
//
//
//@Bean
//public InMemoryUserDetailsManager userDetailService() {
//    UserDetails user = User.withDefaultPasswordEncoder()
//            .username("Megha")
//            .password("Password123")
//            .roles("USER")
//            .build();
//
//    UserDetails admin = User.withDefaultPasswordEncoder()
//            .username("Rohith")
//            .password("Password123")
//            .roles("ADMIN")
//            .build();
//    return new InMemoryUserDetailsManager(user, admin);
//}
//
//public SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> securityConfigurerAdapter(){
//    return new SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
//
//        public void configure(HttpSecurity http) throws Exception{
//            http.authorizeRequests()
//                    .requestMatchers("/GetSessionMetaData/**").hasRole("ADMIN")
//                    .requestMatchers("/fetchAllSession").hasAnyRole("USER","ADMIN")
//                    .anyRequest().authenticated();
//        }
//    };
//}


}
