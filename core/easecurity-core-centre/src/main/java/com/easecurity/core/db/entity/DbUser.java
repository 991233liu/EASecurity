/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.db.entity;

import java.util.Date;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.easecurity.core.basis.b.UserEnum.AcStatus;
import com.easecurity.core.basis.b.UserEnum.PdStatus;
import com.easecurity.core.db.BaseEnumConverter;

@Entity
@Table(name = "b_user")
public class DbUser extends com.easecurity.core.basis.b.User {

    private static final long serialVersionUID = 5558625260330954585L;

    @Id
    public String id;

    /**
     * 登录账号
     */
    public String account;

    /**
     * 密码
     */
    public String pd;

    /**
     * 账号状态
     */
    @Convert(converter = AcStatusConverter.class)
    public AcStatus acStatus;

    /**
     * 密码状态
     */
    @Convert(converter = PdStatusConverter.class)
    public PdStatus pdStatus;

    /**
     * 身份串
     */
    public String identities;
    /**
     * 最后登录时间
     */
    public Date lastLoginTtime;
    /**
     * 密码错误次数
     */
    public Integer pdErrorTimes = 0;

    public Date dateCreated;
    public Date lastUpdated;

    public static class AcStatusConverter extends BaseEnumConverter<AcStatus> {

        public AcStatusConverter() {
            super(AcStatus.class);
        }
    }

    public static class PdStatusConverter extends BaseEnumConverter<PdStatus> {

        public PdStatusConverter() {
            super(PdStatus.class);
        }
    }
}
