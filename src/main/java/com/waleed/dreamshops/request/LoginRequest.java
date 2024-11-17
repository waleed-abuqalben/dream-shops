package com.waleed.dreamshops.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
//TODO: custom validation
public class LoginRequest {
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;

}
