package com.springboot.API.UserManagementCRUD.repository;

import com.springboot.API.UserManagementCRUD.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    List <User> findByFirstName(String firstName);
    List <User> findByLastName(String lastName);
}
