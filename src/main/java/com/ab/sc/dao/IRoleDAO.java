package com.ab.sc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ab.sc.entity.Roles;

@Repository
public interface IRoleDAO extends JpaRepository<Roles,String>{

}
