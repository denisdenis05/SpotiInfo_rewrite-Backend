package com.spotiinfo.server;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;

import static com.spotiinfo.server.SpotifyRequests.getSpotifyUsername;
import static com.spotiinfo.server.SpotifyRequests.getAndSetSpotifyToken;

@RestController
public class ApiController {
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint(HttpSession session) {

        System.out.println("GET request received at /test");
        return ResponseEntity.status(HttpStatus.OK).body("yeah it works"+session.getAttribute("USERNAME"));
    }

    @PostMapping("/setAuthorizationKey")
    public ResponseEntity<LoginInformation> setAuthorizationKey(@RequestBody LoginInformation requestBody, HttpSession session) {
        String identificator = requestBody.getIdentificator();
        System.out.println(identificator);

        getAndSetSpotifyToken(identificator, session);

        if (session.getAttribute("accessToken") != null) {
            String authorizationKey = (String)session.getAttribute("accessToken");

            String username = getSpotifyUsername(authorizationKey);
            if (username != null) {
                session.setAttribute("authenticated", true);
                session.setAttribute("username", username);
                System.out.println("POST request received at /setAuthorizationKey");
                System.out.println(session.getAttribute("accessToken"));
            }
            if (session.getAttribute("authenticated") == null) {
                session.setAttribute("authenticated", false);
                session.setAttribute("username", "placeholder");
                session.setAttribute("accessToken", "placeholder");
            }
        }

        LoginInformation loginInformation = new LoginInformation((Boolean) session.getAttribute("authenticated"), (String) session.getAttribute("username"));
        return ResponseEntity.status(HttpStatus.OK).body(loginInformation);
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
            loginInformation = new LoginInformation((Boolean) session.getAttribute("authenticated"), "placeholder");
        }
        return ResponseEntity.status(HttpStatus.OK).body(loginInformation);
    }

    private URI createSpotifyAuthorizationURI()
    {
        SpotifyApiConfig applicationData = new SpotifyApiConfig();
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(applicationData.CLIENT_ID)
                .setRedirectUri(URI.create(applicationData.REDIRECT_URI))
                .build();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-private,user-read-email,user-top-read,user-read-recently-played")
                .show_dialog(true)
                .build();

        return authorizationCodeUriRequest.execute();
    }

    @GetMapping("/getAuthorizationURI")
    public ResponseEntity<URI> getAuthorizationURI(HttpSession session) {

        URI authorizationUri = createSpotifyAuthorizationURI();
        return ResponseEntity.status(HttpStatus.OK).body(authorizationUri);
    }

    @GetMapping("/getNonSensitiveInformation")
    public ResponseEntity<NonSensitiveInformation> getTokenAndScopes(HttpSession session) {
        SpotifyApiConfig applicationData = new SpotifyApiConfig();
        String clientId = applicationData.CLIENT_ID;
        String[] scopes = applicationData.SCOPES;
        String redirectUri = applicationData.REDIRECT_URI;

        NonSensitiveInformation nonSensitiveInformation = new NonSensitiveInformation(clientId, scopes, redirectUri);
        return ResponseEntity.status(HttpStatus.OK).body(nonSensitiveInformation);
    }

}


class NonSensitiveInformation{
    private String clientId;
    private String[] scopes;
    private String redirectUri;
    public NonSensitiveInformation(String clientId, String[] scopes, String redirectUri){
        this.clientId = clientId;
        this.scopes = scopes;
        this.redirectUri = redirectUri;
    }

    public String[] getScopes() {
        return this.scopes;
    }

    public void setScopes(String[] scopes) {
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
    private String identificator;

    public LoginInformation(boolean isLoggedIn, String username){
        this.isLoggedIn = isLoggedIn;
        this.identificator = username;
    }

    public Boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getIdentificator() {
        return this.identificator;
    }

    public void setIdentificator(String identificator) {
        this.identificator = identificator;
    }

}