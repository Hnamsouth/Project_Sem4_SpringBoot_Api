package com.example.project_sem4_springboot_api.config;

import com.example.project_sem4_springboot_api.entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    // lấy thông tin username đc gán trong claim của token
    public  String extractUsername(String token) { return extractClaim(token, Claims::getSubject);}

    // trích xuất claim
    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //    trích xuất tất cả claim trong token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder() // tạo builder để phân tích token
                .setSigningKey(getSignInKey()) // set key để ktra token
                .build()
                .parseClaimsJws(token) // phân tích token
                .getBody(); // get dữ liệu
    }

    // chuyển đổi string key sang object Key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // tạp token ko có claim
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    // tạo token có claims
    public  String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return builderToken(extraClaims,userDetails,jwtExpiration);
    }
    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return builderToken(new HashMap<>(), userDetails, refreshExpiration);
    }
    // build Token
    public String builderToken( Map<String, Object> extraClaims,  UserDetails userDetails ,long expiration){
        return Jwts
                .builder()
                .setClaims(extraClaims) // set claim cho token
//                .claim("authority",author)
                .setSubject(userDetails.getUsername()) // set chủ thể
                .setIssuedAt(new Date(System.currentTimeMillis())) // set thời điểm phat hành token
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // set thời hạn token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // token đc ký với key
                .compact();
    }
    //  check token is valid
    public boolean isTokenValid (String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public boolean isTokenExpired (String token){ return extractClaim(token,Claims::getExpiration).before(new Date());}

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}
