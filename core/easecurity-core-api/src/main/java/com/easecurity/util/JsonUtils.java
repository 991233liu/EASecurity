/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 自定义json解析类<br>
 * <p>
 * 此类为线程安全类<br>
 * <p>
 * createTime:2016年3月15日
 * 
 * @author liulufeng
 * 
 */
// TODO 暂时不支持注释符
public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final char huakuohao_s = '{';
    private static final char huakuohao_e = '}';
    private static final char dakuohao_s = '[';
    private static final char dakuohao_e = ']';
    private static final char douhao = ',';

    /**
     * 解析json串为java常用数据类型
     * 
     * @param jsonString 需要解析的 json字符串
     * @return Object
     */
    public static Object jsonToObject(String jsonString) {
	if (jsonString == null || jsonString.trim().isEmpty())
	    return null;
	try {
	    JsonBean jsonBean = new JsonBean(jsonString, null);
	    return getValue(jsonBean, null, null);
	} catch (Exception e) {
	    throw new IllegalArgumentException("Json解析错误。Json串原始信息如下：" + jsonString + "\n", e);
	}
    }

    /**
     * 解析json串为自定义数据类型。
     * 
     * @param jsonString 需要解析的 json字符串
     * @param alias      自定义的数据类型配置。Map的key为待转换的json中的key，Map的value为json中的value所对应的数据类型
     *                   。例如：将名为data的属性转换为com.sgcc.orderbuild.Data，则：alias.put("data",
     *                   "com.sgcc.orderbuild.Data") 或者 alias.put("data",
     *                   com.sgcc.orderbuild.Data.class)
     * @return Object
     */
    public static Object jsonToObject(String jsonString, Map<?, ?> alias) {
	if (jsonString == null || jsonString.trim().isEmpty())
	    return null;
	try {
	    JsonBean jsonBean = new JsonBean(jsonString, alias);
	    return getValue(jsonBean, null, null);
	} catch (Exception e) {
	    throw new IllegalArgumentException("Json解析错误。Json串原始信息如下：" + jsonString + "\n", e);
	}
    }

    /**
     * 解析json串为自定义数据类型。
     * 
     * @param jsonString 需要解析的 json字符串
     * @param clazz      Class
     * @return Class
     */
    public static <T> T jsonToObject(String jsonString, Class<T> clazz) {
	return jsonToObject(jsonString, clazz, new HashMap<>());
    }

    /**
     * 解析json串为自定义数据类型。
     * 
     * @param jsonString 需要解析的 json字符串
     * @param clazz      Class
     * @param alias      自定义的数据类型配置。Map的key为待转换的json中的key，Map的value为json中的value所对应的数据类型
     *                   。例如：将名为data的属性转换为com.sgcc.orderbuild.Data，则：alias.put("data",
     *                   "com.sgcc.orderbuild.Data") 或者 alias.put("data",
     *                   com.sgcc.orderbuild.Data.class)
     * @return Class
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String jsonString, Class<T> clazz, @SuppressWarnings("rawtypes") Map alias) {
	if (jsonString == null || jsonString.trim().isEmpty())
	    return null;
	jsonString = "{'data_root':" + jsonString + "}";
	alias.put("data_root", clazz);
	return (T) ((Map<String, Object>) jsonToObject(jsonString, alias)).get("data_root");
    }

    /**
     * object解析成json
     * 
     * @param obj 支持map、list、实体类
     * @return json字符串
     */
    public static String objectToJson(Object obj) {
	String str = objectToJson(obj, null);
	if (null != str) {
	    if (obj instanceof List) {
		int index = str.indexOf(dakuohao_s);
		int lastIndex = str.lastIndexOf(dakuohao_e);
		str = str.substring(index, lastIndex + 1);
	    } else { // 需求上Map和对象的截取方式一样
		int index = str.indexOf(huakuohao_s);
		int nextIndex = str.indexOf(huakuohao_s, index + 1);
		str = str.substring(nextIndex, str.length() - 1);
	    }
	}
	return str;
    }

    /**
     * object解析成json,将对象以Map形式转换，支持自定义别名
     * 
     * @param obj   对象
     * @param alias 自定义别名
     * @return json
     */
    public static String objectToJson(Object obj, String alias) {
	return objectToJson(obj, alias, null, null);
    }

    /**
     * object解析成json,将对象以Map形式转换，支持自定义别名,属性自定义别名
     * 
     * @param obj         对象
     * @param alias       自定义别名
     * @param aliasFields 属性别名：Map的key为源属性名，value为自定义的属性名
     * @return json
     */
    @Deprecated
    public static String objectToJson(Object obj, String alias, List<AliasField> aliasFields) {
	return objectToJson(obj, alias, aliasFields, null);
    }

    /**
     * object解析成json,解析对象为自定义的别名，属性别名，过滤属性
     * 
     * @param obj           对象
     * @param alias         对象别名
     * @param aliasFields   属性别名：Map的key为源属性名，value为自定义的属性名
     * @param excludeFields 忽略属性
     * @return json字符串
     */
    @Deprecated
    @SuppressWarnings("rawtypes")
    public static String objectToJson(Object obj, String alias, List<AliasField> aliasFields, Map<Object, Class> excludeFields) {
	try {
	    Map<String, String> _alias = null;
	    Map<Class<?>, String> _omitFields = null;

	    // 兼容老代码，做参数转换
	    if (alias != null)
		_alias = addPrm(_alias, obj.getClass().getName(), alias);
	    if (aliasFields != null) {
		for (AliasField aliasField : aliasFields) {
		    _alias = addPrm(_alias, aliasField.filedName, aliasField.aliasName);
		}
	    }
	    if (excludeFields != null && !excludeFields.isEmpty()) {
		_omitFields = new HashMap<Class<?>, String>();
		String s_omitFields = null;
		for (Map.Entry<Object, Class> excludeField : excludeFields.entrySet()) {
		    s_omitFields = _omitFields.get(excludeField.getValue());
		    if (s_omitFields == null) {
			s_omitFields = (String) excludeField.getKey();
		    } else {
			s_omitFields = "," + excludeField.getKey();
		    }
		    _omitFields.put(excludeField.getValue(), s_omitFields);
		}
	    }
	    return object2Json(obj, _alias, _omitFields);
	} catch (Exception e) {
	    throw new IllegalArgumentException("objectTOJson异常：\n", e);
//	    try {
//		return JsonUtils_.objectToJson(obj, alias, aliasFields, excludeFields);
//	    } catch (Exception e2) {
//		throw new IllegalArgumentException("objectTOJson异常：\n", e);
//	    }
	}
    }

    /**
     * 临时方法：参数格式转换，兼容老代码用
     */
    private static Map<String, String> addPrm(Map<String, String> prm, String key, String value) throws Exception {
	if (prm == null)
	    prm = new HashMap<String, String>();
	if (prm.containsKey(key)) {
	    Exception e = new RuntimeException("系统遇到一个严重错误，请联系管理员：object2Json转换时异常。");
	    log.error("系统遇到一个严重错误，请联系管理员：object2Json转换时异常。key=" + key + " , map=" + prm, e);
	    throw e;
	}
	prm.put(key, value);
	return prm;
    }

    /**
     * 将java bean转换为json。此转换符合 w3c 的 RFC 4627 规范
     * 
     * @param bean  bean
     * @param alias 别名。入参为Map<String, String>，将key变量的命名转换为value。
     * @return json
     * @throws ServiceException
     */
    public static String object2Json(Object bean, Map<String, String> alias) throws Exception {
	return object2Json(bean, alias, null);
    }

    /**
     * 将java bean转换为json。此转换符合 w3c 的 RFC 4627 规范
     * 
     * @param bean       bean
     * @param alias      别名。入参为Map<String, String>，将key变量的命名转换为value。
     * @param omitFields 忽略列表。入参为Map<Class<?>,
     *                   String>，将忽略Class<?>中的指定的属性。如果有多个属性，使用","分割。
     * @return json
     * @throws ServiceException
     */
    public static String object2Json(Object bean, Map<String, String> alias, Map<Class<?>, String> omitFields) throws Exception {
	try {
	    StringBuffer xml = new StringBuffer();
	    joinRoot(xml, bean, alias, omitFields);
	    return xml.toString();
	} catch (Exception e) {
	    throw new Exception("将bean转换为json时错误。入参信息如下：bean=" + bean.getClass().getName() + " alias=" + alias + " omitFields=" + omitFields + "\n", e);
	}
    }

