# Google Apps Script Plugin

What is it?
--------------------------------------
Google Apps Script Plugin is a maven plugin to allow uploading, downloading and listing google apps script projects.

Setting up
--------------------------------------
### Preparing access token for GASP
- go to https://developers.google.com/oauthplayground/
- Step 1: Select & authorize APIs
  - choose from Drive API: drive, drive.file, drive.scripts
- Click Authorize APIs
- Choose an account (login)
- Step 2: Exchange authorization code for tokens
  - click ``Exchange authorization code for tokens``
  - copy Access Token; if access token expired click ``Refresh access token`` and copy again

### Plugin configuration

```
<plugin>
  <groupId>gasp</groupId>
    <artifactId>gasp-maven-plugin</artifactId>
    <version>0.1-SNAPSHOT</version>
	<configuration>
	  <scriptsDir>${basedir}\src\main\resources</scriptsDir>
	  <downloadDir>${basedir}\backup</downloadDir>
	  <projectId>project_id</projectId>
	  <ignoredFiles>
	    <ignoredFile>ignoredFile.gs</ignoredFile>
	  </ignoredFiles>
	</configuration>
</plugin>
```

- ```scriptsDir``` source directory with script files to uploading
- ```downloadDir``` directory to download script files
- ```projectId``` apps script project ID
- ```ignoredFiles``` list of ignored fi on uploading

### Listing project files
``` mvn gasp:list -DaccessToken=ya29.1access_token_copied_from_oauthplaygrand ```

### Downloading project
``` mvn gasp:download -DaccessToken=ya29.1access_token_copied_from_oauthplaygrand ```

### Uploading project files
``` mvn gasp:upload -DaccessToken=ya29.1access_token_copied_from_oauthplaygrand ```
