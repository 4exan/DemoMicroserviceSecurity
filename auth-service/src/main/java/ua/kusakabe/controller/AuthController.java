package ua.kusakabe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.kusakabe.dto.AuthRR;
import ua.kusakabe.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthRR> addNewUser(@RequestBody AuthRR req) {
        return ResponseEntity.ok(authService.saveUser(req));
    }

    @PostMapping("/token")
    public ResponseEntity<AuthRR> getToken(@RequestBody AuthRR req) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        if(authentication.isAuthenticated()) {
            return ResponseEntity.ok(authService.generateToken(req));
        } else {
            throw new RuntimeException("Authentication failed");
        }
    }

    @PostMapping("/validate")
    public int validateToken(@RequestBody AuthRR req) {
        authService.validateToken(req);
        return authService.validateToken(req).getStatusCode();
    }

}
