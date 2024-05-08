/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.easecurity.core.basis.s.Server;
import com.easecurity.core.utils.Locker;
import com.easecurity.util.MBeanUtils;

/**
 * 服务实例相关服务
 *
 */
@Service
@EnableAsync
public class ServerService {

    private static final Logger log = LoggerFactory.getLogger(ServerService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static volatile Server currentServer;

    private String sql = "UPDATE s_server SET last_time = NOW(6) WHERE id = ?";
    private String sql2 = "INSERT INTO s_server_history (id, sid, port, start_time, ip, name, projet_name, other, last_time) SELECT * FROM s_server WHERE projet_name = ? AND last_time < NOW(6)-interval "
            + timeOut * 3 + " SECOND";
    private String sql3 = "DELETE FROM s_server WHERE projet_name = ? AND last_time < NOW(6)-interval " + timeOut * 3 + " SECOND";
    private String sql4 = "SELECT DISTINCT id FROM s_server ORDER BY id";
    private String sql5 = "SELECT DISTINCT sid FROM s_server WHERE projet_name = ?  ORDER BY sid";
    private String sql6 = "INSERT INTO s_server(id, sid, port, start_time, ip, name, projet_name, other, last_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

    @Value("${server.name:no_name}")
    private String serverName;
    /**
     * 应用实例存活心跳间隔
     */
    private static final long timeOut = 5;

    /**
     * 服务实例存活心跳
     */
    // @Async("taskExecutor")
    @Scheduled(fixedRate = timeOut * 1000)
    public void scheduleTask() {
        try {
            // 服务器启动完毕前不进行心跳检测
            if (currentServer != null)
                heartBeats();
        } catch (Exception e) {
            log.error("服务实例心跳检测异常,Server信息如下：id={} sid={} projetName={} name={}", currentServer.id, currentServer.sid, currentServer.projetName, currentServer.name);
            log.error("服务实例心跳检测异常：", e);
        }
    }

    /**
     * 添加当前实例到数据库
     * 
     */
    public void serverStarte() {
        Server server = new Server();
        server.projetName = serverName;

        StringBuffer other = new StringBuffer();
        Map<String, String> jvmInfo = MBeanUtils.showJvmInfo(other);
        MBeanUtils.showMemoryInfo(other);
        MBeanUtils.showSystem(other);
        MBeanUtils.showClassLoading(other);
        MBeanUtils.showCompilation(other);
        MBeanUtils.showThread(other);
        MBeanUtils.showGarbageCollector(other);
        MBeanUtils.showMemoryManager(other);
        MBeanUtils.showMemoryPool(other);
        server.name = jvmInfo.get("jvm_name");
        server.startTime = Long.valueOf(jvmInfo.get("jvm_startTime"));
        server.other = other.toString().trim();

        log.debug(other.toString());
        addServer(server);

        currentServer = server;
    }

    /**
     * 获取当前服务实例的相关信息
     */
    public Server getCurrentServer() {
        return currentServer;
    }

    /**
     * 心跳检测
     */
    private void heartBeats() {
        if (currentServer.id == null)
            throw new RuntimeException("服务器心跳检测时发现当前实例的id为null。");
        int rl = jdbcTemplate.update(sql, currentServer.id);
        if (rl != 1)
            throw new RuntimeException("服务器心跳检测更新实例心跳时间失败。数据库update行数：" + rl);

    }

    private void addServer(Server server) {
        boolean islock = Locker.lockExist(server.projetName + ":start", null);
        if (!islock) {
            log.info("服务器启动出现严重错误：" + server.projetName + ":start:业务锁正在使用....");
            return;
        }
        try {
            // 将同项目心跳不存在的Server转到历史表中
            jdbcTemplate.update(sql2, server.projetName);
            jdbcTemplate.update(sql3, server.projetName);
            // 计算可用的id
            List<Integer> ids = jdbcTemplate.queryForList(sql4, Integer.class);
            server.id = 1;
            if (ids.size() > 0) {
                // 寻找跳号，使用最小跳号
                for (int i = 0; i < ids.size(); i++) {
                    if (i + 1 != ids.get(i)) {
                        server.id = i + 1;
                        break;
                    }
                }
                // 如果没有找到跳号的，则最大号+1
                if (server.id == 1)
                    server.id = ids.get(ids.size() - 1) + 1;
            }
            // 计算可用sid
            ids = jdbcTemplate.queryForList(sql5, Integer.class, server.projetName);
            server.sid = 0;
            if (ids.size() > 0) {
                // 寻找跳号，使用最小跳号
                for (int i = 0; i < ids.size(); i++) {
                    if (i != ids.get(i)) {
                        server.sid = i;
                        break;
                    }
                }
                // 如果没有找到跳号的，则最大号+1
                if (server.sid == 0)
                    server.sid = ids.get(ids.size() - 1) + 1;
            }
            // 保存server
            jdbcTemplate.update(sql6, server.id, server.sid, server.port, server.startTime, server.ip, server.name, server.projetName, server.other);
        } finally {
            Locker.unlock(server.projetName + ":start");
        }
    }
}
