package com.inexture.azureAuth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@GetMapping("group1")
//	@PreAuthorize("hasRole('ROLE_group1')")
	public String group1() {
		return "Hello group 1";
	}
	
	
	@GetMapping("group2")
//	@PreAuthorize("hasRole('ROLE_group1')")
	public String group2() {
		return "Hello group 2";
	}

}
