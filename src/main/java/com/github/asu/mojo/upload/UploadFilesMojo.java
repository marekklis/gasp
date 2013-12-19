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
        ScriptFilesLister filesLister = new ScriptFilesLister(new RestTemplateProvider(), new HttpHeaderProvider(accessToken));
        ScriptFileBuilder scriptFileBuilder = new ScriptFileBuilder();
        ScriptFilesUploader uploader = new ScriptFilesUploader(new RestTemplateProvider(), new HttpHeaderProvider(accessToken), filesLister, projectId, scriptFileBuilder);
        uploader.upload(scriptsDir);
    }
}
