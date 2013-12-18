package com.github.asu.mojo.upload;

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

    public void execute() throws MojoExecutionException {
        ScriptFilesUploader uploader = new ScriptFilesUploader();
        uploader.upload(scriptsDir);
    }
}
