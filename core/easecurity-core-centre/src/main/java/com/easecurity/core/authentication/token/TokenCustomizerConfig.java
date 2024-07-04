package com.easecurity.core.authentication.token;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
//import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
//import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
//import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * token自定义处理逻辑
 *
 */
@Configuration
public class TokenCustomizerConfig {

//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
//        return (context) -> {
//            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
//                List<String> aud = context.getRegisteredClient().getClientSettings().getSetting("aud");
//                if (aud == null)
//                    aud = new ArrayList<>();
//                context.getClaims().audience(aud);
////                String loginType = ServletUtils.getRequest().getParameter("type");
////                if (loginType != null && !loginType.isEmpty()) {
////                    if ("0".equals(loginType.trim()))
////                        context.getClaims().claim("loginType", "YDGZH");
////                    else if ("1".equals(loginType.trim()))
////                        context.getClaims().claim("loginType", "YDXCX");
////                }
////                String ydToken = ServletUtils.getRequest().getParameter("ydToken");
////                if (ydToken != null && !ydToken.isEmpty()) {
////                    context.getClaims().claim("ydToken", ydToken);
////                }
//                context.getClaims().id(UUID.randomUUID().toString());
//            }
//            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
//                // TODO 待完成
////                OidcUserInfo userInfo = userInfoService.loadUser( // <2>
////                        context.getPrincipal().getName());
////                context.getClaims().claims(claims ->
////                        claims.putAll(userInfo.getClaims()));
//            }
//        };
//    }

}
