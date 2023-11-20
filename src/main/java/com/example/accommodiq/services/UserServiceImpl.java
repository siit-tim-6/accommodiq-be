package com.example.accommodiq.services;

import com.example.accommodiq.domain.User;
import com.example.accommodiq.repositories.UserRepository;
import com.example.accommodiq.services.interfaces.IUserService;
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

    final
    UserRepository allUsers;

    ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    public UserServiceImpl(UserRepository allUsers) {
        this.allUsers = allUsers;
    }

    @Override
    public Collection<User> getAll() {
        System.out.println(allUsers.findAll());
        return allUsers.findAll();
    }

    @Override
    public User findUser(Long UserId) {
        Optional<User> found = allUsers.findById(UserId);
        if (found.isEmpty()) {
            String value = bundle.getString("userNotFound");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
        }
        return found.get();
    }

    @Override
    public User insert(User User) {
        try {
            allUsers.save(User);
            allUsers.flush();
            return User;
        } catch (ConstraintViolationException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "error");
        }
    }

    @Override
    public User update(User User) {
        try {
            findUser(User.getId()); // this will throw ResponseStatusException if User is not found
            allUsers.save(User);
            allUsers.flush();
            return User;
        } catch (RuntimeException ex) {
            Throwable e = ex;
            Throwable c = null;
            while ((e != null) && !((c = e.getCause()) instanceof ConstraintViolationException) ) {
                e = c;
            }
            if (c != null) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "error2");
            throw ex;
        }
    }

    @Override
    public User delete(Long UserId) {
        User found = findUser(UserId); // this will throw UserNotFoundException if User is not found
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
