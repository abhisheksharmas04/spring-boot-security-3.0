package com.ab.sc.dao;

import java.util.ArrayList;
import java.util.Collection;

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
		System.out.println("UserDao.findUserByEmail() " + user.getUserName());
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role->{authorities.add(new SimpleGrantedAuthority(role.getRoleName()));});
		user.getRoles().forEach(roles -> System.out.print(roles.getRoleName()));
		return
			new User(user.getUserName(), user.getPassword(),authorities);
	}

}
