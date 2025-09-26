package com.carolin.libraryproject.authentication;

import com.carolin.libraryproject.security.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Säker nyckel
    @Value("${app.jwtSecret:mySecretKey}")
    private String jwtSecret;

    // Giltig 1h
    @Value("3600000")
    private int jwtExpirationMs;


// Tar en inloggad användare och genererar token
    public String generateToken(Authentication authentication) {

       CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
       String role = userDetails.getAuthorities().iterator().next().getAuthority();

       // Returnerar en JWT sträng.
       return Jwts.builder()
               .setSubject(userDetails.getUsername())
               .claim("role", role)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis()+ jwtExpirationMs))
               .signWith(SignatureAlgorithm.HS512, jwtSecret)
               .compact();
    }



    // Låser upp token med hemlig nyckel
    public String getUsernameFromToken(String token) {

        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }


    // Läser av vilken roll användaren har genom token
    public String getRoleFromToken(String token) {

        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }


    // I JwtTokenProvider
    public String generateTokenFromUsernameAndRole(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }



    // Validering av token som returnerar token true om den är validerad annars loggar den felmeddelande.
    public boolean validateToken(String token) {

        try{
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return true;
        }
        catch(SignatureException e){
            logger.error("Invalid JWT signature: {}",e.getMessage());
        }
        catch (MalformedJwtException e){
            logger.error("Invalid JWT token: {}",e.getMessage());
        }
        catch (ExpiredJwtException e){
            logger.error("Expired JWT token: {}",e.getMessage());
        }
        catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty: {}",e.getMessage());
        }

        return false;
    }
}
