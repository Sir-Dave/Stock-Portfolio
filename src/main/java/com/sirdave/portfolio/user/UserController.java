package com.sirdave.portfolio.user;

import com.sirdave.portfolio.auth.ResetPasswordRequest;
import com.sirdave.portfolio.auth.UserResponse;
import com.sirdave.portfolio.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/auth/user/profile")
public class UserController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil,
                          UserService userService){
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<UserResponse<?>> getMyProfile(HttpServletRequest request){
        User user = getCurrentLoggedUser(request, jwtTokenUtil, userService);
        if (user != null){
            UserResponse<?> response = new UserResponse<>(true, user, null);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        }
        UserResponse<?> response = new UserResponse<>(false, "You have to log in first", null);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @PutMapping
    ResponseEntity<UserResponse<?>> updateUserProfile(
            @RequestParam (required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam (required = false) String phoneNumber,
            HttpServletRequest request) {

        User user = getCurrentLoggedUser(request, jwtTokenUtil, userService);
        if (user != null) {
            userService.updateUserProfile(user.getId(), firstName, lastName, phoneNumber);
            UserResponse<?> response = new UserResponse<>(true,
                    "Successfully updated details", null);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        }
        else{
            UserResponse<?> response = new UserResponse<>(false, "You have to log in first", null);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("resetPassword")
    ResponseEntity<UserResponse<?>> updateUserPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest,
            HttpServletRequest request) {

        User user = getCurrentLoggedUser(request, jwtTokenUtil, userService);

        if (user != null) {

            // is the old password entered correctly?
            if (!userService.arePasswordsSame(
                    resetPasswordRequest.getOldPassword(), user.getPassword())){
                UserResponse<?> response = new UserResponse<>(false,
                        "Incorrect password", null);
                return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            // is the user setting the password to the previous one?
            if (userService.arePasswordsSame(
                    resetPasswordRequest.getNewPassword(), user.getPassword())){
                UserResponse<?> response = new UserResponse<>(false,
                        "This password has been used before. Please use a different one.", null);
                return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            // confirm the new password
            if (!userService.doPasswordsMatch(
                    resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmPassword())){
                UserResponse<?> response = new UserResponse<>(false, "Passwords do not match", null);
                return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }

            // update the password
            userService.changeUserPassword(user.getId(), resetPasswordRequest.getNewPassword());
            UserResponse<?> response = new UserResponse<>(true,
                    "Password successfully updated", null);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        }
        else{
            UserResponse<?> response = new UserResponse<>(false, "You have to log in first", null);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
    }



    User getCurrentLoggedUser(HttpServletRequest request,
                              JwtTokenUtil jwtTokenUtil,
                              UserService userService){

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = header.split(" ")[1].trim();

        String id = jwtTokenUtil.getUserId(token);
        return userService.getUserById(Long.parseLong(id));
    }
}
