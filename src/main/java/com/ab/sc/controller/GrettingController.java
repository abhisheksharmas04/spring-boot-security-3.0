package com.ab.sc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class GrettingController {
	
	@GetMapping("/greetings")
	public ResponseEntity<String> sayHello() throws Exception{
		throw new Exception("Exception");
		//return ResponseEntity.ok("Hello From Spring Security 3.0");
	}
	
	@GetMapping("/say-good-bye")
	public ResponseEntity<String> sayGoodBy(){
		return ResponseEntity.ok("By From Spring Security");
	}

}
