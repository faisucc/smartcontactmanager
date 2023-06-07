package com.smart.dao;
import org.springframework.data.jpa.repository.*;
import com.smart.entities.*;

public interface UserRepository extends JpaRepository<User,Integer> {
   
}
