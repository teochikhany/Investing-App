package usj.genielogiciel.investingapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import usj.genielogiciel.investingapp.model.AppUser;
import usj.genielogiciel.investingapp.model.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


public class SecurityUtils
{
    private static final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); // not how it should be done in production
    private static final JWTVerifier verifier = JWT.require(algorithm).build();

//    SecurityUtils() {}

    static public String getAccessToken(User user, String URI)
    {
        return JWT.create()
                .withSubject(user.getUsername())             // minutes * seconds
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000) )
                .withIssuer(URI)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    static public String getAccessToken(AppUser user, String URI)
    {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000) )
                .withIssuer(URI)
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
    }

    static public String getRefreshToken(User user, String URI)
    {
        return JWT.create()
                .withSubject(user.getUsername())                 // hours * minutes * seconds
                .withExpiresAt(new Date(System.currentTimeMillis() +  2   *   60    * 60 * 1000) )
                .withIssuer(URI)
                .sign(algorithm);
    }

    static public String getSubject(String token)
    {
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }


    static public UsernamePasswordAuthenticationToken getAuthenticationToken(String token)
    {
        DecodedJWT decodedJWT = verifier.verify(token);

        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        // Getting the roles of the user
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

}
