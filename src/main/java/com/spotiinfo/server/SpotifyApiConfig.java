package com.spotiinfo.server;

public class SpotifyApiConfig {

    String CLIENT_ID;
    String CLIENT_SECRET;
    String REDIRECT_URI;

    public SpotifyApiConfig(){
        this.CLIENT_ID = "cda313e2c22d41fca0fd9d6a5d918190";
        this.CLIENT_SECRET = "f18a5e4fb3fd475ba377e48db52bcef2";
        this.REDIRECT_URI = "spotiinforewrite://callback";

    }

    public String getCLIENT_ID() {
        return this.CLIENT_ID;
    }

    public String getCLIENT_SECRET() {
        return this.CLIENT_SECRET;
    }

    public String getREDIRECT_URI() {
        return this.REDIRECT_URI;
    }

}
