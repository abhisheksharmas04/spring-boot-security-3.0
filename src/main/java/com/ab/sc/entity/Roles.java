package com.ab.sc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity @Table(name = "roles_one")
public class Roles {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String roleName;

}
