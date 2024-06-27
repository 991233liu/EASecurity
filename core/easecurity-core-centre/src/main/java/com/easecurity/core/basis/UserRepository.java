package com.easecurity.core.basis;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easecurity.core.db.entity.DbUser;

public interface UserRepository extends JpaRepository<DbUser, String> {

    DbUser findByAccount(String account);
}
