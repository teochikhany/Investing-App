package usj.genielogiciel.investingapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import usj.genielogiciel.investingapp.filter.CustomAuthenticationFilter;
import usj.genielogiciel.investingapp.filter.CustomAuthorizationFilter;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        // Define which Service to get the Users and how to hash the passwords
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // Creating an Authentication Filter before every Request
        // Set the endpoint for login, instead of the default /login
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(this.authenticationManager());
        filter.setFilterProcessesUrl("/api/v1/login");

        // Define which route which user (role) has access to
        http.cors();
        // I don't need csrf protection since I use Json Web Token in the header of each request
        http.csrf().disable(); // this disables the csrf protection
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // hasAuthority("ROLE_ADMIN") is equal to hasRole("ADMIN"), both check the Collection of GrantedAuthority
        // http.authorizeRequests().antMatchers(GET, "/api/v1/users/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST,"/api/v1/login").permitAll();
        http.authorizeRequests().antMatchers(POST,"/api/v1/user/save").permitAll();
        http.authorizeRequests().antMatchers(POST,"/api/v1/role/save").permitAll();
        http.authorizeRequests().antMatchers(GET,"/api/v1/user/refreshtoken").permitAll();
        http.authorizeRequests().anyRequest().authenticated();

        // Adding the filters to run before each request
        http.addFilter(filter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsFilter corsFilter()
    {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // CrossOrigin("http://frontend:4200")
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "OPTIONS"));

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
