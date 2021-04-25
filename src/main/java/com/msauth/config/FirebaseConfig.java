package com.msauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;


import org.apache.commons.io.IOUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import javax.annotation.PostConstruct;

@Service
public class FirebaseConfig {
		
	private final Environment environment;
	
	public FirebaseConfig (Environment environment) {
		this.environment = environment;
	}
		
    @SuppressWarnings("deprecation")
	@PostConstruct
    public Firestore firestore() throws Exception {
    	
    	InputStream firebaseCredential = createFirebaseCredential();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseCredential))
                .build();
        FirebaseApp.initializeApp(options);
        return FirestoreClient.getFirestore();
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

        //serialize with Jackson
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(firebaseCredential);

        //convert jsonString string to InputStream using Apache Commons
        return IOUtils.toInputStream(jsonString);
    }
}