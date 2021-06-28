package ru.otus.istyazhkina.library.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/public/**", "/img/**", "/style/**")
                .permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/**/add").hasRole("ADMIN")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/genres/**", "/books/**", "/authors/**").hasRole("ADMIN")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/genres/**", "/books/**", "/authors/**").hasRole("ADMIN")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .defaultSuccessUrl("/")
                .and()
                .rememberMe()
                .key("library")
                .tokenValiditySeconds(24 * 60 * 60)
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
