package com.sirdave.portfolio.auth;

import com.sirdave.portfolio.security.JwtTokenUtil;
import com.sirdave.portfolio.user.User;
import com.sirdave.portfolio.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil,
                          AuthenticationManager authenticationManager,
                          UserService userService){
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("register")
    ResponseEntity<UserResponse<?>> registerUser(@RequestBody RegisterRequest registerRequest){

        // Has a user previously registered with that email address?
        if (userService.isUserExistsByEmail(registerRequest.getEmail())){
            UserResponse<?> response = new UserResponse<>(false, "User with email already exists", null);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        // Do the passwords match?
        if(!userService.doPasswordsMatch(registerRequest.getPassword(),
                registerRequest.getConfirmPassword())){
            UserResponse<?> response = new UserResponse<>(false, "Passwords do not match", null);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        User user = new User(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail(),
                registerRequest.getPhoneNumber(), registerRequest.getPassword());

        userService.saveUser(user);
        UserResponse<?> response = new UserResponse<>(true, "User registered successfully", null);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("login")
    ResponseEntity<?> loginUser(@RequestBody SignInRequest signInRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getEmail(), signInRequest.getPassword())
            );
            User user = (User) authentication.getPrincipal();
            String token = jwtTokenUtil.generateAccessToken(user);
            UserResponse<?> response = new UserResponse<>(true, "User successfully authenticated", token);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(response);
        }
        catch (BadCredentialsException ex){
            System.out.println(ex.getMessage());
            UserResponse<?> response = new UserResponse<>(false, ex.getMessage(), null);
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
    }
}
