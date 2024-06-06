package com.easecurity.admin.utils

import grails.validation.ConstrainedDelegate
import org.grails.orm.hibernate.cfg.PropertyConfig

/**
 * GenerateVueCommand根据模板自动生成vue标签相关的工具类
 */
class VueTaglibUtils {
    /**
     * show属性HTML标签
     */
    static String showFieldHTML(def domainClass, List hasMany, List hasOne, def p, String prefix, String showId, boolean click = false) {
        def hasManyNames = hasMany ? hasMany*.name : []
        def hasOneNames = hasOne ? hasOne*.name : []
//        <span>{{ row.id }}</span>
//        <span class="link-type" @click="handleShow(row.id)">{{ row.gkey }}</span>
//        <span>{{ row.validTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
//                <span class="link-type span-inline" @click="handleShowChildren(temp.role.id, 'role', 'AdminUser')">{{ temp.role }}</span>
        String spanValue = "{{ ${prefix + p.name} }}"
        if (p.propertyType == Boolean || p.propertyType == boolean) {
            spanValue = showBooleanValue(p, prefix, showId, click)
        } else if (p.propertyType.isEnum()) {
            spanValue = showEnumValue(p, prefix, showId, click)
        }

        String out = click ? "<span class=\"link-type\" @click=\"handleShow(${showId})\">${spanValue}</span>" : "<span>${spanValue}</span>"
        if (hasManyNames.contains(p.name))
            out = showHasManyValue(p, prefix, hasMany[hasManyNames.indexOf(p.name)].association)
        if (hasOneNames.contains(p.name))
            out = showHasOneValue(p, prefix, hasOne[hasOneNames.indexOf(p.name)].association)
        out
    }

    /**
     * edit属性HTML标签
     */
    static String editFieldHTML(def domainClass, Map<String, PropertyConfig> mapping, List hasMany, List hasOne, def p, String prefix, String showId) {
        def hasManyNames = hasMany ? hasMany*.name : []
        def hasOneNames = hasOne ? hasOne*.name : []
//        <el-input id="gkey" v-model="temp.gkey" />
//        <el-date-picker id="validTime" v-model="temp.validTime" type="datetime" placeholder="Please pick a date" />
        String out = "<el-input id=\"${p.name}\" v-model=\"${prefix + p.name}\" clearable />"
        if (p.propertyType == Boolean || p.propertyType == boolean)
            out = editBooleanValue(p, prefix, showId)
        else if (p.propertyType == java.util.Date || p.propertyType == java.sql.Date)
            out = editDateValue(p, prefix, showId)
        else if (p.propertyType.isEnum())
            out = editEnumValue(mapping, p, prefix, showId)
        else if (hasManyNames.contains(p.name))
            out = editHasManyValue(domainClass, p, prefix, hasMany[hasManyNames.indexOf(p.name)].association)
        else if (hasOneNames.contains(p.name))
            out = editHasOneValue(domainClass, p, prefix, hasOne[hasOneNames.indexOf(p.name)].association)
        out
    }

    /**
     * edit属性temp标签
     */
    static String editFieldTemp(def domainClass, List hasMany, List hasOne, def props) {
        StringBuffer out = new StringBuffer()
        hasOne.each { it ->
            out << " ${it.name}: null,"
        }
        hasMany.each { it ->
            out << " ${it.name}: null,"
        }
        if (out.length() > 0) return out.substring(0, out.length() - 1) + " "
        else return out.toString()
    }

    /**
     * edit属性tempDef标签
     */
    static String editFieldTempDef(def domainClass, List hasMany, List hasOne, def props) {
        StringBuffer out = new StringBuffer()
        def instance = domainClass.newInstance()
        props.each { it ->
            if (instance.properties[it.name] != null) {
                if (it.propertyType == String || it.propertyType.isEnum() )
                    out << " ${it.name}: '${instance.properties[it.name]}',"
                else
                    out << " ${it.name}: ${instance.properties[it.name]},"
            }
        }
        if (out.length() > 0) return out.substring(0, out.length() - 1) + " "
        else return out.toString()
    }

