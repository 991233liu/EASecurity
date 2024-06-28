/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easecurity.core.db.entity.DbUser;

/**
 * user的数据库Repository
 *
 */
public interface UserRepository extends JpaRepository<DbUser, String> {

    DbUser findByAccount(String account);
}
