package com.spotiinfo.server;

import jakarta.servlet.http.HttpSession;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;

public class SpotifyRequests {

    public static void getAndSetSpotifyToken(String authorizationCode, HttpSession session) {
        SpotifyApiConfig apiData = new SpotifyApiConfig();
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(apiData.CLIENT_ID)
                .setClientSecret(apiData.CLIENT_SECRET)
                .setRedirectUri(URI.create(apiData.REDIRECT_URI))
                .build();

        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(authorizationCode).build();
        try {
            AuthorizationCodeCredentials credentials = authorizationCodeRequest.execute();

            String accessToken = credentials.getAccessToken();
            String refreshToken = credentials.getRefreshToken();
            int expiresIn = credentials.getExpiresIn();
            session.setAttribute("accessToken", accessToken);
            session.setAttribute("refreshToken", refreshToken);
            session.setAttribute("expiresIn", expiresIn);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String getSpotifyUsername(String authorizationKey){
        System.out.println(authorizationKey);
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(authorizationKey)
                .build();

        GetCurrentUsersProfileRequest request = spotifyApi.getCurrentUsersProfile().build();
        try {
            User user = request.execute();

            String displayName = user.getDisplayName();
            System.out.println(displayName);
            return displayName;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }
}
