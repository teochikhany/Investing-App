package usj.genielogiciel.investingapp.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import usj.genielogiciel.investingapp.exceptions.VariableValidation;
import usj.genielogiciel.investingapp.model.AppUser;
import usj.genielogiciel.investingapp.model.AppUserInfo;
import usj.genielogiciel.investingapp.model.Role;
import usj.genielogiciel.investingapp.model.RoleToUserForm;
import usj.genielogiciel.investingapp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController
{
    private final UserService userService;

    @GetMapping("/users")
    private ResponseEntity<List<AppUserInfo>> getUsers()
    {
        return ResponseEntity.ok(userService.getUsers()
                .stream()
                .map(user -> new AppUserInfo(user.getUsername(),user.getName()))
                .collect(Collectors.toList()));
    }

    @PostMapping("/user/save")
    private ResponseEntity<AppUserInfo> addUser(@RequestBody @Valid AppUser user, Errors errors)
    {
        if (errors.hasErrors()) {
            throw new VariableValidation(errors);
        }

        final AppUser appUser = userService.saveUser(user);
        final AppUserInfo appUserInfo = new AppUserInfo(appUser.getUsername(), appUser.getName());

        return new ResponseEntity<AppUserInfo>(appUserInfo, HttpStatus.CREATED);
    }

    @PostMapping("/role/save")
    private ResponseEntity<Role> addRole(@RequestBody @Valid Role role, Errors errors)
    {
        if (errors.hasErrors()) {
            throw new VariableValidation(errors);
        }

        return new ResponseEntity<Role>(userService.saveRole(role), HttpStatus.CREATED);
    }

    @PostMapping("/role/saveToUser")
    private ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form)
    {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    // TODO: should have a logout endpoint? (maybe already have from spring security)

    @GetMapping("/user/refreshtoken")
    private void getRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
        {
            try{
                //TODO: put this redundant code in a Utils class (maybe singleton, or @Bean, or both ???)

                // Reading the token from the request
                String refresh_token = authorizationHeader.substring("Bearer ".length());

                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); // not how it should be done in production
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);

                String username = decodedJWT.getSubject();
                AppUser user = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000) )
                        .withIssuer(request.getRequestURI())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);


                // putting the tokens in the response
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
            catch (Exception e)
            {
                log.error("Error refreshing the token in {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
    }
}