    /**
     * edit属性rules标签
     */
    static String editFieldRules(def domainClass, List hasMany, List hasOne, def p, ConstrainedDelegate constrainedProperty) {
//        gkey: [{ required: true, message: 'gkey is required', trigger: 'change' }],
        StringBuffer out = new StringBuffer()
        String trigger = p.propertyType == String ? 'blur' : 'change'
        // 必填性
        if (!constrainedProperty.isBlank() || !constrainedProperty.isNullable()) out << "{ required: true, message: '${p.name} is required', trigger: '${trigger}' }, "
        // 邮件
        if (p.propertyType == String && constrainedProperty.isEmail()) out << "{ type: 'email', message: '${p.name} must be a email', trigger: '${trigger}' }, "
        // url
        if (p.propertyType == String && constrainedProperty.isUrl()) out << "{ type: 'url', message: '${p.name} must be a url', trigger: '${trigger}' }, "
        // boolean
        if (p.propertyType == Boolean || p.propertyType == boolean) out << "{ type: 'boolean', message: '${p.name} must be boolean', trigger: '${trigger}' }, "
        // float
        if (p.propertyType == Float || p.propertyType == float || p.propertyType == Double || p.propertyType == double) out << "{ type: 'float', message: '${p.name} must be float', trigger: '${trigger}' }, "
        // integer
        if (p.propertyType == Integer || p.propertyType == int || p.propertyType == Long || p.propertyType == long) out << "{ type: 'integer', message: '${p.name} must be integer', trigger: '${trigger}' }, "
        // date
//        if (p.propertyType == java.util.Date || p.propertyType == java.sql.Date) out << "{ type: 'string', message: '${p.name} must be date', trigger: '${trigger}' },"
        // array
        if (p.propertyType.isArray() || p.propertyType == java.util.List || p.propertyType == java.util.Set) out << "{ type: 'array', message: '${p.name} must be array', trigger: '${trigger}' }, "
        // min和max
        Range range = constrainedProperty.getSize() ? constrainedProperty.getSize() : constrainedProperty.getRange()
        if (range) out << "{ min: ${range.getFrom()}, max: ${range.getTo()}, message: '${p.name} must be range(min: ${range.getFrom()}, max: ${range.getTo()})', trigger: '${trigger}' }, "

        if (out.length() > 0) return out.substring(0, out.length() - 2)
        else return out.toString()
    }

    /**
     * search属性HTML标签
     */
    static String searchFieldHTML(def domainClass, List hasMany, List hasOne, def p) {
//        <el-input v-model="tableQuery.search.${props[i].name}" type="text" clearable />
        String out = "<el-input v-model=\"tableQuery.search.${p.name}\" type=\"text\" clearable />"
    }

    private static String showHasManyValue(def p, String prefix, def association) {
        "<span v-for=\"(item, index) in ${prefix + p.name}\" :key=\"index\" class=\"link-type span-inline\" @click=\"handleShowChildren(item.id, '${p.name}', '${association.getAssociatedEntity().getJavaClass().getSimpleName()}')\">{{ item }}</span>"
    }

    private static String editHasManyValue(def domainClass, def p, String prefix, def association) {
//<div v-for="(item, index) in temp.roles" :key="index" class="span-inline">
//<span class="link-type" @click="handleShowChildren(item.id, 'roles', 'AdminRole')">{{ item }}</span>
//<el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(item.id, 'roles', 'AdminRole', true)">删除</el-link>
//</div>
//<el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('roles', 'AdminRole', 'adminUser', temp.id, true)">添加</el-link>
        StringBuffer out = new StringBuffer()
        out << "<div v-for=\"(item, index) in ${prefix}${p.name}\" :key=\"index\" class=\"span-inline\">"
        out << "<span class=\"link-type\" @click=\"handleShowChildren(item.id, '${p.name}', '${association.getAssociatedEntity().getJavaClass().getSimpleName()}')\">{{ item }}</span>"
        out << "<el-link type=\"danger\" icon=\"el-icon-delete\" @click=\"handleDeleteChildren(item.id, '${p.name}', '${association.getAssociatedEntity().getJavaClass().getSimpleName()}', true)\">{{ \$t('domain.def.delete') }}</el-link>"
        out << "</div>"
        out << "<el-link type=\"primary\" icon=\"el-icon-edit\" @click=\"handleCreateChildren('${p.name}', '${association.getAssociatedEntity().getJavaClass().getSimpleName()}', '${domainClass.propertyName}', ${prefix}id, true)\">{{ \$t('domain.def.add') }}</el-link>"
        out.toString()
    }

