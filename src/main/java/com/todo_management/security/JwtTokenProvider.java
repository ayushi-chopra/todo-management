package com.todo_management.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwSecretKey;
    @Value("${app.jwt-expiration-milliseconds}")
    private Long jwtExpirationMilliseconds;

    //generate token utility method
    public String generateToken(Authentication authentication){
        String username= authentication.getName();
        Date currentDate=new Date();
        Date expiraDate=new Date(currentDate.getTime()+jwtExpirationMilliseconds);

       return Jwts.builder().subject(username).issuedAt(currentDate)
                .signWith(key()).compact();
    }


    private SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwSecretKey));
    }

    public String getUsernameFromToken(String token){
       return Jwts.parser().verifyWith((SecretKey)key()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public Boolean validateToken(String token){
        Jwts.parser().verifyWith(key()).build().parse(token);
        return true;
    }
}
