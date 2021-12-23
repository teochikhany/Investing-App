package usj.genielogiciel.investingapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import usj.genielogiciel.investingapp.model.AppUser;
import usj.genielogiciel.investingapp.model.Role;
import usj.genielogiciel.investingapp.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class InvestingAppApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(InvestingAppApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService)
    {
        return args -> {
            userService.saveRole(new Role(0, "ROLE_USER"));
            userService.saveRole(new Role(0, "ROLE_ADMIN"));

            userService.saveUser(new AppUser(0, "teo", "chikhany", "12345", new ArrayList<>()));

            userService.addRoleToUser("teo", "ROLE_ADMIN");
        };
    }
}
