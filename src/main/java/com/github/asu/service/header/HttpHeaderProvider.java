package com.github.asu.service.header;

import org.springframework.http.HttpHeaders;

public class HttpHeaderProvider {

    private String accessToken;

    public HttpHeaderProvider(String accessToken) {
        this.accessToken = accessToken;
    }

    public HttpHeaders provide() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/vnd.google-apps.script+json");
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }
}
