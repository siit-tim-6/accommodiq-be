package com.example.accommodiq.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.example.accommodiq.domain.Account;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Utility klasa za rad sa JSON Web Tokenima
@Component
public class TokenUtils {

    // Izdavac tokena
    @Value("spring-security-example")
    private String APP_NAME;

    // Tajna koju samo backend aplikacija treba da zna kako bi mogla da generise i proveri JWT https://jwt.io/
    @Value("somesecret")
    public String SECRET;

    // Period vazenja tokena - 30 minuta
    @Value("1800000")
    private int EXPIRES_IN;

    // Naziv headera kroz koji ce se prosledjivati JWT u komunikaciji server-klijent
    @Value("Authorization")
    private String AUTH_HEADER;

    private static final String AUDIENCE_WEB = "web";

    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;


    // ============= Funkcije za generisanje JWT tokena =============

    public String generateToken(String username) {
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(username)
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    private String generateAudience() {
        return AUDIENCE_WEB;
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }

    // ============= Funkcije za citanje informacija iz JWT tokena =============

    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // preuzimamo samo token (vrednost tokena je nakon "Bearer " prefiksa)
        }

        return null;
    }

    public String getUsernameFromToken(String token) {
        String username;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            username = null;
        }

        return username;
    }

    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            audience = claims.getAudience();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            expiration = null;
        }

        return expiration;
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            claims = null;
        }

        // Preuzimanje proizvoljnih podataka je moguce pozivom funkcije claims.get(key)

        return claims;
    }

    // =================================================================

    // ============= Funkcije za validaciju JWT tokena =============

    public Boolean validateToken(String token, UserDetails userDetails) {
        Account account = (Account) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);

        // Token je validan kada:
        return (username != null // korisnicko ime nije null
                && username.equals(userDetails.getUsername()) // korisnicko ime iz tokena se podudara sa korisnickom imenom koje pise u bazi
                && !isCreatedBeforeLastPasswordReset(created, new Date(TimeUnit.SECONDS.toMillis(account.getLastPasswordResetDate())))); // nakon kreiranja tokena korisnik nije menjao svoju lozinku
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    // =================================================================

    public int getExpiredIn() {
        return EXPIRES_IN;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

}
