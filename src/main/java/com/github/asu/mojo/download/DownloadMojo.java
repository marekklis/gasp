package com.github.asu.mojo.download;

import com.github.asu.download.ScriptFilesDownloader;
import com.github.asu.header.HttpHeadersProvider;
import com.github.asu.rest.RestTemplateProvider;
import com.github.asu.scriptfile.ScriptFile;
import com.github.asu.scriptfile.ScriptFileSaver;
import com.github.asu.scriptfile.ScriptFiles;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "download")
public class DownloadMojo extends AbstractMojo {

    @Parameter(property = "downloadDir", required = true)
    private File downloadDir;
    @Parameter(property = "projectId", required = true)
    private String projectId;
    @Parameter(property = "accessToken", required = true)
    private String accessToken;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        RestTemplateProvider restTemplateProvider = new RestTemplateProvider();
        HttpHeadersProvider headersProvider = new HttpHeadersProvider(accessToken);
        ScriptFilesDownloader downloader = new ScriptFilesDownloader(restTemplateProvider, headersProvider);
        ScriptFileSaver saver = new ScriptFileSaver();
        ScriptFiles scriptFiles = downloader.download(projectId);
        for (ScriptFile scriptFile : scriptFiles.getFiles()) {
            saver.save(downloadDir, scriptFile);
        }
    }
}