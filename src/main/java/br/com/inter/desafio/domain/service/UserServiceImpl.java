package br.com.inter.desafio.domain.service;

import br.com.inter.desafio.domain.exceptions.EntityNotFoundException;
import br.com.inter.desafio.domain.model.User;
import br.com.inter.desafio.domain.repository.UserRepository;
import br.com.inter.desafio.domain.service.interfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserServiceInterface {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
    }

    @Override
    public User update(Long id, User user) {
        User oldUser = findById(id);
        oldUser.setEmail(user.getEmail());
        oldUser.setName(user.getName());
        return save(oldUser);
    }
}
