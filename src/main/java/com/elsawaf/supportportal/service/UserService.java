package com.elsawaf.supportportal.service;

import com.elsawaf.supportportal.domain.User;
import com.elsawaf.supportportal.exception.domain.EmailExistException;
import com.elsawaf.supportportal.exception.domain.UserNameExistException;
import com.elsawaf.supportportal.exception.domain.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
User register(String firstName , String lastName ,
              String username , String email) throws UserNotFoundException, EmailExistException, UserNameExistException;

List<User> getUsers();
User findUserByUsername(String username);

User findUserByEmail(String email);

User addNewUser(String firstName , String lastName ,
                String username , String email , String role, boolean isNotLocked ,
                boolean isActive , MultipartFile profileImage);

User updateUser(String currentUsername, String newFirstName, String newLastName,
                String newUserName, String newEmail, String role ,
                boolean isActive , boolean isNotLocked , MultipartFile profileImage );
void DeleteUser(Long id);
void resetPassword(String email);
User updateProfileImage(String username , MultipartFile profileImage);
}






