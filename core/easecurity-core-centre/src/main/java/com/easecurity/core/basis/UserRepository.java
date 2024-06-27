package com.easecurity.core.basis;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<DbUser, String> {

    DbUser findByAccount(String account);
}
