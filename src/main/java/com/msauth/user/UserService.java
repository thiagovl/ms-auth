package com.msauth.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

	private static final String COLLECTION_NAME = "users";
	
	@Autowired
	private StorageService service;
	
	/**
	 * 
	 * @return List All Users
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public List<User> findAll() throws ExecutionException, InterruptedException {	
		ApiFuture<QuerySnapshot> future =
				FirestoreClient.getFirestore().collection(COLLECTION_NAME).get(); // .whereEqualTo("timestampDelete", true).get();		
		List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<User> listProduct = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
        	User user = new User();
        	user.setId(document.getId());
        	user.setName(document.getString("name"));
        	user.setEmail(document.getString("email"));
        	user.setEnable(document.getBoolean("enable"));
        	user.setRole(document.getString("role"));
        	user.setImageUrl(document.getString("imageUrl"));
        	listProduct.add(user);  	
        }
        return listProduct;
	}
	
	/**
	 * 
	 * @param email
	 * @return User for email
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public User findByEmail(String email) throws ExecutionException, InterruptedException {
		
		CollectionReference ref = FirestoreClient.getFirestore().collection(COLLECTION_NAME);
		Query query = ref.whereEqualTo("email", email);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		User user = new User();
		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			  user.setId(document.getId());
			  user.setName(document.getString("name"));
			  user.setEmail(document.getString("email"));
			  user.setPassword(document.getString("password"));
			  user.setEnable(document.getBoolean("enable"));
			  user.setImageUrl(document.getString("imageUrl"));
			  user.setRole(document.getString("role"));
		}
		return user;
	}
	
	/**
	 * 
	 * @param name
	 * @return User for name
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public User findByName(String name) throws ExecutionException, InterruptedException {
			
			CollectionReference ref = FirestoreClient.getFirestore().collection(COLLECTION_NAME);
			Query query = ref.whereEqualTo("name", name);
			ApiFuture<QuerySnapshot> querySnapshot = query.get();
			User user = new User();
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				  user.setId(document.getId());
				  user.setName(document.getString("name"));
				  user.setEmail(document.getString("email"));
				  user.setPassword(document.getString("password"));
				  user.setEnable(document.getBoolean("enable"));
				  user.setImageUrl(document.getString("imageUrl"));
				  user.setRole(document.getString("role"));
			}
			return user;
		}
	
	/**
	 * 
	 * @param name
	 * @return User for id
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public User findById(String id) throws ExecutionException, InterruptedException {
			
			CollectionReference ref = FirestoreClient.getFirestore().collection(COLLECTION_NAME);
			Query query = ref.whereEqualTo("id", id);
			ApiFuture<QuerySnapshot> querySnapshot = query.get();
			User user = new User();
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				  user.setId(document.getId());
				  user.setName(document.getString("name"));
				  user.setEmail(document.getString("email"));
				  user.setPassword(document.getString("password"));
				  user.setEnable(document.getBoolean("enable"));
				  user.setImageUrl(document.getString("imageUrl"));
				  user.setRole(document.getString("role"));
			}
			return user;
		}
	
	
	/**
	 * 
	 * @param email
	 * @return User for text
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public User findByText(String textSearch) throws ExecutionException, InterruptedException {
		
		CollectionReference ref = FirestoreClient.getFirestore().collection(COLLECTION_NAME);
		Query query = ref.whereLessThan("name", textSearch);
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		User user = new User();
		for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
			  user.setId(document.getId());
			  user.setName(document.getString("name"));
			  user.setEmail(document.getString("email"));
			  user.setPassword(document.getString("password"));
			  user.setEnable(document.getBoolean("enable"));
			  user.setImageUrl(document.getString("imageUrl"));
			  user.setRole(document.getString("role"));
		}
		return user;
	}
	
	
	/**
	 * 
	 * @param User
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unused")
	public void save(User user, MultipartFile multipartFile) throws ExecutionException, InterruptedException{
		DocumentReference addedDocRef = FirestoreClient.getFirestore().collection(COLLECTION_NAME).document();
		try {
			Object imageUrl = service.upload(multipartFile);
			user.setId(addedDocRef.getId());
			user.setImageUrl(imageUrl.toString());
			ApiFuture<WriteResult> create = addedDocRef.create(user);
			
		} catch (Exception e) {
 			e.getMessage();
		}
	}
	
	
	/**
	 * 
	 * @param ID and User
	 * @param User
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unused")
	public void update(String id, User obj) throws ExecutionException, InterruptedException{
		DocumentReference docRef = (DocumentReference) FirestoreClient.getFirestore().collection(COLLECTION_NAME).document(id);
		ApiFuture<WriteResult> update = docRef.update(
                "name", obj.getName(),
                "password", obj.getPassword(),
                "enable", obj.getEnable(),
                "role", obj.getRole()
        );
	}
	
	
	/**
	 * 
	 * @param ID
	 * @throws Exception 
	 */
	public void delete(String id) throws Exception {
		DocumentReference docRef = (DocumentReference) FirestoreClient.getFirestore().collection(COLLECTION_NAME).document(id);
		User user = findById(id);
		String imageUrl = user.getImageUrl();
		String aux1 = imageUrl.replace("https://firebasestorage.googleapis.com/v0/b/api-ms-security.appspot.com/o/", "");
		String aux2 = aux1.replace("?alt=media", "");
		System.out.print(aux2);
		service.delete(aux2);
		docRef.delete();
	}

}
