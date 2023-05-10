package com.elsawaf.app.jwtAuth.service.impl;

import com.elsawaf.app.jwtAuth.constant.FileConstant;
import com.elsawaf.app.jwtAuth.constant.UserImplConstant;
import com.elsawaf.app.jwtAuth.domain.User;
import com.elsawaf.app.jwtAuth.domain.UserPrincipal;
import com.elsawaf.app.jwtAuth.enumeration.Role;
import com.elsawaf.app.jwtAuth.domain.EmailExistException;
import com.elsawaf.app.jwtAuth.domain.EmailNotFoundException;
import com.elsawaf.app.jwtAuth.domain.UserNameExistException;
import com.elsawaf.app.jwtAuth.domain.UserNotFoundException;
import com.elsawaf.app.jwtAuth.service.LoginAttemptService;
import com.elsawaf.app.jwtAuth.repository.UserRepository;
import com.elsawaf.app.jwtAuth.service.MailService;
import com.elsawaf.app.jwtAuth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@Service
@Transactional
@Qualifier("userDetailsService")
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService , UserDetailsService {
    private final MailService mailService;
    Logger logger = LoggerFactory.getLogger(getClass());
   private final UserRepository userRepository;
   private final LoginAttemptService loginAttemptService;
   private final BCryptPasswordEncoder bCryptPasswordEncoder;

//@Autowired
//    public UserServiceImpl(UserRepository userRepository
//                            , BCryptPasswordEncoder bCryptPasswordEncoder
//                                , LoginAttemptService loginAttemptService
//                                        , MailService mailService)
//    {
//        this.loginAttemptService = loginAttemptService ;
//        this.userRepository = userRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.mailService=mailService;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameLikeIgnoreCaseAllIgnoreCase(username);
        if (user==null){
            logger.error("Can't find user by User Name :- "+ username);
            logger.info("Inside loadUserByUsername");
            throw new UsernameNotFoundException(UserImplConstant.USER_NAME_NOT_FOUND + username);
        }
        else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            logger.info("User Founded by User Name :- "+ username);
            logger.info("Inside loadUserByUsername after the else");
            return userPrincipal;
        }

    }

