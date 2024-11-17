package com.waleed.dreamshops.service.user;

import com.waleed.dreamshops.dto.UserDto;
import com.waleed.dreamshops.model.User;
import com.waleed.dreamshops.request.CreateUserRequest;
import com.waleed.dreamshops.request.UserUpdateRequest;

public interface IUserService {
	User getUserById(Long userId);
	User createUser(CreateUserRequest request);
	User updateUser(UserUpdateRequest request, Long userId);
	void deleteUser(Long userId);
	UserDto convertUserToDto(User user);
	User getAuthenticatedUser();

}
