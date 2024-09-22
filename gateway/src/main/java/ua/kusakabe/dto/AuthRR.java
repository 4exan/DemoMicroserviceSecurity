package ua.kusakabe.dto;

public class AuthRR {

    private String token;

    public AuthRR() {
    }

    public AuthRR(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
