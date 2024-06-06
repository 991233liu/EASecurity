<%
def hasManyNames = hasMany*.name
def hasManyClassNames = hasMany*.className
def hasManyPropertyNames = hasMany*.propertyName
def hasOneNames = hasOne*.name
def hasOneClassNames = hasOne*.className
def hasOnePropertyNames = hasOne*.propertyName
def props2 = props.findAll { it ->
  def extId = it.name.indexOf('Id')>-1 ? it.name.replace('Ids', '').replace('Id', '') : '1'
  if (hasManyNames.contains(extId) || hasOneNames.contains(extId)) return false
  else return true
}
%>

${className}

${propertyName}

${ctx}

${grailsApplication}

${System.getProperties()}

${domainClass}

${domainClass.properties}


${domainClass.persistentEntity.persistentPropertyNames}

${hasMany}

${hasOne}

${props}

${props2}

${enumClass}

