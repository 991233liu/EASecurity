package com.easecurity.core.basis.s;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DbClientRepository extends JpaRepository<DbClient, String> {

    DbClient findBySkey(String skey);
}
