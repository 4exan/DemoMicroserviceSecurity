package ua.kusakabe.dto;

import jakarta.persistence.Entity;
import ua.kusakabe.entity.UserCredential;

public class AuthRR {

    private int statusCode;
    private String message;
    private String token;
    private String expirationDate;

    private String username;
    private String password;
    private String email;
    private String role;

    private UserCredential user;

    public AuthRR() {
    }

    public AuthRR(int statusCode, String message, String token, String expirationDate, String username, String password, String email, String role, UserCredential user) {
        this.statusCode = statusCode;
        this.message = message;
        this.token = token;
        this.expirationDate = expirationDate;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.user = user;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserCredential getUser() {
        return user;
    }

    public void setUser(UserCredential user) {
        this.user = user;
    }
}
