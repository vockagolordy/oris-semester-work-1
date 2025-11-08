package app.service;

import app.model.User;
import app.repository.UserRepository;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean updateUserBalance(int userId, double newBalance) {
        return userRepository.updateBalance(userId, newBalance);
    }

    public boolean updateUserProfile(int userId, String name, String email) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
            return userRepository.update(user);
        }
        return false;
    }

    public double getUserBalance(int userId) {
        User user = userRepository.findById(userId);
        return user != null ? user.getBalance() : 0.0;
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
}