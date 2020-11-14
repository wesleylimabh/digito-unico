package br.com.inter.desafio.domain.service.interfaces;

import br.com.inter.desafio.domain.model.User;

import java.util.List;

public interface UserServiceInterface {

    User save(User user);

    List<User> findAll();

    void delete(Long id);

    User findById(Long id);

    User update(Long id, User user);

}
