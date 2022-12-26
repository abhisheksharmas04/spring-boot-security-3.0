package com.ab.sc.dao;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ab.sc.entity.Users;

@Component
public class UserDao {
	
	@Autowired
	private IUserDAO userDao;
	
	public UserDetails findUserByEmail(String email) {
		Users user = userDao.findByUserName(email).orElseThrow(() -> new UsernameNotFoundException("No User found with email " + email));
		return
			new User(user.getUserName(), user.getUserPassword(), Collections.singleton(new SimpleGrantedAuthority(user.getRole())));
	}

}
