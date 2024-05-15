package com.easecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.easecurity.core.basis.ServerService;

/**
 * 启动执行
 *
 */
@Component
public class BootStrap implements CommandLineRunner{
    @Autowired
    private ServerService serverService;

    @Override
    public void run(String... args) throws Exception {
        serverService.serverStarte();
    }
}
