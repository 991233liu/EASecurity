package com.easecurity.core.basis.s;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DbClientRepository extends JpaRepository<DbClient, Integer> {

    DbClient findBySkey(String skey);
}
