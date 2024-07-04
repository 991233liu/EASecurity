package com.easecurity.admin

import com.easecurity.admin.auth.UserAdmin
import com.easecurity.admin.core.b.Org
import com.easecurity.admin.core.b.Posts
import com.easecurity.admin.core.b.User
import com.easecurity.admin.core.b.UserInfo
import com.easecurity.admin.core.r.Role
import com.easecurity.admin.core.r.RoleGroup
import com.easecurity.admin.core.r.RoleUser
import com.easecurity.admin.core.re.Menu
import com.easecurity.admin.core.b.Channel
import com.easecurity.admin.core.b.InternalSystem
import com.easecurity.admin.core.s.ApiSecret
import com.easecurity.admin.core.s.Certificates
import com.easecurity.core.basis.b.ChannelEnum
import com.easecurity.core.basis.b.InternalSystemEnum
import com.easecurity.core.basis.s.ApiSecretEnum
import com.easecurity.core.basis.s.CertificatesEnum
import com.easecurity.core.basis.b.OrgEnum
import com.easecurity.core.basis.b.PostsEnum
import com.easecurity.core.basis.b.UserEnum
import com.easecurity.core.basis.b.UserInfoEnum
import com.easecurity.core.basis.re.MenuEnum
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        if (User.count() == 0) {  // 系统全新安装时，初始化系统基础数据
            User user
            User.withTransaction {
                RoleGroup roleGroup = new RoleGroup(name: '系统管理员', code: 'admin')
                roleGroup.save(failOnError: true)
                RoleGroup userRoleGroup = new RoleGroup(name: '系统用户', code: 'user')
                userRoleGroup.save(failOnError: true)
                RoleGroup anonymousRoleGroup = new RoleGroup(name: '未登录用户', code: 'anonymous')
                anonymousRoleGroup.save(failOnError: true)
                Org org = new Org(name: '根', code: 'root', type: OrgEnum.Type.ROOT, status: OrgEnum.Status.ENABLED, fullName: '/', fullCode: '/', fullPathid: '/')
                org.save(failOnError: true)
                Role role = new Role(org: org, roleGroup: roleGroup)
                role.save(failOnError: true)
                Role userRole = new Role(org: org, roleGroup: userRoleGroup)
                userRole.save(failOnError: true)
                Role anonymousRole = new Role(org: org, roleGroup: anonymousRoleGroup)
                anonymousRole.save(failOnError: true)
                // TODO 账号identities空
                user = new User(account: 'admin', pd: '$2a$05$EzgpXxRZY/78MyUBA6ORfOsPQapPih36qlw8bdFmUBRLB25icMcOW', acStatus: UserEnum.AcStatus.ENABLED, pdStatus: UserEnum.PdStatus.ENABLED)
                user.save(failOnError: true)
                RoleUser roleUser = new RoleUser(role: role, user: user)
                roleUser.save(failOnError: true)
                Posts posts1 = new Posts(name: '公司领导', code: 'gslingdao', ranking: 20, type: PostsEnum.Type.LEADERSHIP)
                posts1.save(failOnError: true)
                Posts posts2 = new Posts(name: '部门经理', code: 'bmjingli', ranking: 10, type: PostsEnum.Type.LEADERSHIP)
                posts2.save(failOnError: true)
                Posts posts3 = new Posts(name: '员工', code: 'yuangong', ranking: 1, type: PostsEnum.Type.EMPLOYEE)
                posts3.save(failOnError: true)

                Menu menu = new Menu(name: '默认菜单', code: 'root', sortNumber: 0, status: MenuEnum.Status.ENABLED)
                menu.save(failOnError: true)
                Menu menu2 = new Menu(name: 'admin系统菜单', code: 'adminRoot', sortNumber: 0, status: MenuEnum.Status.ENABLED)
                menu2.save(failOnError: true)
            }
            UserAdmin.withTransaction {
                UserAdmin userAdmin = new UserAdmin(account: 'admin')
                userAdmin.save(failOnError: true)
                UserInfo userInfo = new UserInfo(user: user, userId: user.id, account: user.account, name: '系统管理员', avatar: '/adminUI/images/user.gif', status: UserInfoEnum.Status.ENABLED)
                userInfo.save(failOnError: true)
            }
        }
        initInternalSystem()
        initChannel()
    }

    def destroy = {
    }

	def initInternalSystem = {
        // 开发过程中，多次修改初始化数据，在某个时间节点前的数据清空，重新执行初始化。
        if ((Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) && InternalSystem.count() > 0) {
            if (System.currentTimeMillis() < 1716739199000) { // 2024-05-26 23:59:59 前的时间
                InternalSystem.withTransaction {
                    InternalSystem.executeUpdate("delete ApiSecret")
                    InternalSystem.executeUpdate("delete Certificates")
                    InternalSystem.executeUpdate("delete InternalSystem")
                    InternalSystem.executeUpdate("delete Channel")
                }
            }
        }
        if (InternalSystem.count() == 0) {  // 初始化系统
            InternalSystem.withTransaction {
                InternalSystem admin = new InternalSystem(sysid: 'admin', cname: 'admin平台', name: 'admin', status: InternalSystemEnum.Status.ENABLED);
                admin.save(failOnError: true)
                InternalSystem sc = new InternalSystem(sysid: 'SecurityCentre', cname: '认证中心', name: 'SecurityCentre', status: InternalSystemEnum.Status.ENABLED);
                sc.save(failOnError: true)
                InternalSystem gateway = new InternalSystem(sysid: 'gateway', cname: 'API网关', name: 'gateway', status: InternalSystemEnum.Status.ENABLED);
                gateway.save(failOnError: true)
                InternalSystem app = new InternalSystem(sysid: 'app', cname: 'app微服务应用', name: 'app', status: InternalSystemEnum.Status.ENABLED);
                app.save(failOnError: true)

//                RSAKey rsaKey = Jwks.generateRsa()
//                Certificates scCertificates = new Certificates(internalSystem: sc, publicKey: rsaKey.toPublicJWK().toJSONString(), privateKey: rsaKey.toPrivateKey().getAlgorithm(), type: CertificatesEnum.Type.RSA, status: CertificatesEnum.Status.ENABLED)
//                scCertificates.save(failOnError: true)

//                ApiSecret gatewayApi = new ApiSecret(internalSystem: gateway, skey: UUID.randomUUID().toString().replaceAll("-", ""), secret: Base64.encodeBase64String(UUID.randomUUID().toString().replaceAll("-", "").getBytes()), status: ApiSecretEnum.Status.ENABLED)
                ApiSecret adminApi = new ApiSecret(internalSystem: admin, skey: "7ce55a1e3a2d4903bd62090c5967a742", secret: "OGI4YzRmYWVkZTVkNGNlZmI2NDY2NjQxMTgxYTQ0Yjk=", status: ApiSecretEnum.Status.ENABLED)
                adminApi.save(failOnError: true)
                ApiSecret gatewayApi = new ApiSecret(internalSystem: gateway, skey: "3dc801db8e9c482bbe3be007bdc651b9", secret: "YzUyN2JkZWU5MTI0NGU3NTlhNTUwNDkxODVhMDg2ZDU=", status: ApiSecretEnum.Status.ENABLED)
                gatewayApi.save(failOnError: true)
                ApiSecret appApi = new ApiSecret(internalSystem: app, skey: "68f6523a41e24d17a40fcf55ed8eaca8", secret: "MWM4NTBkMGIxNzcyNDI1OTgwYTJlZmUwYTE0ZDRhYzg=", status: ApiSecretEnum.Status.ENABLED)
                appApi.save(failOnError: true)
            }
        }
    }

    def initChannel = {
        if (Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) {
            if (Channel.count() == 0) {  // 初始化渠道
                Channel.withTransaction {
                    Channel test = new Channel(appid: '6d882088', cname: '测试渠道', name: 'test', status: ChannelEnum.Status.ENABLED)
                    test.save(failOnError: true)
                    ApiSecret testApiSecret = new ApiSecret(channel: test, skey: "messaging-client", secret: "secret", options: '{"tokenSettings":{"authorizationCodeTimeToLive":300,"accessTokenTimeToLive":300,"refreshTokenTimeToLive":300}}', status: ApiSecretEnum.Status.ENABLED)
                    testApiSecret.save(failOnError: true)
                    Certificates testCertificates = new Certificates(channel: test, keyId: "226688", publicKey: "b0e1a1f36fa8e9da5656f6dbe39a516f616bd9b3a7971806f53f35ff00b6e7ba", privateKey: "e38b997cb325db83a56ea188de3a7568", status: CertificatesEnum.Status.ENABLED, type: CertificatesEnum.Type.AES)
                    testCertificates.save(failOnError: true)
                }
            }
        }
    }
}