    private static String showHasOneValue(def p, String prefix, def association) {
        "<span class=\"link-type span-inline\" @click=\"handleShowChildren(${prefix}${p.name}.id, '${p.name}', '${association.getAssociatedEntity().getJavaClass().getSimpleName()}')\">{{ ${prefix + p.name} }}</span>"
    }

    private static String editHasOneValue(def domainClass, def p, String prefix, def association) {
//<div class="span-inline">
//<span class="link-type" @click="handleShowChildren(temp.user.id, 'user', 'AdminUser')">{{ temp.user }}</span>
//<el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(item.id, 'user', 'AdminUser')">删除</el-link>
//</div>
//<el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('user', 'AdminUser', 'adminUser', temp.id)">添加</el-link>

        StringBuffer out = new StringBuffer()
        out << "<div class=\"span-inline\">"
        out << "<span class=\"link-type\" @click=\"handleShowChildren(${prefix}${p.name}.id, '${p.name}', '${association.getAssociatedEntity().getJavaClass().getSimpleName()}')\">{{ ${prefix + p.name} }}</span>"
        out << "<el-link type=\"danger\" icon=\"el-icon-delete\" @click=\"handleDeleteChildren(${prefix}${p.name}.id, '${p.name}', '${association.getAssociatedEntity().getJavaClass().getSimpleName()}')\">{{ \$t('domain.def.delete') }}</el-link>"
        out << "</div>"
        out << "<el-link type=\"primary\" icon=\"el-icon-edit\" @click=\"handleCreateChildren('${p.name}', '${association.getAssociatedEntity().getJavaClass().getSimpleName()}', '${domainClass.propertyName}', ${prefix}id)\">{{ \$t('domain.def.add') }}</el-link>"
        out.toString()
    }

    private static String showBooleanValue(def p, String prefix, String showId, boolean click) {
        "{{ ${prefix + p.name} | fmatBoolean }}"
    }

    private static String editBooleanValue(def p, String prefix, String showId) {
//        <el-switch id="enabled" v-model="temp.enabled" :active-text="'true'|i18n('adminUser')" :inactive-text="'false'|i18n('adminUser')" />
//        <el-radio v-model="temp.enabled" :label="true">{{ 'true'|fmatBoolean }}</el-radio>
        StringBuffer out = new StringBuffer()
        out << "<el-radio-group id=\"${p.name}\" v-model=\"${prefix}${p.name}\">"
        out << "<el-radio :label=\"true\">{{ 'true'|fmatBoolean }}</el-radio>"
        out << "<el-radio :label=\"false\">{{ 'false'|fmatBoolean }}</el-radio>"
        out << "</el-radio-group>"
        out.toString()
    }

    private static String showEnumValue(def p, String prefix, String showId, boolean click) {
        "{{ ${prefix + p.name} | fmatEnum(enumClasses['${p.propertyType.name}']) }}"
    }

    private static String editEnumValue(Map<String, PropertyConfig> mapping, def p, String prefix, String showId) {
        String enumType = mapping[p.name].getEnumType()?.toLowerCase()
        StringBuffer out = new StringBuffer()
        out << "<el-select id=\"${p.name}\" v-model=\"${prefix}${p.name}\" :placeholder=\"\$t('domain.def.selectPlaceholder')\">"
        out << "<el-option v-for=\"(item, index) in enumClasses['${p.propertyType.name}']\" :key=\"index\" :label=\"item.title\" :value=\"item.code\" />"
//        if ("ordinal".equals(enumType))
//            out << "<el-option v-for=\"(item, index) in enumClasses['${p.propertyType.name}']\" :key=\"item.code\" :label=\"item.title\" :value=\"index\"></el-option>"
//        else
//            out << "<el-option v-for=\"(item, index) in enumClasses['${p.propertyType.name}']\" :key=\"item.code\" :label=\"item.title\" :value=\"item.code\"></el-option>"
        out << "</el-select>"
        out.toString()
    }

    private static String editDateValue(def p, String prefix, String showId) {
        "<el-date-picker id=\"${p.name}\" v-model=\"${prefix + p.name}\" type=\"datetime\" placeholder=\"Please pick a date\" value-format=\"yyyy-MM-dd HH:mm:ss\" />"
    }
}

