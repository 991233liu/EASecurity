/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis.b;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * user的数据库Repository
 *
 */
public interface UserRepository extends JpaRepository<User, String> {

    User findByAccount(String account);
}
