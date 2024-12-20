package com.revature.project1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.project1.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
