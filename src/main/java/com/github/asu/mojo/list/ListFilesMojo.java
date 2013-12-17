package com.github.asu.mojo.list;

import com.github.asu.service.header.HttpHeaderProvider;
import com.github.asu.service.list.ScriptFile;
import com.github.asu.service.list.ScriptFiles;
import com.github.asu.service.list.ScriptFilesLister;
import com.github.asu.service.rest.RestTemplateProvider;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Mojo(name = "list")
public class ListFilesMojo extends AbstractMojo {

    @Parameter(property = "projectId", required = true)
    private String projectId;
    @Parameter(property = "accessToken", required = true)
    private String accessToken;

    public void execute() throws MojoExecutionException {
        RestTemplate restTemplate = new RestTemplateProvider().provide();
        HttpHeaders httpHeaders = new HttpHeaderProvider(accessToken).provide();
        ScriptFilesLister lister = new ScriptFilesLister(restTemplate, httpHeaders);
        ScriptFiles files = lister.listFiles(projectId);
        for (ScriptFile file : files.getFiles()) {
            getLog().info(file.getName());
        }
    }

}
