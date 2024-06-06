<%
def vue = com.easecurity.admin.utils.VueTaglibUtils
def hasManyNames = hasMany*.name
def hasManyClassNames = hasMany*.className
def hasManyPropertyNames = hasMany*.propertyName
def hasOneNames = hasOne*.name
def hasOneClassNames = hasOne*.className
def hasOnePropertyNames = hasOne*.propertyName
def oneAndManyClassNames = []
oneAndManyClassNames.addAll(hasManyClassNames)
oneAndManyClassNames.addAll(hasOneClassNames)
oneAndManyClassNames.unique()
def oneAndManyPropertyNames = []
oneAndManyPropertyNames.addAll(hasManyPropertyNames)
oneAndManyPropertyNames.addAll(hasOnePropertyNames)
oneAndManyPropertyNames.unique()
def props2 = props.findAll { it ->
  def extId = it.name.indexOf('Id')>-1 ? it.name.replace('Ids', '').replace('Id', '') : '1'
  if ('id'.equals(it.name) || hasManyNames.contains(extId) || hasOneNames.contains(extId)) return false
  else return true
}
%>
<template>
  <el-dialog :title="\$t('domain.def.' + config.eStatus)+'——'+\$t('domain.${propertyName}.pageTitle')" :visible.sync="config.eVisible" :append-to-body="config.appendToBody" width="80%" class="edit-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container edit-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="edit-row">
<%
for (int i=0; i<props2.size(); i++) {
%>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'${props2[i].name}'|i18n('${propertyName}')" prop="${props2[i].name}">
                ${vue.editFieldHTML(domainClass, mapping, hasMany, hasOne, props2[i], "temp.", "temp."+props2[i].name+".id")}
              </el-form-item>
            </el-col>
<%
}
%>
          </el-row>
        </el-card>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="config.eVisible = false">
        {{ \$t('domain.def.cancel') }}
      </el-button>
      <el-button type="primary" :disabled="loading || deleteLoading" :loading="saveLoading" @click="config.eStatus == 'create' ? createData() : updateData()">
        {{ \$t('domain.def.save') }}
      </el-button>
      <el-button type="danger" :disabled="loading || saveLoading" :loading="deleteLoading" @click="deleteData">
        {{ \$t('domain.def.delete') }}
      </el-button>
    </div>
<%
for (int i=0; i<oneAndManyClassNames.size(); i++) {
%>
    <${oneAndManyClassNames[i]}ShowView :config="childrenConfig.${oneAndManyPropertyNames[i]}" />
    <${oneAndManyClassNames[i]}SelectView :config="childrenConfig.${oneAndManyPropertyNames[i]}" />
    <${oneAndManyClassNames[i]}EditView :config="childrenConfig.${oneAndManyPropertyNames[i]}" />
<%
}
%>
  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: '${className}Edit',
<%
if (!oneAndManyClassNames.isEmpty()){
%>
  components: {
<%
  for (int i=0; i<oneAndManyClassNames.size(); i++) {
%>
    ${oneAndManyClassNames[i]}ShowView: () => import ('${mdView}/${oneAndManyPropertyNames[i]}/show'),
    ${oneAndManyClassNames[i]}SelectView: () => import ('${mdView}/${oneAndManyPropertyNames[i]}/select'),
    ${oneAndManyClassNames[i]}EditView: () => import ('${mdView}/${oneAndManyPropertyNames[i]}/edit')${i<oneAndManyClassNames.size()-1?',':''}
<%
  }
%>
  },
<%
}
%>
  props: {
    config: {
      type: Object,
      default: () => {
        return {
          id: undefined,
          domainName: undefined,
          eStatus: undefined,
          eVisible: undefined,
          eCallBack: undefined
        }
      }
    }
  },
  data() {
    return {
      ...baseEdit.data,
      // 表单数据
      temp: {${vue.editFieldTemp(domainClass, hasMany, hasOne, props)}},
      // 表单初始化数据，新建页面打开默认值
      tempDef: {${vue.editFieldTempDef(domainClass, hasMany, hasOne, props)}},
      enumClasses: {
<%
enumClass.eachWithIndex { entry, i ->
%>
        '${entry.value.name}': this.getEnum('${entry.value.name}')${i<enumClass.size()-1?',':''}
<%
}
%>
      },
      params: {},
      // 页面校验规则
      rules: {
<%
for (int i=0; i<props2.size(); i++) {
%>
        ${props2[i].name}: [${vue.editFieldRules(domainClass, hasMany, hasOne, props2[i], constrainedProperties[props2[i].name])}]${i<props2.size()-1?',':''}
<%
}
%>
      },
      childrenConfig: {
<%
for (int i=0; i<oneAndManyPropertyNames.size(); i++) {
%>
        ${oneAndManyPropertyNames[i]}: {
          id: undefined,
          domainName: '${oneAndManyPropertyNames[i]}',
          props: ['id'], // select.vue中显示的列，列如['id','name']
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.${oneAndManyPropertyNames[i]}.id, null, this.childrenConfig.${oneAndManyPropertyNames[i]}.domainName)
            }
          },
          seVisible: false,
          seCallBack: (prop, values, hasMany) => {
            if (values) {
              if (hasMany) this.temp[prop] ? this.temp[prop] = this.temp[prop].concat(values) : this.temp[prop] = values
              else this.temp[prop] = values[0]
            }
          },
          eStatus: 'update',
          eVisible: false,
          eCallBack: (refresh) => {
            if (refresh) {
              this.getData()
            }
          }
        }${i<oneAndManyPropertyNames.size()-1?',':''}
<%
}
%>
      },
      loading: false,
      saveLoading: false,
      deleteLoading: false
    }
  },
  computed: {
    ...baseEdit.computed
  },
  created() {
    // this.params.deep = true // 如果需要显示子类中的属性，可以放开，但是会出现N+1数据库查询问题，影响性能
  },
  methods: {
    ...baseEdit.methods
  }
}
</script>

<style lang="scss">
@import "~@/styles/base.scss";
</style>

<style lang="scss" scoped>
@import "~@/styles/baseEdit.scss";
</style>
