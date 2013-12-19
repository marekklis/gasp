package com.github.asu.list;

import com.github.asu.header.HttpHeadersProvider;
import com.github.asu.rest.RestTemplateProvider;
import com.github.asu.scriptfile.ScriptFiles;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

public class ScriptFilesLister {

    private RestTemplateProvider restTemplateProvider;
    private HttpHeadersProvider headersProvider;

    public ScriptFilesLister(RestTemplateProvider restTemplateProvider, HttpHeadersProvider headersProvider) {
        this.restTemplateProvider = restTemplateProvider;
        this.headersProvider = headersProvider;
    }

    public ScriptFiles listFiles(String projectId) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(headersProvider.provide());
        ResponseEntity<ScriptFiles> responseEntity =
                restTemplateProvider.provide().exchange(listPath(projectId), HttpMethod.GET, requestEntity, ScriptFiles.class);
        return responseEntity.getBody();
    }

    private URI listPath(String projectId) {
        try {
            return new URI("https://script.google.com/feeds/download/export?id=" + projectId + "&format=json");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
