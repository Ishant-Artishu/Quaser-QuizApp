package com.Qauser.Quaser.Service;

import com.Qauser.Quaser.Entity.Role;
import com.Qauser.Quaser.Entity.User;
import com.Qauser.Quaser.Repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserPersonService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    public UserPersonService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user){
        if(user.getRole() == null){
            user.setRole(Role.USER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public void deleteById(Long id){
        userRepo.deleteById(id);
    }

    public User login(String email, String password) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("INVALID CREDENTIALS!");
        }
        return user;
    }
}
