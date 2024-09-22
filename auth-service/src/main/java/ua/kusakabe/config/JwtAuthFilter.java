package ua.kusakabe.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.kusakabe.dto.AuthRR;
import ua.kusakabe.entity.UserCredential;
import ua.kusakabe.service.JwtService;
import ua.kusakabe.service.UserCredentialsService;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserCredentialsService userCredentialsService;

    @Autowired
    public JwtAuthFilter(JwtService jwtService, UserCredentialsService userCredentialsService) {
        this.jwtService = jwtService;
        this.userCredentialsService = userCredentialsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;
        if (authHeader == null || authHeader.isBlank()) {   //If Header is blank ->
            filterChain.doFilter(request, response);    // -> do default filter.
            return;
        } //Else
        jwtToken = authHeader.substring(7); //Substring 'Bearer ' from token
        username = jwtService.extractUsername(jwtToken);    //Extract username from token

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {   //If username is present and user is not authenticated
            AuthRR req = new AuthRR();
            var userDetails = userCredentialsService.loadUserByUsername(username);  //Get user from db with token.username
            req.setUsername(userDetails.getUsername());
            req.setToken(jwtToken);
            if (jwtService.validateToken(jwtToken, userDetails.getUsername())) {  //If token is valid
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();   //Create empty context
                //Create new usernamePasswordAuth token and set to token: username and roles;
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);   //Give token to security context
                SecurityContextHolder.setContext(securityContext);  //Set context holder
            }
        }
        filterChain.doFilter(request, response);    //Do filter
    }
}
