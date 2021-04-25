package com.msauth.security;


import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.msauth.user.User;
import com.msauth.user.UserService;


@Service
public class UserPrincipalDetailsService implements UserDetailsService{

	@Autowired
	UserService repository;
	
	/* Search the database by email and switch to UserDetails */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = new User();
		try {
			user = repository.findByEmail(email);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		MyUserDetails userPrincipal = new MyUserDetails(user);

        return userPrincipal;		
		
	}	

}
