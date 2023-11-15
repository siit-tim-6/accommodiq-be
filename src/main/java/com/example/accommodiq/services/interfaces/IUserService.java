package com.example.accommodiq.services.interfaces;

import com.example.accommodiq.domain.User;

import java.util.Collection;

public interface IUserService {
    public Collection<User> getAll();

    public User findUser(Long UserId);

    public User insert(User User);

    public User update(User User);

    public User delete(Long UserId);

    public void deleteAll();
}
