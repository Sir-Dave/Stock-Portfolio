package com.sirdave.portfolio.user;

import com.sirdave.portfolio.auth.UserResponse;
import com.sirdave.portfolio.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    User getCurrentLoggedUser(HttpServletRequest request,
                              JwtTokenUtil jwtTokenUtil,
                              UserService userService){

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = header.split(" ")[1].trim();

        String id = jwtTokenUtil.getUserId(token);
        System.out.println("User Controller: User id is " + id);
        return userService.getUserById(Long.parseLong(id));
    }
}
