package app.service;

import app.model.User;

public interface SecurityService {
    String registerUser(String name, String email, String password, String passwordRepeat);
    String loginUser(String email, String password);
    User getUser(String sessionId);
    void logoutUser(String sessionId);
}