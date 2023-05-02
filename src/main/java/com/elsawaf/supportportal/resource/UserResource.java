package com.elsawaf.supportportal.resource;


import com.elsawaf.supportportal.constant.FileConstant;
import com.elsawaf.supportportal.constant.SecurityConstant;
import com.elsawaf.supportportal.domain.HttpResponse;
import com.elsawaf.supportportal.domain.User;
import com.elsawaf.supportportal.domain.UserPrincipal;
import com.elsawaf.supportportal.exception.domain.EmailExistException;
import com.elsawaf.supportportal.exception.domain.ExceptionHandling;

import com.elsawaf.supportportal.exception.domain.UserNameExistException;
import com.elsawaf.supportportal.exception.domain.UserNotFoundException;
import com.elsawaf.supportportal.service.UserService;
import com.elsawaf.supportportal.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/","/user"})
public class UserResource extends ExceptionHandling {
    public static final String EMAIL_SENT = "an email with the new password has been sent to ";
    public static final String USER_DELETED_SUCCESSFULLY = "User Deleted Successfully";

    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;
    private final AuthenticationManager authenticationManager ;
    private final JwtTokenProvider jwtTokenProvider;

@PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, EmailExistException, UserNameExistException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail());
        return new ResponseEntity<>(newUser , HttpStatus.OK);
    }




    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("email") String email,
                                           @RequestParam("role") String role,
                                           @RequestParam("isNotLocked") String isNotLocked,
                                           @RequestParam("isActive") String isActive ,
                                           @RequestParam(value = "profileImage" , required = false) MultipartFile profileImage){
        User user = userService.addNewUser(firstName,lastName ,username ,
                email , role , Boolean.parseBoolean(isNotLocked) , Boolean.parseBoolean(isActive) , profileImage);
        return new ResponseEntity<>(user , HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){
        try {
            authenticate(user.getUserName(),user.getPassword());
        } catch (BadCredentialsException e) {
            // Publish an AuthenticationFailureBadCredentialsEvent event
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
            AuthenticationException exception = new BadCredentialsException("Invalid username or password");
            eventPublisher.publishEvent(new AuthenticationFailureBadCredentialsEvent(authRequest, exception));
            // Rethrow the exception to propagate it up the call stack
            throw e;
        }
        User loginUser = userService.findUserByUsername(user.getUserName());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeaders = getJwtHeaders(userPrincipal);
        return new ResponseEntity<>(loginUser , jwtHeaders , HttpStatus.OK);
    }


    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestParam("currentUsername") String currentUsername,
                                           @RequestParam("newFirstName") String newFirstName,
                                           @RequestParam("newLastName") String newLastName,
                                           @RequestParam("newUserName")  String newUserName,
                                           @RequestParam("newEmail") String newEmail,
                                           @RequestParam("role") String role ,
                                           @RequestParam("isActive") String isActive ,
                                           @RequestParam("isNotLocked") String isNotLocked ,
                                           @RequestParam(value = "profileImage" , required = false) MultipartFile profileImage){

        User UpdatedUser = userService.updateUser(currentUsername ,newFirstName , newLastName ,
                newUserName , newEmail , role , Boolean.parseBoolean(isActive) , Boolean.parseBoolean(isNotLocked) ,profileImage);
        return new ResponseEntity<>(UpdatedUser , HttpStatus.OK);
    }


    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username){
        User user = userService.findUserByUsername(username);
        return new ResponseEntity<>(user , HttpStatus.OK);
    }

    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email){
        userService.resetPassword(email);
        return response(HttpStatus.OK , EMAIL_SENT +email);
    }


    private HttpHeaders getJwtHeaders(UserPrincipal userPrincipal) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(SecurityConstant.JWT_TOKEN_HEADER , jwtTokenProvider.generateJwtToken(userPrincipal));
        return httpHeaders;
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users , HttpStatus.OK);
    }






    @DeleteMapping("/delete/{id}")
@PreAuthorize("hasAnyAuthority('user:delete')")
 public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") Long id){
    userService.DeleteUser(id);
    return response(HttpStatus.NO_CONTENT, USER_DELETED_SUCCESSFULLY);
 }


 @PostMapping("/updateProfileImage")
 public ResponseEntity<User> updateProfileImage(  @RequestParam("newUserName")  String userName,
                                                  @RequestParam( "profileImage" ) MultipartFile profileImage){
    User user = userService.updateProfileImage(userName,profileImage);
    return new ResponseEntity<>(user , HttpStatus.OK);

 }


 @SneakyThrows
 @GetMapping(path = "/image/{username}/{filename}" , produces = IMAGE_JPEG_VALUE)
public byte[] getProfileImage( @PathVariable("username") String username ,
                               @PathVariable("filename") String filename){
    return Files.readAllBytes(Paths.get(FileConstant.USER_FOLDER +
            username + FileConstant.FORWARD_SLASH + filename));
 }




 @SneakyThrows
 @GetMapping(path = "/image/{profile}/{username}" , produces = IMAGE_JPEG_VALUE)
public byte[] getTempProfileImage(@PathVariable("username") String username, @PathVariable("profile") String profile){
     URL url = new URL(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + username);
     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     try (InputStream inputStream = url.openStream()){
         int bytesRead;
         byte[] chunk = new byte[1024];
         while ((bytesRead = inputStream.read(chunk))>0){
             byteArrayOutputStream.write(chunk,0,bytesRead);
         }

     }
     return byteArrayOutputStream.toByteArray();
 }


    private void authenticate(String userName, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName , password));
    }



    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        final HttpResponse body = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase()
                , message.toUpperCase());
        return new ResponseEntity<>(body, httpStatus);
    }

}
