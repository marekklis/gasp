package com.github.asu.list;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Mojo(name = "list")
public class ListFilesMojo extends AbstractMojo {

    @Parameter(property = "projectId", required = true)
    private String projectId;
    @Parameter(property = "accessToken", required = true)
    private String accessToken;

    public void execute() throws MojoExecutionException {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/vnd.google-apps.script+json");
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
        ResponseEntity<String> responseEntity =
                rest.exchange(listPath(), HttpMethod.GET, requestEntity, String.class);
        getLog().info(responseEntity.getBody());
    }

    private URI listPath() throws MojoExecutionException {
        try {
            return new URI("https://script.google.com/feeds/download/export?id=" + projectId + "&format=json");
        } catch (URISyntaxException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
