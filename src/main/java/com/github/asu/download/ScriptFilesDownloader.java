package com.github.asu.download;

import com.github.asu.header.HttpHeadersProvider;
import com.github.asu.rest.RestTemplateProvider;
import com.github.asu.scriptfile.ScriptFiles;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

public class ScriptFilesDownloader {

    private RestTemplateProvider restTemplateProvider;
    private HttpHeadersProvider headersProvider;

    public ScriptFilesDownloader(RestTemplateProvider restTemplateProvider, HttpHeadersProvider headersProvider) {
        this.restTemplateProvider = restTemplateProvider;
        this.headersProvider = headersProvider;
    }

    public ScriptFiles download(String projectId) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(headersProvider.provide());
        ResponseEntity<ScriptFiles> responseEntity =
                restTemplateProvider.provide().exchange(downloadURI(projectId), HttpMethod.GET, requestEntity, ScriptFiles.class);
        return responseEntity.getBody();
    }

    private URI downloadURI(String projectId) {
        try {
            return new URI("https://script.google.com/feeds/download/export?id=" + projectId + "&format=json");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
