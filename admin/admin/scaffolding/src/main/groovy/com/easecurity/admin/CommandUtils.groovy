package com.easecurity.admin

import grails.codegen.model.Model
import grails.core.GrailsApplication
import grails.persistence.Event
import grails.util.Holders
import org.apache.commons.lang3.StringUtils
import org.grails.datastore.mapping.model.types.ToMany
import org.grails.datastore.mapping.model.types.ToOne
import org.grails.io.support.Resource

/**
 * GenerateVueCommand和GenerateAsyncControllerCommand获取模板用的Model
 */
class CommandUtils {

    public static Map getModel(Resource sourceClass, Model model, GrailsApplication grailsApplication) {
        def binding = model.asMap()
        binding['ctx'] = grailsApplication
        binding['grailsApplication'] = grailsApplication
        binding['domainClass'] = grailsApplication.getDomainClass(model.fullName)

        binding['excludedProps'] = Event.allEvents.toList() << 'version' << 'deleted'
        binding['defaultProps'] = ['dateCreated', 'lastUpdated', 'createdBy', 'updatedBy']

        binding.domainClass.isAutowire()
        binding['persistentPropNames'] = binding.domainClass.persistentEntity.persistentPropertyNames
        binding['constrainedProperties'] = binding.domainClass.constrainedProperties

        binding['hasMany'] = []
        binding['hasOne'] = []

        binding['mdView'] = '@/views/md'

        def associations = [:]
        binding.domainClass.persistentEntity.getAssociations()?.each { it ->
            associations[it.name] = it
            if (it instanceof ToOne) {
                String className = it.type.getSimpleName()
                binding.hasOne << [name: it.name, className: className, propertyName: StringUtils.uncapitalize(className), typeName: it.type.name, association: it]
            }
            if (it instanceof ToMany && it.getAssociatedEntity()) {
                String className = it.getAssociatedEntity().getJavaClass().getSimpleName()
                binding.hasMany << [name: it.name, className: className, propertyName: StringUtils.uncapitalize(className), typeName: it.getAssociatedEntity().name, association: it]
            }
        }
        binding['mapping'] = binding.domainClass.persistentEntity.getMapping().mappedForm.getPropertyConfigs()

        // TODO 有一个bug，主键id名是写死的，如果是联合主键或者不叫id，vue模板主键的restful将有bug
        def props = binding.domainClass.properties.propertyDescriptors.findAll { it ->
            if (binding.persistentPropNames.contains(it.name) || 'id'.equals(it.name)) {
                return !binding.excludedProps.contains(it.name) && (binding.domainClass.constrainedProperties[it.name] ? binding.domainClass.constrainedProperties[it.name].display : true)
            } else if (binding.constrainedProperties[it.name]?.display) {
                return true
            }
            return false
        }
        Collections.sort(props, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return compare2(o1, o2, binding.domainClass.constrainedProperties, binding.domainClass)
            }
        })
        binding['props'] = props

        binding['enumClass'] = [:]
        props.each { it ->
//            println(it.propertyType)
//            println(it.propertyType.isEnum())
            if (it.propertyType.isEnum()) {
                binding.enumClass[it.name] = it.propertyType
            } else if (it.propertyType.isArray()) {
//                println(it.propertyType.getComponentType().isEnum())
                if (it.propertyType.getComponentType().isEnum())
                    binding.enumClass[it.name] = it.propertyType.getComponentType()
            } else if (it.propertyType == java.util.List && associations[it.name].componentType.isEnum()) {
                binding.enumClass[it.name] = associations[it.name].componentType
            }
//                println(associations[it.name].properties)
//                println(associations[it.name].componentType)
//                println(associations[it.name].componentType.isEnum())
//            if (it.propertyType == java.util.Set) {
//                println(associations[it.name].properties)
////                println(associations[it.name].componentType)
////                println(associations[it.name].componentType.isEnum())
////                return associations[it.name].componentType.isEnum()
//                println(associations[it.name].getAssociatedEntity().getJavaClass().isEnum())
//                return associations[it.name].getAssociatedEntity().getJavaClass().isEnum()
//            }
        }
        binding['i18n'] = generateI18n(binding.domainClass, binding.props)
        println(binding.i18n)
        binding['hasHibernate'] = Holders.pluginManager.hasGrailsPlugin('hibernate') || Holders.pluginManager.hasGrailsPlugin('hibernate5')

        return binding
    }

    private static int compare2(Object o1, Object o2, Map constrainedProperties, def domainClass) {
        if (o1.getName().equals('id')) {
            return 1;
        }
        if (o2.getName().equals('id')) {
            return -1;
        }

        def prop1 = o1;
        def prop2 = o2;

        def cp1 = constrainedProperties.get(prop1.getName());
        def cp2 = constrainedProperties.get(prop2.getName());

        if (cp1 == null & cp2 == null) {
            return prop1.getName().compareTo(prop2.getName());
        }

        if (cp1 == null) {
            return 1;
        }

        if (cp2 == null) {
            return -1;
        }

        if (cp1.getOrder() > cp2.getOrder()) {
            return 1;
        }

        if (cp1.getOrder() < cp2.getOrder()) {
            return -1;
        }

        return 0;
    }

    /**
     * 生成i18n消息
     */
    private static def generateI18n(domainClass, properties) {
        def props2 = [:]
        def excludes = ["id", 'dateCreated', 'lastUpdated', "updatedBy", "createdBy"]
        // 打印Domain Class的注释信息
        def fullName = domainClass.fullName
        def naturalInfo = getNaturalInfo(domainClass);
        if (naturalInfo == null) return;    // 返回null，没找到源文件！
        props2['pageTitle'] = getNaturalInfo(domainClass, null, naturalInfo.lines).naturalName
//        println domainClass.fullName + "=" + getNaturalInfo(domainClass, null, naturalInfo.lines).naturalName;
        // 遍历属性，并获得其所在源代码中的行号、注释信息
        def props = []
        for (prop in properties) {
            if (excludes.contains(prop.name)) continue;
            props << getNaturalInfo(domainClass, prop.name, naturalInfo.lines);
        }
        // 拼装属性
        for (p in (props - null).sort { it.line }) {
            props2[p.name] = p.naturalName
//            println domainClass.fullName + "." + p.name + "=" + p.naturalName;
//            def cp = domainClass.constrainedProperties[p.name];
//            if ( cp && cp.blank!=null && cp.blank==false ) {
//                println domainClass.fullName + "." + p.name + ".blank=【" + p.naturalName + "】不能为空！";
//            } else if ( cp && cp.nullable!=null && cp.nullable==false ) {
//                println domainClass.fullName + "." + p.name + ".null=【" + p.naturalName + "】不能为空！";
//            }
        }
        props2
    }

    /**
     * 从源代码的注释中取类和属性的自然语言名称
     * 注意注释必须为 /** ... 式样的，如果有多行，则名称必须在第一行
     *
     * @return map [line:x, name:x, naturalName:x, lines:lines]
     */
    private static def getNaturalInfo(domainClass, propName = null, List lines = null) {
        if (lines == null) {
            String filename = domainClass.fullName.replace(".", "/") + ".groovy"
            String path
            def an = domainClass.clazz.getAnnotation(I18nSource.class)
            if (an != null) { // 使用源码注解的
                path = an.value()
            } else {
                // 计算源代码的文件路径
                path = "./grails-app/domain/" + filename;
                path = path.replace("/", System.getProperty("file.separator"));
            }
            File file = new File(path);
            if (!file.exists()) {
                // 到插件中找找看
                List files = (new groovy.util.FileNameFinder()).getFileNames('./plugins/', '**/grails-app/domain/' + filename);
                if (files) file = new File(files[0]);
                else println "找不到源文件：" + filename;
            }
            if (!file.exists()) return null;    // 返回null，没找到源文件！
            // 读取源代码：必须是UTF-8编码
            lines = file.readLines("UTF-8");
        }
        // 解析源代码
        for (int i = 0; i < lines.size(); i++) {
            String l = lines[i];
            // 类定义
            if (!propName && l =~ /class[\s]+${domainClass.name}[\s{]+/) {
                // 去掉类上方的注解和空行
                List temp = []
                temp.addAll(lines)
                int di = 0
                for (int j = i; j > 0; j--) {
                    String ll = temp[j].trim()
                    if (ll.isEmpty() || ll.startsWith('@')) {
                        temp.removeAt(j)
                        di++
                    }
                }
//                println new String(getCommentName(temp, i - di).getBytes("UTF-8"))
                def naturalName = getCommentName(temp, i - di)
                if (naturalName == null)
                    naturalName = domainClass.name
                return [line: i, naturalName: naturalName, lines: lines];
            }
            // 属性定义
            if (propName && l =~ /[\s\[]+${propName}[\s,;:]*/) {
                def naturalName = getCommentName(lines, i)
                if (naturalName == null)
                    naturalName = StringUtils.capitalize(propName)
                return [line: i, name: propName, naturalName: naturalName, lines: lines];
            }
        }
    }

    private static def getCommentName(List lines, i) {
        for (int j = i - 1; j >= 0; j--) {
            // 如果往上超过2行没找到注释，则认为没有
            if (j <= i - 2 && lines[j].indexOf("*") == -1) {
                break;
            }
            // 找到注释开始行
            if (lines[j] =~ /\/\*\*/) {
                // 如果就一行注释，则直接将中间的内容返回
                if (lines[j] =~ /\*\//) {
                    return lines[j].replaceAll("\\/", "").replaceAll("\\*", "").trim();
                }
                // 否则往下找第一行
                else {
                    for (int k = j + 1; k < i; k++) {
                        if (lines[k] =~ /\*\s*[^\s]+/)
                            return lines[k].replaceAll("\\*", "").trim();
                    }
                }
                break;
            }
        }
        return null;
    }
}
