package com.msauth.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.msauth.config.FirebaseCredentials;

@Service
public class StorageService {

	private final Environment environment;
	@Value("${BUCKET_NAME}")
	private String bucketName;
		
	
	public StorageService (Environment environment) {
		this.environment = environment;
	}
	
	private String uploadFile(File file, String fileName) throws Exception {
		InputStream firebaseCredential = createFirebaseCredential();

		
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(firebaseCredential);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format("https://firebasestorage.googleapis.com/v0/b/"+bucketName+"/o/%s?alt=media", URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
	
    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public Object upload(MultipartFile multipartFile) {

        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name. 

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String TEMP_URL = this.uploadFile(file, fileName);                            // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            return TEMP_URL;                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }
    
    public String download(String fileName) throws Exception {
    	InputStream firebaseCredential = createFirebaseCredential();
    	
        String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));// to set random strinh for destination file name
        String destFilePath = "~/Downloads/" + destFileName; // to set destination file path
        
        Credentials credentials = GoogleCredentials.fromStream(firebaseCredential);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        blob.downloadTo(Paths.get(destFilePath));
        return "Download com sucesso!!!";
    }

    @SuppressWarnings("unused")
	public String delete(String fileName) throws Exception {
    	InputStream firebaseCredential = createFirebaseCredential();
    	
    	Credentials credentials = GoogleCredentials.fromStream(firebaseCredential);
    	Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    	try {
    		boolean blob = storage.delete(BlobId.of(bucketName, fileName));
    		return "Deleted com sucesso!!!";
		} catch (Exception e) {
			return "ERROR: " + e.getMessage();
		}    	
    }

    @SuppressWarnings("deprecation")
	public InputStream createFirebaseCredential() throws Exception {

        FirebaseCredentials firebaseCredential = new FirebaseCredentials();
        
        String privateKey = environment.getRequiredProperty("PRIVATE_KEY").replace("\\n", "\n");
        
        firebaseCredential.setType(environment.getRequiredProperty("TYPE"));
        firebaseCredential.setProject_id(environment.getRequiredProperty("PROJECT_ID"));
        firebaseCredential.setPrivate_key_id("PRIVATE_KEY_ID");
        firebaseCredential.setPrivate_key(privateKey);
        firebaseCredential.setClient_email(environment.getRequiredProperty("CLIENT_EMAIL"));
        firebaseCredential.setClient_id(environment.getRequiredProperty("CLIENT_ID"));
        firebaseCredential.setAuth_uri(environment.getRequiredProperty("AUTH_URI"));
        firebaseCredential.setToken_uri(environment.getRequiredProperty("TOKEN_URI"));
        firebaseCredential.setAuth_provider_x509_cert_url(environment.getRequiredProperty("AUTH_PROVIDER_X509_CERT_URL"));
        firebaseCredential.setClient_x509_cert_url(environment.getRequiredProperty("CLIENT_X509_CERT_URL"));
        bucketName = environment.getRequiredProperty("BUCKET_NAME");
        
        //serialize with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(firebaseCredential);
//		return jsonString = mapper.writeValueAsString(firebaseCredential);

        //convert jsonString string to InputStream using Apache Commons
        return IOUtils.toInputStream(jsonString);
    }
}
