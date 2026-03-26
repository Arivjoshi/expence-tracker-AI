package services;

import models.User;
import utils.FileManager;

import java.util.List;
import java.util.Optional;

public class UserService {
    private List<User> users;

    public UserService() {
        this.users = FileManager.loadUsers();
    }

    public boolean register(String username, String password, double monthlyBudget) {
        if (findByUsername(username).isPresent()) {
            return false;
        }
        User user = new User(username, password, monthlyBudget);
        users.add(user);
        FileManager.saveUsers(users);
        return true;
    }

    public Optional<User> login(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
}
