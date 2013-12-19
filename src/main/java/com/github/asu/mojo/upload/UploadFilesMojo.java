package com.github.asu.mojo.upload;

import com.github.asu.service.header.HttpHeaderProvider;
import com.github.asu.service.list.ScriptFilesLister;
import com.github.asu.service.rest.RestTemplateProvider;
import com.github.asu.service.scriptfile.ScriptFileBuilder;
import com.github.asu.service.upload.ScriptFilesUploader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Mojo(name = "upload")
public class UploadFilesMojo extends AbstractMojo {

    @Parameter(property = "scriptsDir", required = true)
    private File scriptsDir;
    @Parameter(property = "projectId", required = true)
    private String projectId;
    @Parameter(property = "accessToken", required = true)
    private String accessToken;

    public void execute() throws MojoExecutionException {
        RestTemplate restTemplate = new RestTemplateProvider().provide();
        HttpHeaders httpHeaders = new HttpHeaderProvider(accessToken).provide();
        ScriptFilesLister filesLister = new ScriptFilesLister(restTemplate, httpHeaders);
        ScriptFileBuilder scriptFileBuilder = new ScriptFileBuilder();
        ScriptFilesUploader uploader = new ScriptFilesUploader(restTemplate, httpHeaders, filesLister, projectId, scriptFileBuilder);
        uploader.upload(scriptsDir);
    }
}
