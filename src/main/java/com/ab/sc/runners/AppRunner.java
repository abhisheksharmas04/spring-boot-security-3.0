package com.ab.sc.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ab.sc.dao.IUserDAO;
import com.ab.sc.entity.Users;

import lombok.AllArgsConstructor;

@AllArgsConstructor
//@Component
public class AppRunner implements CommandLineRunner {
	
	private final IUserDAO userDao;
	private final PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {
		Users user1 = new Users();
		user1.setUserName("abhishek@ranosys.com");
		user1.setUserPassword(encoder.encode("123456"));
		user1.setRole("ADMIN");
		
		Users user2 = new Users();
		user2.setUserName("abhi@ranosys.com");
		user2.setUserPassword("789456");
		user2.setRole("STUDENT");
		
		userDao.save(user1);
		userDao.save(user2);
		
		System.out.println("AppRunner.enclosing_method():: User Saved");

	}

}
