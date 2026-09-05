package com.harshadcodes.EcommerceWebsite.security.jwt;


import com.harshadcodes.EcommerceWebsite.security.services.UserDetailsImpl;
import com.harshadcodes.EcommerceWebsite.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${security.secretKey}")
    private String secretKey;

    @Value("${security.expirationDate}")
    private Duration expirationDate;

    @Value("${security.jwtCookie}")
    private String jwtCookie;

    private SecretKey key(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private final Logger logger=LoggerFactory.getLogger(JwtUtils.class);


    public String getJwtFromCookie(HttpServletRequest request){
        Cookie cookie= WebUtils.getCookie(request,jwtCookie);

        if(cookie!=null){
            return cookie.getValue();
        }
        return null;
    }

    public String getJwtFromHeader(HttpServletRequest request){
        String bearer=request.getHeader("Authorization");
        if(bearer!=null && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }


    public ResponseCookie generateJwtFromCookie(UserDetailsImpl user){
        String jwt=generateToken(user);

        return ResponseCookie.from(jwtCookie,jwt)
                .path("/api")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(24 *60 * 60)
                .build();
    }

    public String generateToken(UserDetailsImpl user){
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("roles",user.getAuthorities().toString())
                .claim("email",user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expirationDate.toMillis()))
                .signWith(key(),Jwts.SIG.HS256)
                .compact();
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cleanCookie = ResponseCookie.from(jwtCookie, null)
                .path("/api")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .maxAge(0)
                .build();
        return cleanCookie;
    }



    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserIdFromToken(String token){
        return Long.valueOf(extractAllClaims(token).getSubject());
    }

    public boolean validateToken(String token){

        try {
            Claims claims=extractAllClaims(token);
            Long userId = Long.valueOf(claims.getSubject());
            Date expirationDate=claims.getExpiration();

            if(userId !=null && expirationDate.after(new Date())){
                return true;
            }
        }
        catch (MalformedJwtException e) {
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
