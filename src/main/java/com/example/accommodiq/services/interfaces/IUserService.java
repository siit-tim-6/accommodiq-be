package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.User;

import java.util.Collection;

public interface IUserService {
    Collection<User> getAll();

    User findUser(Long UserId);

    User insert(User User);

    User update(User User);

    User delete(Long UserId);

    void deleteAll();
}
