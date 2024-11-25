package com.waleed.dreamshops.data;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.waleed.dreamshops.model.Role;
import com.waleed.dreamshops.model.User;
import com.waleed.dreamshops.repository.RoleRepository;
import com.waleed.dreamshops.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
   
    
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
		createDefaultRoleIfNotExists(defaultRoles);
	    createDefaultUserIfNotExists();
		createDefaultAdminIfNotExists();
	
		
	}
	
	private void createDefaultUserIfNotExists() {
		Role userRole = roleRepository.findByName("ROLE_USER").get();
		for(int i = 1 ; i<= 5; i++) {
			String defaultEamil = "user"+i+"@gmail.com";
			if(userRepository.existsByEmail(defaultEamil))
				continue;
			User user = new User();
			user.setFirstName("The User");
			user.setLastName("User"+ i);
			user.setEmail(defaultEamil);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
			System.out.println("Default vet user "+i
					+ " created successfuly.");
		}
	}
	
	private void createDefaultAdminIfNotExists() {
		Role userRole = roleRepository.findByName("ROLE_ADMIN").get();
		for(int i = 1 ; i<= 2; i++) {
			String defaultEamil = "admin"+i+"@gmail.com";
			if(userRepository.existsByEmail(defaultEamil))
				continue;
			User user = new User();
			user.setFirstName("Admin");
			user.setLastName("Admin"+ i);
			user.setEmail(defaultEamil);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
			System.out.println("Default admin user "+i
					+ " created successfuly.");
		}
	}
	
	private void createDefaultRoleIfNotExists(Set<String> roles) {
		roles.stream()
		.filter(role -> roleRepository.findByName(role).isEmpty())
		.map(Role :: new).forEach(roleRepository :: save);
	}
	
	

}
