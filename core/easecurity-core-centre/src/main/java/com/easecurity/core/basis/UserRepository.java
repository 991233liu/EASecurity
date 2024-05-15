package com.easecurity.core.basis;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easecurity.core.basis.b.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findByAccount(String account);
}
