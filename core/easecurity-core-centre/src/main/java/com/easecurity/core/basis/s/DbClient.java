package com.easecurity.core.basis.s;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 基于数据库的客户端
 *
 */
public class DbClient extends ApiSecret {

    private static final long serialVersionUID = -7058613146596994775L;
    
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

    private Map<String, Object> _optionsMap = new HashMap<>();

    private Map<String, Object> getOptionsMap() {
        if (_optionsMap.isEmpty() && options != null && !options.isEmpty())
            _optionsMap = JSONObject.parseObject(options);
        return _optionsMap;
    }
}
