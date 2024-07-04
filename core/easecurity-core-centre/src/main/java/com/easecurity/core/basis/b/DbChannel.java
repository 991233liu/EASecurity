package com.easecurity.core.basis.b;

import java.util.Date;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.easecurity.core.basis.b.ChannelEnum.Status;
import com.easecurity.core.db.BaseEnumConverter;

/**
 * 渠道
 *
 */
@Entity
@Table(name = "b_channel")
public class DbChannel extends Channel {

    private static final long serialVersionUID = 2771180994840873028L;

    @Id
    public Integer id;
    /**
     * 渠道唯一标识appid
     */
    public String appid;
    /**
     * 渠道中文名称
     */
    public String cname;
    /**
     * 渠道英文名称
     */
    public String name;
    /**
     * 备注
     */
    public String memo;
    /**
     * 状态
     */
    @Convert(converter = StatusConverter.class)
    public Status status;

    public Date dateCreated;
    public Date lastUpdated;

    public static class StatusConverter extends BaseEnumConverter<Status> {

        public StatusConverter() {
            super(Status.class);
        }
    }
}
