package com.spotiinfo.server;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.util.Arrays;

@RestController
public class ApiController {
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint(HttpSession session) {
        System.out.println("GET request received at /test");
        return ResponseEntity.status(HttpStatus.OK).body("yeah it works"+session.getAttribute("USERNAME"));
    }

    @PostMapping("/test2")
    public ResponseEntity<String> test2Endpoint(HttpSession session) {
        session.setAttribute("USERNAME", "denis");
        System.out.println("GET request received at /test2");
        return ResponseEntity.status(HttpStatus.OK).body("it's ok, "+session.getAttribute("USERNAME"));
    }

    @GetMapping("/checkIfLoggedIn")
    public ResponseEntity<LoginInformation> checkIfLoggedIn(HttpSession session) {

        Boolean isLoggedIn = Boolean.valueOf((String)session.getAttribute("isLoggedIn"));
        LoginInformation loginInformation;
        if(isLoggedIn!=null && isLoggedIn){
            String username = (String) session.getAttribute("username");
            loginInformation = new LoginInformation(isLoggedIn, username);
        }
        else{
            loginInformation = new LoginInformation(false, "placeholder");
        }
        return ResponseEntity.status(HttpStatus.OK).body(loginInformation);
    }

    private void createSpotifyAuthorizationURI()
    {

    }

    @GetMapping("/getAuthorizationURI")
    public ResponseEntity<URI> getAuthorizationURI(HttpSession session) {

        SpotifyApiConfig applicationData = new SpotifyApiConfig();

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(applicationData.CLIENT_ID)
                .setRedirectUri(URI.create(applicationData.REDIRECT_URI))
                .build();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-private,user-read-email,user-top-read,user-read-recently-played")
                .show_dialog(true)
                .build();

        URI authorizationUri = authorizationCodeUriRequest.execute();

        return ResponseEntity.status(HttpStatus.OK).body(authorizationUri);
    }

    @GetMapping("/getNonSensitiveInformation")
    public ResponseEntity<NonSensitiveInformation> getTokenAndScopes(HttpSession session) {
        SpotifyApiConfig applicationData = new SpotifyApiConfig();
        String clientId = applicationData.CLIENT_ID;
        String scopes = applicationData.SCOPES;
        String redirectUri = applicationData.SCOPES;

        NonSensitiveInformation nonSensitiveInformation = new NonSensitiveInformation(clientId, scopes, redirectUri);
        return ResponseEntity.status(HttpStatus.OK).body(nonSensitiveInformation);
    }

}


class NonSensitiveInformation{
    private String clientId;
    private String scopes;
    private String redirectUri;
    public NonSensitiveInformation(String clientId, String scopes, String redirectUri){
        this.clientId = clientId;
        this.scopes = scopes;
        this.redirectUri = redirectUri;
    }

    public String getScopes() {
        return this.scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getRedirectUri() {
        return this.redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}

class LoginInformation{
    private Boolean isLoggedIn;
    private String username;

    public LoginInformation(boolean isLoggedIn, String username){
        this.isLoggedIn = isLoggedIn;
        this.username = username;
    }

    public Boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}