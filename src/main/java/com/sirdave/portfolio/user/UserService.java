package com.sirdave.portfolio.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {
    private static final String USER_NOT_FOUND
            = "User with email %s not found";

    private static final String USER_ID_NOT_FOUND
            = "User with id %s not found";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND, email))
        );
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(String.format(USER_ID_NOT_FOUND, id)));
    }

    public boolean isUserExistsByEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean doPasswordsMatch(String p1, String p2){
        return p1.equals(p2);
    }

    public User saveUser(User user){
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Transactional
    public void updateUserProfile(Long userId, String firstName, String lastName,
                          String phoneNumber) {

        User user = getUserById(userId);

        if (firstName != null && !user.getFirstName().equals(firstName)){
            user.setFirstName(firstName);
        }

        if (lastName != null && !user.getLastName().equals(lastName)){
            user.setLastName(lastName);
        }

        if(phoneNumber != null && !user.getPhoneNumber().equals(phoneNumber)) {
            user.setPhoneNumber(phoneNumber);
        }
    }

    public boolean arePasswordsSame(String password, String hashedPassword){
        return bCryptPasswordEncoder.matches(password, hashedPassword);
    }

    User changeUserPassword(String password){
        return null;
    }
}
