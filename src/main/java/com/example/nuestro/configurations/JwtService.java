package com.example.nuestro.configurations;

import com.example.nuestro.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static java.lang.Long.parseLong;

@Service
public class JwtService
{
    //32 bytes 256 bits
    private  static  final  String EncryptioKey="6lUIonUvOmZN/q827FcqIZa7ES3UKtRpmJk9gAtMV8I=";
    public String ExtractEmail(String jwtToken){
        return ExtractClain(jwtToken, Claims::getSubject);
    }
    public Date ExtractExpiration(String jwtToken){
        return ExtractClain(jwtToken, Claims::getExpiration);
    }

    public String ExtractId(String jwtToken) {
        var id= ExtractClain(jwtToken, Claims::getId);
        //return parseLong(id);
        return id;
    }

    public  String GenerateToken( @NotNull Map<String,Object> extraClaims, @NotNull User user){

        return Jwts.builder().setClaims(extraClaims)
                .setIssuer("nuestro.iverique.com")
                .setId(user.id)
                .setSubject(user.email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration( new Date(System.currentTimeMillis() +1000*60 *60*24)) // 24 hours
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public  boolean IsTokenValid(String jwtToken, @NotNull UserDetails user){

        final  String email= ExtractEmail(jwtToken);
        var username= user.getUsername();
        var isTokenExpired=IsTokenExpired(jwtToken);
        var isTokenValid= (email.equals(username)) && !isTokenExpired; // GEt email
        return isTokenValid;
    }
    public  boolean IsTokenValid(String jwtToken, @NotNull User user){

        final  String email= ExtractEmail(jwtToken);
        var isTokenValid= (email.equals(user.email)) && !IsTokenExpired(jwtToken);
        return isTokenValid;
    }

    private boolean IsTokenExpired(String jwtToken) {
     return ExtractExpiration(jwtToken).before(new Date());
    }

    public  <T> T ExtractClain(String jwtToken, Function<Claims, T> claimsResolver){
        final  Claims claims= ExtractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims ExtractAllClaims(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey()).build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSigninKey() {
        byte[] keyBytes= Decoders.BASE64.decode(EncryptioKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
