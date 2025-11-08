package app.service;

import app.exception.AuthenticationException;
import app.model.Session;
import app.model.User;
import app.repository.UserRepository;
import app.repository.SessionRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final Base64.Encoder base64Encoder;
    private final Duration sessionDuration;

    public SecurityServiceImpl(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.base64Encoder = Base64.getEncoder();
        this.sessionDuration = Duration.ofHours(24);
    }

    @Override
    public String registerUser(String name, String email, String password, String passwordRepeat) {
        if (!password.equals(passwordRepeat)) {
            throw new AuthenticationException("Пароли не совпадают");
        }

        // Проверяем, нет ли уже пользователя с таким email
        if (userRepository.findByEmail(email) != null) {
            throw new AuthenticationException("Пользователь с таким email уже существует");
        }

        String salt = UUID.randomUUID().toString();
        String saltedPassword = password + salt;
        String passwordHash = getPasswordHash(saltedPassword);

        User user = new User(name, email, passwordHash, salt);
        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            throw new AuthenticationException("Ошибка при создании пользователя");
        }

        String sessionId = UUID.randomUUID().toString();
        sessionRepository.addSession(savedUser.getId(), sessionId, LocalDateTime.now().plus(sessionDuration));

        return sessionId;
    }

    @Override
    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new AuthenticationException("Пользователь не найден");
        }

        String saltedPassword = password + user.getSalt();
        String passwordHash = getPasswordHash(saltedPassword);

        if (!passwordHash.equals(user.getPasswordHash())) {
            throw new AuthenticationException("Неверный пароль");
        }

        String sessionId = UUID.randomUUID().toString();
        sessionRepository.addSession(user.getId(), sessionId, LocalDateTime.now().plus(sessionDuration));

        return sessionId;
    }

    @Override
    public User getUser(String sessionId) {
        Session session = sessionRepository.findBySessionId(sessionId);
        if (session == null || session.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Сессия недействительна");
        }

        User user = userRepository.findById(session.getUserId());
        if (user == null) {
            throw new AuthenticationException("Пользователь не найден");
        }

        return user;
    }

    @Override
    public void logoutUser(String sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }

    private String getPasswordHash(String saltedPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordHashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return base64Encoder.encodeToString(passwordHashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}