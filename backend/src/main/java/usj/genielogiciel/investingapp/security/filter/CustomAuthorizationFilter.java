package usj.genielogiciel.investingapp.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import usj.genielogiciel.investingapp.security.SecurityUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        // If the user is trying to log in, skip the authorisation process
        if (!request.getServletPath().equals("/api/v1/login") && !request.getServletPath().equals("/api/v1/user/refreshtoken"))
        {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            // Skip if there is no token
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            {
                try{
                    // Reading the token from the request
                    String token = authorizationHeader.substring("Bearer ".length());
                    val authenticationToken = SecurityUtils.getAuthenticationToken(token);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                catch (Exception e)
                {
                    log.error("Error logging in {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());

                    Map<String, String> error = new HashMap<>();
                    error.put("error", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
