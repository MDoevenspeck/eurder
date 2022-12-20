package com.switchfully.eurder.repositories;

import com.switchfully.eurder.model.users.Customer;
import com.switchfully.eurder.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    public Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u where u.role = 'CUSTOMER' and u.id = :userId")
    public Optional<Customer> getCustomerByUserID(@Param("userId") long userId);
}

