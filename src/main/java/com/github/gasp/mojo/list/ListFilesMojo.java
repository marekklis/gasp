package com.github.gasp.mojo.list;

import com.github.gasp.download.ScriptFilesDownloader;
import com.github.gasp.header.HttpHeadersProvider;
import com.github.gasp.rest.RestTemplateProvider;
import com.github.gasp.scriptfile.ScriptFile;
import com.github.gasp.scriptfile.ScriptFiles;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "list")
public class ListFilesMojo extends AbstractMojo {

    @Parameter(property = "projectId", required = true)
    private String projectId;
    @Parameter(property = "accessToken", required = true)
    private String accessToken;

    public void execute() throws MojoExecutionException {
        RestTemplateProvider restTemplateProvider = new RestTemplateProvider();
        HttpHeadersProvider headersProvider = new HttpHeadersProvider(accessToken);
        ScriptFilesDownloader downloader = new ScriptFilesDownloader(restTemplateProvider, headersProvider);
        ScriptFiles files = downloader.download(projectId);
        for (ScriptFile file : files.getFiles()) {
            getLog().info(file.getName() + "." + file.getFileType().getExtension());
        }
    }

}