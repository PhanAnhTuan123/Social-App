package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;
    public List<User>listAll(){
        return repository.findAll();
    }
    public List<Role>listRoles(){
            return roleRepository.findAll();
    }

    public void save(User user) {
        repository.save(user);
    }
}
