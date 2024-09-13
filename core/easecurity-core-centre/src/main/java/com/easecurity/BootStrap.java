package com.easecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.easecurity.core.basis.ServerService;
import com.easecurity.core.basis.b.User;
import com.easecurity.core.basis.b.UserRepository;

/**
 * 启动执行
 *
 */
@Component
public class BootStrap implements CommandLineRunner {
    @Autowired
    private ServerService serverService;
    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
//        serverService.serverStarte();
        User user = userRepository.findByAccount("admin");
        user.getAcStatus();
        user.getPd();
    }
}
