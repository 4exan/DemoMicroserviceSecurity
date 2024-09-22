package ua.kusakabe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.kusakabe.dto.AuthRR;
import ua.kusakabe.entity.UserCredential;
import ua.kusakabe.repository.UserCredentialRepository;

@Service
public class AuthService {

    private final UserCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserCredentialRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthRR saveUser(AuthRR req) {
        AuthRR res = new AuthRR();
        try{
            UserCredential toSave = new UserCredential();
            toSave.setUsername(req.getUsername());
            toSave.setPassword(passwordEncoder.encode(req.getPassword()));
            toSave.setEmail(req.getEmail());
            toSave.setRole(req.getRole());
            UserCredential saved = repository.save(toSave);
            if(saved.getId() > 0){
                res.setStatusCode(200);
                res.setMessage("User saved successfully!");
                res.setUser(saved);
            } else {
                res.setStatusCode(400);
                res.setMessage("User save failed!");
            }
        }catch (Exception e){
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    public AuthRR generateToken(AuthRR req) {
        AuthRR res = new AuthRR();
        try{
            String token = jwtService.generateToken(req);
            if(token != null){
                res.setToken(token);
                res.setStatusCode(200);
                res.setMessage("Token generated successfully!");
            }
        }catch (Exception e){
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    public AuthRR validateToken(AuthRR req) {
        AuthRR res = new AuthRR();
        try{
            UserCredential tokenBearer = repository.findByUsername(jwtService.extractUsername(req.getToken())).orElseThrow(()-> new RuntimeException("No such user in db!"));
            boolean isValid = jwtService.validateToken(req.getToken(), tokenBearer.getUsername());
            if(isValid){
                res.setStatusCode(200);
                res.setMessage("Token validated successfully!");
            } else {
                res.setStatusCode(400);
                res.setMessage("Token validation failed!");
            }
        }catch (Exception e){
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }

}
