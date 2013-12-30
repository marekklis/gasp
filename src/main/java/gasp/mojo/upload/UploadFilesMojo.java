package gasp.mojo.upload;

import gasp.download.ScriptFilesDownloader;
import gasp.header.HttpHeadersProvider;
import gasp.rest.RestTemplateProvider;
import gasp.scriptfile.ScriptFileBuilder;
import gasp.upload.ScriptFilesUploader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

@Mojo(name = "upload")
public class UploadFilesMojo extends AbstractMojo {

    @Parameter(property = "scriptsDir", required = true)
    private File scriptsDir;
    @Parameter(property = "ignoredFiles", required = false)
    private List<String> ignoredFiles;
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
        uploader.upload(scriptsDir, ignoredFiles);
        getLog().info("----- excluded files ------");
        for (String ignoredFile : ignoredFiles) {
            getLog().info(ignoredFile);
        }
        getLog().info("---------------------------");
    }
}
