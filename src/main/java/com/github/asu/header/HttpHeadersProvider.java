package com.github.asu.header;

import org.springframework.http.HttpHeaders;

public class HttpHeadersProvider {

    private String accessToken;

    public HttpHeadersProvider(String accessToken) {
        this.accessToken = accessToken;
    }

    public HttpHeaders provide() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/vnd.google-apps.script+json");
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }
}
