package ua.kusakabe.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import ua.kusakabe.dto.AuthRR;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private static final long EXPIRATION_TIME = 86000000;   //24 Hours

    public JwtService(){
        String secretString = "121397812431791215781719821598123252234159523152";
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(AuthRR req) {
        return Jwts.builder()   //Build new token
                .subject(req.getUsername()) //Sign token for this user -> userDetails.getUsername
                .issuedAt(new Date(System.currentTimeMillis())) //Time when token was created
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //Expiration time for token
                .signWith(secretKey)    //Encrypt with secret key
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);    //Return subject from token payload
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        return claimsResolver.apply(    //Take username from token
                Jwts.parser()
                        .verifyWith(secretKey)  //Decrypt with secretKey
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()); //Take payload
    }

    public boolean validateToken(String token, String dbuser) {
        final String username = extractUsername(token);
        return (username.equals(dbuser) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}