//    private void validateLoginAttempt(User user)  {
//
//    if (user.getIsNotLocked()){
//        logger.info("Inside validateLoginAttempt after knowing that the user is not locked ");
//                if (loginAttemptService.hasExceededMaxAttempts(user.getUserName()))
//                {
//                    logger.info("User Exceeded maximum number of attempts ");
//                    user.setIsNotLocked(false);}
//    else {user.setIsNotLocked(true);}
//                             }
//    else {loginAttemptService.evictUserFromLoginAttempts(user.getUserName());}
//
//    }

    @Override
    public User register(String firstName,
                         String lastName,
                         String username,
                         String email) throws UserNotFoundException, EmailExistException, UserNameExistException {
       validateNewUsernameAndEmail(StringUtils.EMPTY,username,email);
       User user = new User();
       user.setUserId(generateUserId());
       String password = generatePassword();
       String encodedPassword = encodePassword(password);
       user.setEmail(email);
       user.setFirstName(firstName);
       user.setLastName(lastName);
       user.setUserName(username);
       user.setJoinedDate(new Date());
       user.setPassword(encodedPassword);
       user.setIsActive(true);
       user.setIsNotLocked(true);
       user.setRole(Role.ROLE_USER.name());
       user.setAuthorities(Role.ROLE_USER.getAuthorities());
       user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
       userRepository.save(user) ;
       mailService.sendNewPasswordEmail(firstName , password , email);
       logger.info("new user password" + password);
       return user;
    }

    @SneakyThrows
    @Override
    public User addNewUser(String firstName, String lastName,
                           String username, String email, String role,boolean isNotLocked,
                           boolean isActive , MultipartFile profileImage) {
    validateNewUsernameAndEmail(StringUtils.EMPTY,username,email);
    User user = new User();
    String password = generatePassword();
    String encodedPassword = encodePassword(password);
    user.setUserId(generateUserId());
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setJoinedDate(new Date());
    user.setUserName(username);
    user.setPassword(encodedPassword);
    user.setIsActive(isActive);
    user.setIsNotLocked(isNotLocked);
    user.setRole(getRoleEnum(role).name());
    user.setAuthorities(getRoleEnum(role).getAuthorities());
    user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
    userRepository.save(user);
    saveProfileImage(user , profileImage);
        return user;
    }

    @SneakyThrows
    private void saveProfileImage(User user, MultipartFile profileImage) {
    if(profileImage != null){
        Path userFolder = Paths.get(FileConstant.USER_FOLDER +user.getUserName())
                .toAbsolutePath().normalize();
        if (!Files.exists(userFolder)){
            Files.createDirectories(userFolder);
            logger.info("inside path created" + userFolder);
        }
        Files.deleteIfExists(Paths.get(userFolder+user.getUserName() +FileConstant.DOT +FileConstant.JPG_EXTENSION));
        Files.copy(profileImage.getInputStream(),
                userFolder.resolve(user.getUserName() +FileConstant.DOT +FileConstant.JPG_EXTENSION) , REPLACE_EXISTING);
        user.setProfileImageUrl(setProfileImageUrl(user.getUserName()));
        userRepository.save(user);
        logger.info(" logger profile image saved "+ profileImage.getOriginalFilename());


    }
    }

    private String setProfileImageUrl(String userName) {
    return  ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstant.USER_IMAGE_PATH
    + userName + FileConstant.FORWARD_SLASH + userName + FileConstant.DOT + FileConstant.JPG_EXTENSION).toUriString();
    }

    private Role getRoleEnum(String role){
return Role.valueOf(role.toUpperCase());
}
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUserNameLikeIgnoreCaseAllIgnoreCase(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


    @SneakyThrows
    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName,
                           String newUserName, String newEmail, String role ,
                           boolean isActive , boolean isNotLocked , MultipartFile profileImage) {
        validateNewUsernameAndEmail(currentUsername,newUserName,newEmail);
        User updatedUser = validateNewUsernameAndEmail(currentUsername,newUserName,newEmail);
        updatedUser.setIsNotLocked(isNotLocked);
        updatedUser.setIsActive(isActive);
        updatedUser.setUserName(newUserName);
        updatedUser.setFirstName(newFirstName);
        updatedUser.setLastName(newLastName);
        updatedUser.setEmail(newEmail);
        updatedUser.setAuthorities(getRoleEnum(role).getAuthorities());
        updatedUser.setRole(getRoleEnum(role).name());
        userRepository.save(updatedUser);
        saveProfileImage(updatedUser , profileImage );
        return updatedUser;
    }


    @Override
    public void DeleteUser(Long id) {
userRepository.deleteById(id);
    }

    @SneakyThrows
    @Override
    public void resetPassword(String email) {
User user = userRepository.findUserByEmail(email);
if (user == null){
    throw new EmailNotFoundException(UserImplConstant.NO_USER_FOUND_BY_EMAIL + email);
}
String password = generatePassword();
user.setPassword(encodePassword(password));
userRepository.save(user);
mailService.sendNewPasswordEmail(user.getFirstName(),password , user.getEmail());

    }

    @SneakyThrows
    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) {
        User user = validateNewUsernameAndEmail(username,null,null);
  saveProfileImage(user,profileImage);
  return user ;

    }

    private User validateNewUsernameAndEmail(String currentUsername
            , String newUserName
            , String newEmail) throws UserNotFoundException, UserNameExistException, EmailExistException {

        User userByNewUserName = findUserByUsername(newUserName);
        User userByNewEmail = findUserByEmail(newEmail);
    if (StringUtils.isNotBlank(currentUsername)){
        User currentUser = findUserByUsername(currentUsername);

        // I will check to see if the current user is already registered
        if (currentUser==null){
            logger.info("Inside validateNewUsernameAndEmail");
            throw new UserNotFoundException(UserImplConstant.USER_NAME_NOT_FOUND+ currentUsername);
        }
        // now I will check to see if I have a username matching the new username I want to create
        if (userByNewUserName != null && !currentUser.getId().equals(userByNewUserName.getId())){
            logger.info("Inside validateNewUsernameAndEmail");
throw new UserNameExistException(UserImplConstant.USER_NAME_ALREADY_EXIST);
        }
        // I will with email now
        if (userByNewEmail != null && !currentUser.getUserId().equals(userByNewEmail.getEmail())){
            logger.info("Inside validateNewUsernameAndEmail");
            throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXISTS);
        }
        return currentUser;
    } else {
            if (userByNewUserName != null) {
                logger.info("Inside validateNewUsernameAndEmail");
throw new UserNameExistException(UserImplConstant.USER_NAME_ALREADY_EXIST + newUserName);
            }
        if (userByNewEmail != null) {
            logger.info("Inside validateNewUsernameAndEmail");
            throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXISTS + newEmail);

        }
return null;
    }


    }
    private void validateLoginAttempt(User user) {
        if (user.getIsNotLocked()) {
            logger.info("Inside validateLoginAttempt after knowing that the user is not locked");
            if (loginAttemptService.hasExceededMaxAttempts(user.getUserName())) {
                logger.info("User exceeded maximum number of attempts");
                user.setIsNotLocked(false);
                loginAttemptService.evictUserFromLoginAttempts(user.getUserName());
            } else {
                logger.info("Inside  user.setIsNotLocked(true);");
                user.setIsNotLocked(true);
            }
        } else {
            logger.info("Inside  loginAttemptService.evictUserFromLoginAttempts(user.getUserName());");
            loginAttemptService.evictUserFromLoginAttempts(user.getUserName());
        }
    }


    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(FileConstant.DEFAULT_USER_IMAGE_PATH +username).toUriString();
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }
}
