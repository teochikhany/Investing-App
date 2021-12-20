package usj.genielogiciel.investingapp.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import usj.genielogiciel.investingapp.model.ExceptionResponce;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

// TODO: move the filter package inside the security package.

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException
    {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.info("User attempting to login {}", username);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException
    {
        // Get the authenticated User
        User user = (User) authentication.getPrincipal();

        // Generating an Algorithm to encrypt the tokens,
        // "secret" show not be in the code,
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); // not how it should be done in production

        // Creating the access token
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000) )
                .withIssuer(request.getRequestURI())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        // Creating the refresh token
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 120 * 60 * 1000) )
                .withIssuer(request.getRequestURI())
                .sign(algorithm);

//        response.setHeader("access_token", access_token);
//        response.setHeader("refresh_token", refresh_token);

        // putting the tokens in the response
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException
    {
        log.error("failed authentication attempt");

        var test = new ExceptionResponce(
                                new Date(),
                                HttpStatus.UNAUTHORIZED.value(),
                        "Wrong Credentials");

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(403);
        new ObjectMapper().writeValue(response.getOutputStream(), test);
    }


}
