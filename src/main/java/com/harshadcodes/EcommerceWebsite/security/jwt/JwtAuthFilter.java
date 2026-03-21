package com.harshadcodes.EcommerceWebsite.security.jwt;


import com.harshadcodes.EcommerceWebsite.security.services.UserDetailsImpl;
import com.harshadcodes.EcommerceWebsite.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserDetailsServiceImpl userDetailsService;

    private static final Logger logger= LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.debug("Auth filter called for URI: {}",request.getRequestURI());

        try{
            String token=jwtUtils.getJwtTokenFromHeader(request);

            if(token!=null){
                String username=jwtUtils.extractUsernameFromToken(token);

                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                    UserDetailsImpl userDetails=(UserDetailsImpl) userDetailsService.loadUserByUsername(username);

                    if(jwtUtils.validateToken(token,userDetails)){

                        UsernamePasswordAuthenticationToken authenticationToken=
                                new UsernamePasswordAuthenticationToken(userDetails
                                        ,null
                                        ,userDetails.getAuthorities());
                        authenticationToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    }
                }
            }
        }
        catch (JwtException e) {
            logger.error("JWT error: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
