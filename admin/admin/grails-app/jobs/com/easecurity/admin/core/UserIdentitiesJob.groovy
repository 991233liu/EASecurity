package com.easecurity.admin.core

import com.easecurity.admin.core.b.Org
import com.easecurity.admin.core.b.OrgUser
import com.easecurity.admin.core.b.Posts
import com.easecurity.admin.core.b.User
import com.easecurity.admin.core.r.Role
import com.easecurity.admin.core.r.RoleUser
import com.easecurity.admin.core.s.JobUser

/**
 * 定时计算用户的identities字段
 */
class UserIdentitiesJob {
    static triggers = {
        simple repeatInterval: 30000l // execute job once in 300 seconds
    }

    def group = "User"
    def description = "定时，每5分钟一次，重新计算用户的identities字段。（没有变化的不会重新计算）"

    def execute() {
        Date now = new Date();
        List<User> failList = []
        JobUser jobUser = JobUser.findByTabName('User')
        JobUser jobOrg = JobUser.findByTabName('Org')
        JobUser jobOrgUser = JobUser.findByTabName('OrgUser')
        JobUser jobRoleUser = JobUser.findByTabName('RoleUser')

        // 查找没有初始化身份串的人员
        Date lastTime = jobUser ? jobUser.lastTime : new Date(1)
        List<User> userList = User.findAll("from User where identities is null or identities = '' ")
        System.out.println("-----## UserIdentitiesJob.userList1=" + userList)
        // 查找组织信息变化的的人员，组织必须启用状态才有效
        lastTime = jobOrg ? jobOrg.lastTime : new Date(1)
        ((List<OrgUser>) OrgUser.findAllByOrgInList(Org.findAll('from Org where lastUpdated > :lastUpdated  and status = 0', [lastUpdated: lastTime]))).each { OrgUser orgUser ->
            if (!userList.contains(orgUser.user)) userList.add(orgUser.user)
        }
        System.out.println("-----## UserIdentitiesJob.userList2=" + userList)
        // 查找人员组织关系：新增、修改的人员
        lastTime = jobOrgUser ? jobOrgUser.lastTime : new Date(1)
        ((List<OrgUser>) OrgUser.findAll('from OrgUser where lastUpdated > :lastUpdated', [lastUpdated: lastTime])).each { OrgUser orgUser ->
            if (!userList.contains(orgUser.user)) userList.add(orgUser.user)
        }
        System.out.println("-----## UserIdentitiesJob.userList3=" + userList)
        // 查找人员角色关系：新增、修改的人员
        lastTime = jobRoleUser ? jobRoleUser.lastTime : new Date(1)
        ((List<RoleUser>) RoleUser.findAll('from RoleUser where lastUpdated > :lastUpdated or lastUpdated is null', [lastUpdated: lastTime])).each { RoleUser roleUser ->
            if (!userList.contains(roleUser.user)) userList.add(roleUser.user)
        }
        System.out.println("-----## UserIdentitiesJob.userList4=" + userList)

        // 更新人員身份信息
        userList.each { User user ->
            User.withTransaction {
                try {
                    if (!User.updateIdentities(user)) {
                        failList.add(user)
                    }
                } catch (Exception e) {
                    failList.add(user)
                }
            }
        }

        // 更新系统记录表
        JobUser.withTransaction {
            if (jobOrg) jobOrg.lastTime = now
            else jobOrg = new JobUser(tabName: "Org", lastTime: now)
            jobOrg.save(failOnError: true)

            if (jobOrgUser) jobOrg.lastTime = now
            else jobOrgUser = new JobUser(tabName: "OrgUser", lastTime: now)
            jobOrgUser.save(failOnError: true)

            if (jobRoleUser) jobOrg.lastTime = now
            else jobRoleUser = new JobUser(tabName: "RoleUser", lastTime: now)
            jobRoleUser.save(failOnError: true)

            now = new Date();
            if (jobUser) jobUser.lastTime = now
            else jobUser = new JobUser(tabName: "User", lastTime: now)
            jobUser.save(failOnError: true)
        }
        // TODO 删除User关联关系时，怎么处理？方案1，再删除的controller中直接user.updateIdentities；方案2，修改user的lastUpdated
        // TODO failList怎么处理？？？
        System.out.println("-----## UserIdentitiesJob.failList5=" + failList)
    }

}