//	
//	/**
//	 * 解析对象为自定义的别名 默认最外层data (使用Gson转换)
//	 * @param obj 对象
//	 * @param aliasFields 属性别名：Map的key为源属性名，value为自定义的属性名
//	 * @param excludeFields 忽略属性
//	 * @return json字符串
//	 */
//	/*public static String objectToJson(Object obj, Map<Object, Object> aliasFields, List<String> excludeFields){
//		try {
//			return toJson(obj, null, aliasFields, excludeFields);
//		} catch (Exception e) {
//			throw new IllegalArgumentException("objectTOJson异常：\n",e);
//		}
//	}*/
//	
//	/**
//	 * 对象含有List转json(基于xstream，兼容老版本，标准json不推荐使用)
//	 * @param obj 对象
//	 * @return json
//	 */
//	@Deprecated
//	public static String objectOfListToJson(Object obj){
//		return JsonUtils_.objectOfListToJson(obj);
//	}
//	/**
//	 * 对象含有List转json(基于xstream，兼容老版本，标准json不推荐使用)
//	 * @param obj 对象
//	 * @param alias 类名：Map的key为别名，value为源类名
//	 * @return json
//	 */
//	@SuppressWarnings("rawtypes")
//	public static String objectOfListToJson(Object obj,Map<String, Class> alias){
//		return JsonUtils_.objectOfListToJson(obj, alias);
//	}
//
//	/**
//	 * 对象含有List转json(基于xstream，兼容老版本，标准json不推荐使用)
//	 * @param obj 对象
//	 * @param alias 类别名：Map的key为别名，value为源类名
//	 * @param aliasFieldList 属性别名：AliasField: 属性别名：alias,所属类：type,filedName:源字段名
//	 * @param omitFields 忽略字段：Map的key为忽略字段,value为所属类
//	 * @param annotation 注解 ：参数为List<Object>，1、boolean的true;2、Object.class
//	 * @return json字符串
//	 */
//	@SuppressWarnings("rawtypes")
//	public static String objectOfListToJson(Object obj,Map<String,Class> alias,List<AliasField> aliasFields,Map<String,Class> omitFields, List<Object> annotation){
//		return JsonUtils_.objectOfListToJson(obj, alias, aliasFields, omitFields, annotation);
//	}
// 
//	/**
//	 * 解析json串，转换为Map
//	 * 
//	 * @param jsonString	需要解析的 json字符串
//	 * @return Map
//	 */
//	private static Map<?, ?> jsonToMap(JsonBean jsonBean) {
//		return (Map<?, ?>) getValue(jsonBean, null);
//	}

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ object2Json 相关方法 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 拼接根节点信息
     * 
     * @param json       结果json
     * @param bean       bean
     * @param alias      别名。入参为Map<String, String>，将key变量的命名转换为value。
     * @param omitFields 忽略列表。入参为Map<Class<?>,
     *                   String>，将忽略Class<?>中的指定的属性。如果有多个属性，使用","分割。
     */
    private static void joinRoot(StringBuffer json, Object bean, Map<String, String> alias, Map<Class<?>, String> omitFields) {
	// 根节点默认以bean的数据类型为tagName
	String rootName = bean.getClass().getName();
	if (alias != null && alias.containsKey(rootName))
	    rootName = alias.get(rootName);

	// 拼装根
	json.append("{");

	appendMember(json, rootName, bean, alias, omitFields);

	// 拼装根
	json.append('}');
    }

    private static void appendMember(StringBuffer json, String tagName, Object value, Map<String, String> alias, Map<Class<?>, String> omitFields) {
	// TODO 未来空值也可以产生标签
	if (value == null)
	    return;

	// 拼装tag
	json.append('"');
	json.append(tagName);
	json.append("\":");

	// 拼装value
	appendValue(json, value, alias, omitFields);
    }

    @SuppressWarnings("unchecked")
    private static void appendValue(StringBuffer json, Object value, Map<String, String> alias, Map<Class<?>, String> omitFields) {
	// 拼装value
	if (value instanceof Map) { // Map类型
	    joinMap(json, (Map<String, Object>) value, alias, omitFields);
	} else if (value instanceof List) { // List类型
	    joinList(json, (List<Object>) value, alias, omitFields);
	} else if (isBasicDataType(value.getClass())) { // 基础类型
	    joinBasicDataType(json, value, alias, omitFields);
	} else { // bean类型
	    joinBean(json, value, alias, omitFields);
	}
    }

    /**
     * 拼接节点信息，bean
     * 
     * @param json       结果json
     * @param bean       bean
     * @param alias      别名。入参为Map<String, String>，将key变量的命名转换为value。
     * @param omitFields 忽略列表。入参为Map<Class<?>,
     *                   String>，将忽略Class<?>中的指定的属性。如果有多个属性，使用","分割。
     */
    private static void joinBean(StringBuffer json, Object bean, Map<String, String> alias, Map<Class<?>, String> omitFields) {
	// 获取bean中的所有属性，支持继承
	Class<?> clazz = bean.getClass();
	initBeanFields(clazz);
	Map<String, Field> fields = getFields(bean);

	// 预处理忽略列表，方便后续使用
	String s_omitFields = omitFields != null ? omitFields.get(clazz) : null;
	if (s_omitFields != null && !s_omitFields.startsWith(",")) {
	    s_omitFields = "," + s_omitFields + ",";
	    omitFields.put(clazz, s_omitFields);
	}

	// TODO 未来空bean也可以产生标签
	if (bean != null) {
	    // 拼装对象根
	    json.append('{');

	    // 遍历所有属性，生产Member
	    Object value;
	    for (Map.Entry<String, Field> property : fields.entrySet()) {
		// 忽略列表
		if (s_omitFields != null && s_omitFields.indexOf("," + property.getKey() + ",") > -1) {
		    continue;
		} else {
		    value = getPropertyValue(bean, property.getValue());
		    if (alias != null && alias.containsKey(property.getKey())) { // 有别名
			appendMember(json, alias.get(property.getKey()), value, alias, omitFields);
		    } else { // 无别名
			appendMember(json, property.getKey(), value, alias, omitFields);
		    }
		    // TODO 未来空值也可以产生标签
		    if (value != null) {
			// 拼装多值分隔符
			json.append(',');
		    }
		}
	    }

	    // 去掉最后一个多值分隔符
	    if (json.charAt(json.length() - 1) == ',')
		json.deleteCharAt(json.length() - 1);

	    // 拼装对象根
	    json.append('}');
	}
    }

    /**
     * 获取属性值
     * 
     * @param bean
     * @param field
     * @return
     */
    private static Object getPropertyValue(Object bean, Field field) {
	synchronized (field) { // 取时并发造成bug
	    field.setAccessible(true);
	    try {
		return field.get(bean);
	    } catch (Exception e) {
		throw new RuntimeException("bean对象获取属性值时错误：bean=" + bean.getClass().getName() + " field=" + field.getName(), e);
	    } finally {
		field.setAccessible(false);
	    }
	}
    }

    /**
     * 拼接节点信息，Map
     * 
     * @param json       结果json
     * @param bean       bean
     * @param alias      别名。入参为Map<String, String>，将key变量的命名转换为value。
     * @param omitFields 忽略列表。入参为Map<Class<?>,
     *                   String>，将忽略Class<?>中的指定的属性。如果有多个属性，使用","分割。
     */
    private static void joinMap(StringBuffer json, Map<String, Object> map, Map<String, String> alias, Map<Class<?>, String> omitFields) {
	// 拼装对象根
	json.append('{');

	if (map != null && !map.isEmpty()) {
	    // 遍历所有属性，生产Member
	    for (Map.Entry<String, Object> property : map.entrySet()) {
		if (alias != null && alias.containsKey(property.getKey())) { // 有别名
		    appendMember(json, alias.get(property.getKey()), property.getValue(), alias, omitFields);
		} else { // 无别名
		    appendMember(json, property.getKey(), property.getValue(), alias, omitFields);
		}
		// TODO 未来空值也可以产生标签
		if (property.getValue() != null) {
		    // 拼装多值分隔符
		    json.append(',');
		}
	    }

	    // 去掉最后一个多值分隔符
	    if (json.charAt(json.length() - 1) == ',')
		json.deleteCharAt(json.length() - 1);
	}

	// 拼装对象根
	json.append('}');
    }

    /**
     * 拼接节点信息，基础数据类型
     * 
     * @param json       结果json
     * @param value      value
     * @param alias      别名。入参为Map<String, String>，将key变量的命名转换为value。
     * @param omitFields 忽略列表。入参为Map<Class<?>,
     *                   String>，将忽略Class<?>中的指定的属性。如果有多个属性，使用","分割。
     */
    private static void joinList(StringBuffer json, List<Object> value, Map<String, String> alias, Map<Class<?>, String> omitFields) {
	// 拼装对象根
	json.append('[');

	for (Object c_value : value) {
	    appendValue(json, c_value, alias, omitFields);
	    // 拼装多值分隔符
	    json.append(',');
	}

	if (value != null && !value.isEmpty()) {
	    // 去掉最后一个多值分隔符
	    if (json.charAt(json.length() - 1) == ',')
		json.deleteCharAt(json.length() - 1);
	}

	// 拼装对象根
	json.append(']');
    }

    /**
     * 拼接节点信息，基础数据类型
     * 
     * @param json       结果json
     * @param value      value
     * @param alias      别名。入参为Map<String, String>，将key变量的命名转换为value。
     * @param omitFields 忽略列表。入参为Map<Class<?>,
     *                   String>，将忽略Class<?>中的指定的属性。如果有多个属性，使用","分割。
     */
    private static void joinBasicDataType(StringBuffer json, Object value, Map<String, String> alias, Map<Class<?>, String> omitFields) {
	if (value == null) {
	    json.append("null");
	} else if (value.getClass() == String.class) {
	    json.append('"');
	    value = converChar(value.toString());
	    json.append(value);
	    json.append('"');
	} else {
	    json.append(String.valueOf(value));
	}
    }

