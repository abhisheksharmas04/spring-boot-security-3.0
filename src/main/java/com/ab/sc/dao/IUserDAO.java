package com.ab.sc.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ab.sc.entity.Users;


@Repository
public interface IUserDAO extends JpaRepository<Users,Integer>{
	public Optional<Users> findByUserName(String userName);

}
