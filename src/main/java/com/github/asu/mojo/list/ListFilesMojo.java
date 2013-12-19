package com.github.asu.mojo.list;

import com.github.asu.service.header.HttpHeadersProvider;
import com.github.asu.service.list.ScriptFilesLister;
import com.github.asu.service.rest.RestTemplateProvider;
import com.github.asu.service.scriptfile.ScriptFile;
import com.github.asu.service.scriptfile.ScriptFiles;
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
        ScriptFilesLister lister = new ScriptFilesLister(restTemplateProvider, headersProvider);
        ScriptFiles files = lister.listFiles(projectId);
        for (ScriptFile file : files.getFiles()) {
            getLog().info(file.getName() + "." + file.getFileType().getExtension());
        }
    }

}
