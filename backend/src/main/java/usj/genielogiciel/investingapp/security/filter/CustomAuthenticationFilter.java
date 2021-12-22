package usj.genielogiciel.investingapp.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import usj.genielogiciel.investingapp.model.ExceptionResponce;
import usj.genielogiciel.investingapp.security.SecurityUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    // TODO: how to check who is the authenticated user
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
        // stateless handling of user request vs stateful:
        // stateful -> keep track of the sessionID in the backend (db)
        // stateless -> No database, does not keep track of sessionIDs, everything needed is in the request

        // jwt token-based authentication (vs form based authentication - vs basic Auth)
        // basic Auth -> username and password are sent (automatically by the browser) in every request, ?? maybe can't log out from backend ??
        // form based -> sessionId sent (automatically by the browser) in every request, can log out.
        // jwt token based -> access_taken sent in every request, can't log out from backend by default, use it when you have multiple services accessing your application

        // TODO: check if my front end has cookies
        // Get the authenticated User
        User user = (User) authentication.getPrincipal();

        // Creating the access and refresh token
        String access_token = SecurityUtils.getAccessToken(user, request.getRequestURI());
        String refresh_token = SecurityUtils.getRefreshToken(user, request.getRequestURI());

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
