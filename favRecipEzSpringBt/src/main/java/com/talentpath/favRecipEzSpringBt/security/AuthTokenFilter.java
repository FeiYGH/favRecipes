package com.talentpath.favRecipEzSpringBt.security;

import org.springframework.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    //where we decrypt incoming token
    //provide an username and unhashed password. look up username and hash their password and see if
    //it works out
    //this says you've given us back a token, we're going to look for key in header called authorization and token string
    //and then decrypt it, breaking it into several claims. Then get back UserDetails user
    @Value("${favRecipEz.app.jwtsecret}")
    private String secret;

    @Autowired
    UserDetailsServiceImpl detailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException{

            String token = extractJwt(request);


            if(token != null){
                //secret used to decrypt
                //token stores username, IAT (issued at time), etc
                //each of the claims is one of those fields
                Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
                if(token!=null){
                    String userName = claims.getBody().getSubject();
                    UserDetails details = detailsService.loadUserByUsername(userName);
                    UsernamePasswordAuthenticationToken convertedToken =
                            new UsernamePasswordAuthenticationToken(
                                    details,
                                    null,
                                    details.getAuthorities()
                            );
                    convertedToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(convertedToken);
                }

            }else {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
            filterChain.doFilter(request,response);

    }

    private String extractJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

}
