package com.example.accommodiq.services.users;

import com.example.accommodiq.domain.User;
import com.example.accommodiq.repositories.UserRepository;
import com.example.accommodiq.services.interfaces.users.IUserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
public class UserServiceImpl implements IUserService {

    final UserRepository allUsers;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    @Autowired
    public UserServiceImpl(UserRepository allUsers) {
        this.allUsers = allUsers;
    }

    @Override
    public Collection<User> getAll() {
        System.out.println(allUsers.findAll());
        return allUsers.findAll();
    }

    @Override
    public User findUser(Long userId) {
        Optional<User> found = allUsers.findById(userId);
        if (found.isEmpty()) {
            String value = bundle.getString("userNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public User insert(User user) {
        try {
            allUsers.save(user);
            allUsers.flush();
            return user;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be inserted");
        }
    }

    @Override
    public User update(User user) {
        try {
            findUser(user.getId());
            allUsers.save(user);
            allUsers.flush();
            return user;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException)) {
                e = c;
            }
            if ((c != null)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be updated");
            }
            throw ex;
        }
    }

    @Override
    public User delete(Long userId) {
        User found = findUser(userId);
        allUsers.delete(found);
        allUsers.flush();
        return found;
    }

    @Override
    public void deleteAll() {
        allUsers.deleteAll();
        allUsers.flush();
    }
}
