package fr.framelab.services;

import fr.framelab.models.User;
import fr.framelab.dao.UserDAO;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUser(int userId) {
        return this.userDAO.findById(userId);
    }

    public void saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        try {
            if (this.getUser(user.getId()) != null) {
                this.userDAO.update(user);
            } else {
                this.userDAO.save(user);
            }
        } catch (Exception e) {
            this.userDAO.save(user);
        }
    }

    public void deleteUser(int id) {
        this.userDAO.deleteById(id);
    }
}
