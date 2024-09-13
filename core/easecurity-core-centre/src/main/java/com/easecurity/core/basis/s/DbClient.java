package com.easecurity.core.basis.s;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.easecurity.core.basis.s.ApiSecretEnum.Status;
import com.easecurity.core.db.BaseEnumConverter;

/**
 * 基于数据库的客户端
 *
 */
@Entity
@Table(name = "s_api_secret")
public class DbClient extends ApiSecret {

    private static final long serialVersionUID = -7058613146596994775L;
    
    @Id
    public String id;
    /**
     * 渠道id
     */
    public Integer channelId;
    /**
     * 系统id
     */
    public Integer internalSystemId;
    /**
     * key
     */
    public String skey;
    /**
     * secret
     */
    public String secret;
    /**
     * 扩展配置
     */
    public String options;
    /**
     * 状态
     */
    @Convert(converter = StatusConverter.class)
    public Status status;

    public Date dateCreated;
    public Date lastUpdated;

    @SuppressWarnings("unchecked")
    public boolean containsOptionsKey(String key) {
        if (key.indexOf(".") > -1) {
            String[] keys = key.split("\\.");
            Object value = getOptionsMap();
            for (String string : keys) {
                if (((Map<String, Object>) value).containsKey(string))
                    value = ((Map<String, Object>) value).get(string);
                else
                    return false;
            }
            return true;
        } else {
            return getOptionsMap().containsKey(key);
        }
    }

    @SuppressWarnings("unchecked")
    public Object getOptionsValue(String key) {
        if (key.indexOf(".") > -1) {
            String[] keys = key.split("\\.");
            Object value = getOptionsMap();
            for (String string : keys) {
                if (((Map<String, Object>) value).containsKey(string))
                    value = ((Map<String, Object>) value).get(string);
            }
            return value;
        } else {
            return getOptionsMap().get(key);
        }
    }

    public String getOptionsValueWithDef(String key, String def) {
        if (containsOptionsKey(key))
            return (String) getOptionsValue(key);
        else
            return def;
    }

    public int getOptionsValueWithDef(String key, int def) {
        if (containsOptionsKey(key))
            return (Integer) getOptionsValue(key);
        else
            return def;
    }

    public long getOptionsValueWithDef(String key, long def) {
        if (containsOptionsKey(key))
            return (Long) getOptionsValue(key);
        else
            return def;
    }

    @Transient
    private Map<String, Object> _optionsMap = new HashMap<>();

    private Map<String, Object> getOptionsMap() {
        if (_optionsMap.isEmpty() && options != null && !options.isEmpty())
            _optionsMap = JSONObject.parseObject(options);
        return _optionsMap;
    }
    
    public static class StatusConverter extends BaseEnumConverter<Status> {

        public StatusConverter() {
            super(Status.class);
        }
    }
}
