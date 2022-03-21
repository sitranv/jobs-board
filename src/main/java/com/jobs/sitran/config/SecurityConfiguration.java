package com.jobs.sitran.config;

import com.jobs.sitran.security.AuthorityConstants;
import com.jobs.sitran.security.filter.CorsFilterCustom;
import com.jobs.sitran.security.jwt.JWTAuthEntryPoint;
import com.jobs.sitran.security.jwt.JwtConfig;
import com.jobs.sitran.security.jwt.JwtFilter;
import com.jobs.sitran.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@Import(SecurityProblemSupport.class)
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    @Autowired
    private JWTAuthEntryPoint unauthorizedHandler;//Exception Handler

    public SecurityConfiguration(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .csrf()
                .disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(new CorsFilterCustom(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/authenticate").permitAll()
                .antMatchers("/users/register").permitAll()
                .antMatchers("/users/confirm").permitAll()
                .antMatchers("/activate").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/jobs/**").permitAll()
                .antMatchers("/jobs/create").authenticated()
                .antMatchers("/company/create").hasAuthority(AuthorityConstants.EMPLOYER)
                .antMatchers("/employer/**").hasAnyAuthority(AuthorityConstants.EMPLOYER, AuthorityConstants.ADMIN)
                .antMatchers("/admin/**").hasAuthority(AuthorityConstants.ADMIN)
//                .antMatchers("/api/**").authenticated()
//                .antMatchers("/management/**").hasAuthority(AuthorityConstants.ADMIN)
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JwtConfig securityConfigurerAdapter() {
        return new JwtConfig(tokenProvider);
    }

}
