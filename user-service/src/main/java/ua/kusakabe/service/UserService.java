package ua.kusakabe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.kusakabe.dto.UserRR;
import ua.kusakabe.entity.User;
import ua.kusakabe.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRR getAllUsers() {
        UserRR res = new UserRR();
        try{
            List<User> allUsers= userRepository.findAll();
            if(!allUsers.isEmpty()){
                res.setUserList(allUsers);
                res.setStatusCode(200);
                res.setMessage("All users loaded successfully!");
            } else {
                res.setStatusCode(404);
                res.setMessage("No users found!");
            }
        }catch (Exception e){
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    public UserRR getUserById(Long id) {
        UserRR res = new UserRR();
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                res.setUser(user);
                res.setStatusCode(200);
                res.setMessage("User loaded successfully!");
            } else {
                res.setStatusCode(404);
                res.setMessage("User not found!");
            }
        } catch (Exception e) {
            res.setStatusCode(500);
            res.setMessage(e.getMessage());
        }
        return res;
    }

}
