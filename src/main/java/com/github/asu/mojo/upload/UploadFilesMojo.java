package com.github.asu.mojo.upload;

import com.github.asu.download.ScriptFilesDownloader;
import com.github.asu.header.HttpHeadersProvider;
import com.github.asu.rest.RestTemplateProvider;
import com.github.asu.scriptfile.ScriptFileBuilder;
import com.github.asu.upload.ScriptFilesUploader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

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
        RestTemplateProvider restTemplateProvider = new RestTemplateProvider();
        HttpHeadersProvider headersProvider = new HttpHeadersProvider(accessToken);
        ScriptFilesDownloader downloader = new ScriptFilesDownloader(restTemplateProvider, headersProvider);
        ScriptFileBuilder scriptFileBuilder = new ScriptFileBuilder();
        ScriptFilesUploader uploader = new ScriptFilesUploader(restTemplateProvider, headersProvider, downloader, projectId, scriptFileBuilder);
        uploader.upload(scriptsDir);
    }
}
