package app.repository;

import app.model.User;
import java.util.List;

public interface UserRepository {
    User save(User user);
    User findById(int id);
    User findByEmail(String email);
    List<User> findAll();
    boolean update(User user);
    boolean delete(int id);
    boolean updateBalance(int userId, double newBalance);
    boolean existsByEmail(String email);
}