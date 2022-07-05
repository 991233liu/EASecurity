/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.easecurity.core.basis.ServerService;

/**
 * 启动执行
 *
 */
@Component
public class BootStrap {
    @Autowired
    ServerService serverService;

    public void init() {
	serverService.serverStarte();
    }
}
