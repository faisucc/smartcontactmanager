package com.smart.dao;
import org.springframework.data.jpa.repository.*;
import com.smart.entities.*;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
   public User findByEmail(String email);
}
