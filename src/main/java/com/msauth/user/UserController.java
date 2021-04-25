package com.msauth.user;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	UserService service;
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	
	/**
	 * 
	 * @return Message index
	 */
	@GetMapping("/index")
	public String index() {
		return "Controller User Running...";
	}
	
			
	/**
	 * 
	 * @return List Users
	 */
	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() 
			throws ExecutionException, InterruptedException{	
		
		List<User> listUser = null;
		HashMap<String, String> message = new HashMap<>();
		try {
			listUser = service.findAll();
			if(!listUser.isEmpty()) 
				return ResponseEntity.ok().body(service.findAll());
		} catch (Exception e) {
			message.put("Success", "false");
			message.put("Message", e.getMessage());
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
		message.put("Success", "true");
		message.put("Message", "Não há usuários cadastrados!");
		return ResponseEntity.ok().body(message);
		
	}
	
	
	/**
	 * 
	 * @param email
	 * @return User for email
	 */
	@PostMapping("/users/email")
	public ResponseEntity<?> getByEmailUsers(@RequestBody Email email) 
			throws ExecutionException, InterruptedException {

		User user = null;
		HashMap<String, String> message = new HashMap<>();
		try {
			user = service.findByEmail(email.getEmail());
			if(user.getEmail() == null) {
				message.put("Success", "true");
				message.put("Message", "Não há usuário com este email!");
				return new ResponseEntity<>(message, HttpStatus.OK);
			}
		} catch (Exception e) {
			message.put("Success", "false");
			message.put("Message", e.getMessage());
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
		return ResponseEntity.ok().body(user);
	}
	
	/**
	 * 
	 * @param name
	 * @return User for name
	 */
	@PostMapping("/users/name")
	public ResponseEntity<?> getByNameUsers(@RequestBody Name name) 
			throws ExecutionException, InterruptedException {

		User user = null;
		HashMap<String, String> message = new HashMap<>();
		try {
			user = service.findByName(name.getName());
			if(user.getName() == null) {
				message.put("Success", "true");
				message.put("Message", "Não há usuário com este email!");
				return new ResponseEntity<>(message, HttpStatus.OK);
			}
		} catch (Exception e) {
			message.put("Success", "false");
			message.put("Message", e.getMessage());
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
		return ResponseEntity.ok().body(user);
	}
	
	/**
	 * 
	 * @return Message Access Denied
	 */
	@GetMapping(value = "/access-denied")
	public ResponseEntity<String> accessDenied() {
		String mensage = "Access Denied!!!";
		return ResponseEntity.ok().body(mensage);
	}
	
	/**
	 * 
	 * @param User
	 * @return Status 201
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@PostMapping(value = "/users", headers="Content-Type=multipart/form-data")
	public ResponseEntity<?> save(
			@RequestParam String name, 
			@RequestParam String email, 
			@RequestParam String password, 
			@RequestParam Boolean enable, 
			@RequestParam String role, 
			@RequestParam("file") MultipartFile multipartFile) throws ExecutionException, InterruptedException {
		User user = new User(null, name, email, password, enable, role, null);
		HashMap<String, String> message = new HashMap<>();
		try {
			String pass = user.getPassword();
			user.setPassword(passwordEncoder.encode(pass));
			
			service.save(user, multipartFile);
			
			message.put("Success", "true");
			message.put("Message", "Usuário cadastrado com sucesso!");
			return ResponseEntity.status(HttpStatus.CREATED).body(message);
		} catch (Exception e) {
			message.put("Success", "false");
			message.put("Message", e.getMessage());
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}

	}
	
	
	/**
	 * 
	 * @param ID and User
	 * @param User
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@PutMapping("/users/{id}")
    public ResponseEntity<?> update(@PathVariable String id,  @RequestBody User obj) throws ExecutionException, InterruptedException {
		HashMap<String, String> message = new HashMap<>();
		try {
			String pass = obj.getPassword();
			obj.setPassword(passwordEncoder.encode(pass));
			service.update(id, obj);
			message.put("Success", "true");
			message.put("Message", "Usuário atualizado com sucesso!");
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			message.put("Success", "false");
			message.put("Message", e.getMessage());
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}
	}
	
	
	/**
	 * 
	 * @param ID
	 */
	@DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
		HashMap<String, String> message = new HashMap<>();
		try {
			service.delete(id);
			message.put("Success", "true");
			message.put("Message", "Usuário deletado com sucesso!");
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			message.put("Success", "false");
			message.put("Message", e.getMessage());
 			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
		}		
	}
}