//	public static class XStreamUtilModel{
//		@SuppressWarnings("rawtypes")
//		Map<String, Class> alias=new HashMap<String,Class>();
//		List<AliasField> aliasList=new ArrayList<JsonUtils.AliasField>();
//		@SuppressWarnings("rawtypes")
//		Map<String,Class> omit=new HashMap<String,Class>();
//		List<Object> ObList;
//	}

    /**
     * 属性别名内部类
     */
    public static class AliasField {
	/**
	 * 别名
	 */
	String aliasName;
	/**
	 * 所属类
	 */
	@SuppressWarnings("rawtypes")
	Class clazz;
	/**
	 * 字段名
	 */
	String filedName;

	@SuppressWarnings("rawtypes")
	public AliasField(String aliasName, Class clazz, String filedName) {
	    super();
	    this.aliasName = aliasName;
	    this.clazz = clazz;
	    this.filedName = filedName;
	}
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ object2Json 相关方法 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ json2Object 相关方法 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 获取key
     */
    private static String getKey(JsonBean jsonBean) {
	// 鼠标移动到key的开始位置
	getValueFirstChar(jsonBean);
	return to_String(jsonBean, false).trim();
    }

    /**
     * 获取值。
     */
    private static Object getValue(JsonBean jsonBean, Object bean, String beanKey) {
	char c = getValueFirstChar(jsonBean);
	try {
	    if (c == huakuohao_s) { // Map或bean类型
		if (bean == null)
		    return toMap(jsonBean);
		return toBean(jsonBean, bean);
	    } else if (c == dakuohao_s) { // List类型
		return toList(jsonBean, bean, beanKey);
	    } else if (c == '"' || c == '\'') { // String类型
		return to_String(jsonBean);
	    } else { // 其他类型
		return toOtherType(jsonBean);
	    }
	} catch (ClassNotFoundException e) {
	    throw new RuntimeException("转换value时出现错误：cursor=" + jsonBean.cursor, e);
	} catch (InstantiationException e) {
	    throw new RuntimeException("转换value时出现错误：cursor=" + jsonBean.cursor, e);
	} catch (IllegalAccessException e) {
	    throw new RuntimeException("转换value时出现错误：cursor=" + jsonBean.cursor, e);
	}
    }

    /**
     * 解析成集合
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    private static List<Object> toList(JsonBean jsonBean, Object bean, String beanKey) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	List<Object> ret = new ArrayList<Object>();
	char sep = jsonBean.getNextValidChar();

	// 指针后移，跳过开始字符
	jsonBean.next();

	if (dakuohao_e == sep) { // 开始字符后面紧跟着结束字符，说明是个空的list。
	    // 指针直接跳到结束符
	    sep = getSeparator(jsonBean);
	} else { // 有数据
	    // 开始解析
	    while (jsonBean.cursor < jsonBean.strlength) {
		ret.add(getValue(jsonBean, getBean(jsonBean, beanKey), beanKey));
		// 判断结束符
		sep = getSeparator(jsonBean);
		if (dakuohao_e == sep) { // 结束符
		    break;
		} else if (douhao == sep) { // ","分隔符
		    // 跳过","分隔符
		    jsonBean.next();
		} else
		    throw new RuntimeException("结束符错误：cursor=" + jsonBean.cursor + " sep=" + sep);
	    }
	}

	// 指针后移，跳过结束符
	jsonBean.next();

	return ret;
    }

    /**
     * 解析成Map或者bean
     * 
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static Object toMap(JsonBean jsonBean) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	Map<String, Object> ret = new HashMap<String, Object>();
	String key = null;
	char sep = jsonBean.getNextValidChar();
	int cursor_start = 0; // 游标

	// 指针后移，跳过开始字符
	jsonBean.next();

	if (huakuohao_e == sep) { // 开始字符后面紧跟着结束字符，说明是个空的map。
	    // 指针直接跳到结束符
	    sep = getSeparator(jsonBean);
	} else { // 有数据
	    // 开始解析
	    while (jsonBean.cursor < jsonBean.strlength) {
		try {
		    cursor_start = jsonBean.cursor;
		    key = getKey(jsonBean);
		} catch (Exception e) {
		    // 获取key时报错了，说明是空的map，但不是null。例如："data":{}
		    jsonBean.cursor = cursor_start;
		    if (ret.isEmpty()) {
			// 结束符
			sep = getSeparator(jsonBean);
			// 指针后移，跳过结束符
			jsonBean.next();
			return ret;
		    } else
			throw new RuntimeException("map解析时获取key错误：cursor=" + jsonBean.cursor);
		}
		// 跳过":"分隔符
		sep = getSeparator(jsonBean);
		if (':' != sep)
		    throw new RuntimeException("key:value分隔符错误：cursor=" + jsonBean.cursor + " sep=" + sep);
		ret.put(key, getValue(jsonBean, getBean(jsonBean, key), key));
		// 判断结束符
		sep = getSeparator(jsonBean);
		if (huakuohao_e == sep) { // 结束符
		    break;
		} else if (douhao == sep) { // ","分隔符
		    // 跳过","分隔符
		    jsonBean.next();
		} else
		    throw new RuntimeException("结束符错误：cursor=" + jsonBean.cursor + " sep=" + sep);
	    }
	}

	// 指针后移，跳过结束符
	jsonBean.next();
	return ret;
    }

    /**
     * 解析成bean
     * 
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     */
    private static Object toBean(JsonBean jsonBean, Object bean) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException {
	String key = null;
	Object valueType = null;
	char sep;
	int cursor_start = 0; // 游标

	// 指针后移，跳过开始字符
	jsonBean.next();
	Map<String, Field> fields = getFields(bean);

	sep = jsonBean.getCurrentChar();
	if (huakuohao_e == sep) { // 开始字符后面紧跟着结束字符，说明是个空的bean。
	    // 指针后移，跳过结束符
	    jsonBean.next();
	    return null;
	} else {
	    // 开始解析
	    while (jsonBean.cursor < jsonBean.strlength) {
		try {
		    cursor_start = jsonBean.cursor;
		    key = getKey(jsonBean);
		} catch (Exception e) {
		    // 获取key时报错了，说明是空的对象，但不是null。例如："data":{}
		    jsonBean.cursor = cursor_start;
		    // 结束符
		    sep = getSeparator(jsonBean);
		    // 指针后移，跳过结束符
		    jsonBean.next();
		    return null;
		}
		// 跳过":"分隔符
		sep = getSeparator(jsonBean);
		if (':' != sep)
		    throw new RuntimeException("key:value分隔符错误：cursor=" + jsonBean.cursor + " sep=" + sep);

		valueType = getBean(jsonBean, key);
		setPropertyValue(bean, fields.get(key), key, getValue(jsonBean, valueType, key));

		// 判断结束符
		sep = getSeparator(jsonBean);
		if (huakuohao_e == sep) { // 结束符
		    break;
		} else if (douhao == sep) { // ","分隔符
		    // 跳过","分隔符
		    jsonBean.next();
		} else
		    throw new RuntimeException("结束符错误：cursor=" + jsonBean.cursor + " sep=" + sep);
	    }
	}

	// 指针后移，跳过结束符
	jsonBean.next();
	return bean;
    }

    private static void setPropertyValue(Object bean, Field field, String key, Object value) throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
	synchronized (field) { // 赋值时并发造成bug
	    field.setAccessible(true);
	    try {
		Object type = field.getType();
		if (bean.getClass() == type) {
		    field.set(bean, value);
		} else if (type == long.class || type == Long.class) {
		    field.set(bean, Long.parseLong(value.toString()));
		} else if (type == double.class || type == Double.class) {
		    field.set(bean, Double.parseDouble(value.toString()));
		} else if (type == int.class || type == Integer.class) {
		    field.set(bean, Integer.parseInt(value.toString()));
		} else if (type == float.class || type == Float.class) {
		    field.set(bean, Float.parseFloat(value.toString()));
		} else if (type == String.class) {
		    field.set(bean, value.toString());
		} else { // 其它未知情况，以后发现时再追加处理
		    field.set(bean, value);
		}
	    } catch (Exception e) {
		// List类型的数据，有时会用Map套装一下。历史遗留问题，报错时兼容一下。
		if (value instanceof Map && field.getType().getName().equals("java.util.List")) {
		    // 规律1：map的key去掉“List”字符串后，在map内部中会找到一个对应的key，其value就是最终值
		    String t_key = key.substring(0, key.length() - 4);
		    if (key.endsWith("List") && ((Map<?, ?>) value).containsKey(t_key))
			field.set(bean, ((Map<?, ?>) value).get(t_key));
		    else {
			// 规律2：map内部只有1个对象，对象的value是List
			Iterator<?> temps = ((Map<?, ?>) value).entrySet().iterator();
			if (temps.hasNext()) {
			    if (!field.isAccessible()) {
				log.error("这里不应该是false，发生了严重错误。请联系管理员！！！");
				field.setAccessible(true);
				log.info("重新赋值后，field.isAccessible()=" + field.isAccessible());
			    }
			    @SuppressWarnings("unchecked")
			    Map.Entry<String, List<?>> temp = (Entry<String, List<?>>) temps.next();
			    field.set(bean, temp.getValue());
			}
		    }
		}
	    }
	    field.setAccessible(false);
	}
    }

    /**
     * 解析String
     */
    private static String to_String(JsonBean jsonBean) {
	return to_String(jsonBean, true);
    }

    /**
     * 解析String
     */
    /**
     * @param jsonBean
     * @param needSep  是否需要开始符
     */
    private static String to_String(JsonBean jsonBean, boolean needSep) {
	char c = getValueFirstChar(jsonBean);
	if ('"' != c && '\'' != c) {
	    if (needSep)
		throw new RuntimeException("String开始符错误：cursor=" + jsonBean.cursor + " sep=" + c);
	    else
		jsonBean.back();
	}
	StringBuffer ret = new StringBuffer();

	// 指针后移，跳过开始字符
	jsonBean.next();

	// 开始解析
	while (jsonBean.cursor < jsonBean.strlength) {
	    c = jsonBean.getCurrentChar();
	    if ('"' == c || '\'' == c)
		break; // 再次出现结束符时结束
	    if (c == '\\' && jsonBean.getNextChar() == '"') { // value中间含有特殊字符"""
		ret.append('"');
		jsonBean.next();
	    } else if (c == '\\' && jsonBean.getNextChar() == '\'') { // value中间含有特殊字符"'"
		ret.append('\'');
		jsonBean.next();
	    } else if (!needSep && c == ':') { // 当有些特殊情况，没有开始和结束符号时，找到“:”时结束。
		jsonBean.back();
		break;
	    } else
		ret.append(c);
	    jsonBean.next();
	}

	// 指针后移，跳过结束符
	jsonBean.next();

	return ret.toString();
    }

    /**
     * 解析数字、布尔、null
     */
    private static Object toOtherType(JsonBean jsonBean) {
	char c = getValueFirstChar(jsonBean);
	StringBuffer ret = new StringBuffer(c);
	if (('-' == c || '+' == c) || ('0' <= c && c <= '9')) { // 数字类型
	    boolean hasPoint = false;
	    while (jsonBean.cursor < jsonBean.strlength) {
		c = jsonBean.getCurrentChar();
		if (isEndSymbol(c))
		    break; // 再次出现结束符时结束
		ret.append(c);
		if ('.' == c)
		    hasPoint = true; // 浮点型
		jsonBean.next();
	    }
	    String str = ret.toString().trim();
	    if (hasPoint) { // 浮点型
		if (str.indexOf("D") > -1 || str.indexOf("d") > -1) { // double类型
		    return Double.parseDouble(str);
		} else {
		    try {
			return Float.parseFloat(str);
		    } catch (Exception e) {
			// 不规范的情况下，试一次double类型
			return Double.parseDouble(str);
		    }
		}
	    } else {
		if (str.indexOf("L") > -1 || str.indexOf("l") > -1) { // long类型
		    return Long.parseLong(str);
		} else {
		    try {
			return Integer.parseInt(str);
		    } catch (Exception e) {
			// 不规范的情况下，试一次long类型
			return Long.parseLong(str);
		    }
		}
	    }
	} else if (c == 'n') { // null类型
	    while (jsonBean.cursor < jsonBean.strlength) {
		c = jsonBean.getCurrentChar();
		if (isEndSymbol(c))
		    break; // 再次出现结束符时结束
		ret.append(c);
		jsonBean.next();
	    }
	    if ("null".equals(ret.toString().trim()))
		return null;
	    else
		throw new RuntimeException("null类型转换错误：cursor=" + jsonBean.cursor + " sep=" + ret.toString().trim());
	} else { // boolean类型
	    while (jsonBean.cursor < jsonBean.strlength) {
		c = jsonBean.getCurrentChar();
		if (isEndSymbol(c))
		    break; // 再次出现结束符时结束
		ret.append(c);
		jsonBean.next();
	    }
	    return Boolean.parseBoolean(ret.toString().trim());
	}
    }

