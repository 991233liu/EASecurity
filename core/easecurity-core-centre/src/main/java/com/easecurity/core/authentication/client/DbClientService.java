package com.easecurity.core.authentication.client;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import com.easecurity.core.basis.s.ApiSecretEnum.Status;
import com.easecurity.core.basis.s.DbClient;
import com.easecurity.core.basis.s.DbClientRepository;

/**
 * token客户端服务
 *
 */
@Service
@EnableScheduling
public class DbClientService implements RegisteredClientRepository {
    private static final Logger log = LoggerFactory.getLogger(DbClientService.class);

    @Autowired
    private DbClientRepository dbClientRepository;
//    @Autowired
//    private DbChannelRepository dbChannelRepository;
//    @Autowired
//    private RedisUtil redisUtil;
    private Map<String, String> apiSecrets = new HashMap<>();

    @Override
    public void save(RegisteredClient registeredClient) {
        // 不允许注册，只能从运维管理平台注册
    }

    @Override
    public RegisteredClient findById(String id) {
        Optional<DbClient> client = dbClientRepository.findById(id);
        if (client != null && client.isPresent())
            return buildRegisteredClient(client.get());
        else
            return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Optional<DbClient> client = dbClientRepository.findById(clientId);
        if (client != null && client.isPresent())
            return buildRegisteredClient(client.get());
        else
            return null;
    }

    public Map<String, String> getAllEnabledClientInfo() {
//        if (apiSecrets == null || apiSecrets.isEmpty())
//            updateClientInfo();
        return apiSecrets;
    }

    public boolean containsKey(String key) {
        return apiSecrets.containsKey(key);
    }

    public String getValue(String key) {
        return apiSecrets.get(key);
    }

//    @Scheduled(cron = "0 */5 * * * ?")
//    public void updateClientInfo() {
//        apiSecrets = getAllEnabledClientInfoFromDB();
//        redisUtil.set("SC:apiSecrets", apiSecrets);
//    }
//
//    public Map<String, String> getAllEnabledClientInfoFromDB() {
//        Map<String, String> result = new HashMap<>();
//        QueryWrapper<DbClient> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("1", "1");
//        List<DbClient> all = dbClientRepository.selectList(queryWrapper);
//        for (DbClient dbClient : all) {
//            if (dbClient.status == Status.ENABLED) {
//                result.put(dbClient.skey, dbClient.secret);
//                if (dbClient.channelId != null)
//                    result.put(dbClient.skey + "-appid", dbChannelRepository.selectById(dbClient.channelId).appid);
//            }
//        }
//
//        return result;
//    }
//
    /**
     * 构建一个可用的RegisteredClient
     * 
     * @param dbClient
     * @return
     */
    private RegisteredClient buildRegisteredClient(DbClient dbClient) {
        if (dbClient == null || dbClient.id == null || dbClient.status == Status.DISABLED)
            return null;
        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] digestBytes = md.digest(dbClient.secret.getBytes());
            // 加载token配置
            TokenSettings tokenSettings = TokenSettings.builder() //
                    .authorizationCodeTimeToLive(Duration.ofSeconds(dbClient.getOptionsValueWithDef("tokenSettings.authorizationCodeTimeToLive", 7200))) //
                    .accessTokenTimeToLive(Duration.ofSeconds(dbClient.getOptionsValueWithDef("tokenSettings.accessTokenTimeToLive", 7200))) //
                    .refreshTokenTimeToLive(Duration.ofSeconds(dbClient.getOptionsValueWithDef("tokenSettings.refreshTokenTimeToLive", 7200))) //
                    .build();
            List<String> aud = new ArrayList<>();
//            // 授权用户
//            String account = (String) ServletUtils.getRequest().getParameter("account");
//            if (account != null)
//                aud.add(account);
            ClientSettings clientSettings = ClientSettings.builder().requireAuthorizationConsent(true) //
                    .setting("aud", aud) //
                    .build();

            RegisteredClient registeredClient = RegisteredClient.withId(dbClient.id) //
                    .clientId(dbClient.id) //
                    .clientSecret("{noop}" + dbClient.getSkey()) //
//                    .clientSecret("{noop}" + ByteUtils.toHexString(digestBytes)) //
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) //
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) //
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) //
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) //
//                    .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc") //
                    .redirectUri("http://127.0.0.1:9100/authorized") //
//                    .scope(OidcScopes.OPENID) //
//                    .scope(OidcScopes.PROFILE) //
//                    .scope("message.read") //
//                    .scope("message.write") //
                    .tokenSettings(tokenSettings) //
                    .clientSettings(clientSettings) //
                    .build();
            return registeredClient;
        } catch (Exception e) {
            log.error("加载DbClient时异常：", e);
        }
        return null;
    }
}
