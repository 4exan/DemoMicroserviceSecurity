package ua.kusakabe.dto;

public class AuthRR {

    /*
    Created only for filter purpose, because i sent POST request to auth-service and i need ->
    -> JSON body to hold token for basic secure purposes.
     */

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