//	/**
//	 * 过滤特殊字符
//	 */
//	private void filterSpecialChar() {
//		for (; cursor < strlength; cursor++) {
//			char c = this.getCurrentChar();
//			if (c == ' ' || c == '\n' || c == '\t') {
//				continue;
//			} else {
//				break;
//			}
//		}
//	}

    /**
     * 判断是否结束符或者“,”分隔符
     */
    private static boolean isEndSymbol(char sep) {
	return (',' == sep || huakuohao_e == sep || dakuohao_e == sep) ? true : false;
    }

    /**
     * 获取分隔符
     */
    private static char getSeparator(JsonBean jsonBean) {
	char sep;
	while (jsonBean.cursor < jsonBean.strlength) {
	    sep = jsonBean.getCurrentChar();
	    if (':' == sep || isEndSymbol(sep)) { // 分隔符
		return sep;
	    }
	    jsonBean.next();
	}
	throw new RuntimeException("在需要分隔符的地方，没有找到分隔符：cursor=" + jsonBean.cursor);
    }

    /**
     * 获取value的第一个字符
     */
    private static char getValueFirstChar(JsonBean jsonBean) {
	// TODO spe付初始值存在bug，不应该设置初始值
	char sep = 0;
	while (jsonBean.cursor < jsonBean.strlength) {
	    sep = jsonBean.getCurrentChar();
	    if (huakuohao_s == sep || dakuohao_s == sep || '"' == sep || '\'' == sep) { // 分隔符
		break;
	    }
	    if (('-' == sep || '+' == sep) || ('0' <= sep && sep <= '9') || ('a' <= sep && sep <= 'z') || ('A' <= sep && sep <= 'Z')) { // 值类型
		break;
	    }
	    if (douhao == sep)
		throw new RuntimeException("value开始符错误：cursor=" + jsonBean.cursor + " sep=" + sep);
	    jsonBean.next();
	}
	return sep;
    }

    /**
     * 获取key所要转换的bean的实例。同时初始化实例中的所有属性
     * 
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("rawtypes")
    private static Object getBean(JsonBean jsonBean, String key) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
	if (jsonBean.alias != null && jsonBean.alias.get(key) != null) { // 指定了key所对应数据的数据类型
	    Object t_bean = null;
	    Object clazz = jsonBean.alias.get(key);
	    if (clazz instanceof String)
		clazz = Class.forName((String) jsonBean.alias.get(key));
	    t_bean = ((Class) clazz).newInstance();
	    initBeanFields((Class) clazz);
	    return t_bean;
	} else
	    return null;
    }

    private static class JsonBean {
	/**
	 * json
	 */
	public String jsonString = null;
	/**
	 * 别名
	 */
	public Map<?, ?> alias = null;
	/**
	 * 游标
	 */
	public int cursor = 0;
	/**
	 * jsonString.length()
	 */
	public int strlength = 0;

	/**
	 * json与Bean对象互相转换
	 * 
	 * @param jsonString
	 * @param alias
	 */
	public JsonBean(String jsonString, Map<?, ?> alias) {
	    this.jsonString = jsonString;
	    this.alias = alias;
	    this.strlength = jsonString.length();
	    cursor = jsonString.indexOf(huakuohao_s);
	}

	/**
	 * 获取游标所在字符
	 */
	public char getCurrentChar() {
	    return jsonString.charAt(cursor);
	}

	/**
	 * 获取游标下一个字符
	 */
	public char getNextChar() {
	    return jsonString.charAt(cursor + 1);
	}

	/**
	 * 获取游标下一个有效字符，不是换行符、空格、制表符、结束符等。 过滤规则等同于String.trim()
	 */
	@SuppressWarnings("null")
	public char getNextValidChar() {
	    char sep = 0;
	    int len = cursor;
	    while (cursor < strlength) {
		sep = jsonString.charAt(len + 1);
		if (sep > ' ')
		    return sep;
		len++;
	    }

	    return (Character) null;
	}

	/**
	 * 游标向右移一位
	 */
	public void next() {
	    cursor++;
	}

	/**
	 * 游标向后移一位
	 */
	public void back() {
	    cursor--;
	}
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ json2Object 相关方法 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 公共方法 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 获取key所要转换的bean中的所有属性
     * 
     * @throws ClassNotFoundException
     */
    private static Map<String, Field> getFields(Object bean) {
	return ((Map<String, Field>) aliasFields.get(bean.getClass().getName()));
    }

    /**
     * 初始化bean中的所有属性到缓存中
     * 
     * @throws ClassNotFoundException
     */
    private static void initBeanFields(Class<?> clazz) {
	Map<String, Field> fields = (Map<String, Field>) aliasFields.get(clazz.getName());
	if (fields == null) {
	    synchronized (aliasFields) { // 初始化时并发造成bug
		fields = (Map<String, Field>) aliasFields.get(clazz.getName());
		if (fields != null)
		    return;
		fields = getAllFields(null, clazz);
		aliasFields.put(clazz.getName(), fields);
	    }
	}
    }

    private static ConcurrentHashMap<String, Map<String, Field>> aliasFields = new ConcurrentHashMap<String, Map<String, Field>>();

    /**
     * 获取Class(bean)中的所有属性
     */
    private static Map<String, Field> getAllFields(Map<String, Field> fields, Class<?> clazz) {
	if (fields == null) {
	    fields = new HashMap<String, Field>();
	}
	if (clazz.getSuperclass() != null) {
	    Field[] fieldsSelf = clazz.getDeclaredFields();
	    for (Field field : fieldsSelf) {
		if (!Modifier.isFinal(field.getModifiers())) {
		    if (fields.get(field.getName()) != null) {
			log.error("这里发现了一个严重的错误，请联系管理员。错误如下：" + clazz);
			// 当出现继承关系时，子类与父类中共同的变量名称，使用子类的。
			continue;
		    }
		    fields.put(field.getName(), field);
		}
	    }
	    getAllFields(fields, clazz.getSuperclass());
	}
	return fields;
    }

    /**
     * 是否是基础数据类型，或者基础数据类型的封装类
     * 
     * @return true/false
     */
    private static boolean isBasicDataType(Class<?> type) {
	boolean flag = false;
	if (type == String.class) {
	    flag = true;
	} else if (type == int.class || type == Integer.class) {
	    flag = true;
	} else if (type == boolean.class || type == Boolean.class) {
	    flag = true;
	} else if (type == long.class || type == Long.class) {
	    flag = true;
	} else if (type == float.class || type == Float.class) {
	    flag = true;
	} else if (type == double.class || type == Double.class) {
	    flag = true;
	} else if (type == byte.class || type == Byte.class) {
	    flag = true;
	} else if (type == short.class || type == Short.class) {
	    flag = true;
	}
	return flag;
    }

    /**
     * @Title: converChar @Description: Json中特殊字符转换 @param @param value
     *         json @param @return 设定文件 @return Object 返回类型 @throws
     */
    private static Object converChar(String value) {
	value = value.replace("\\\"", "&quot;");
	value = value.replace("\"", "\\\"");
	value = value.replace("\n", "\\n");
	value = value.replace("\r", "\\r");
	value = value.replace("&quot;", "\\\"");
	return value;
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 公共方法 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
