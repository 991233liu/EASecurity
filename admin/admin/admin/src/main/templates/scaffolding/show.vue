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
  <el-dialog :title="\$t('domain.def.show')+'——'+\$t('domain.${propertyName}.pageTitle')" :visible.sync="config.sVisible" :append-to-body="config.appendToBody" width="80%" class="show-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container show-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="show-row">
<%
for (int i=0; i<props2.size(); i++) {
%>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'${props2[i].name}'|i18n('${propertyName}')">
                ${vue.showFieldHTML(domainClass, hasMany, hasOne, props2[i], "temp.", "temp."+props2[i].name+".id")}
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
      <el-button @click="config.sVisible = false">
        {{ \$t('domain.def.cancel') }}
      </el-button>
      <el-button type="primary" :disabled="loading || deleteLoading" @click="editData">
        {{ \$t('domain.def.edit') }}
      </el-button>
      <el-button type="danger" :disabled="loading" :loading="deleteLoading" @click="deleteData">
        {{ \$t('domain.def.delete') }}
      </el-button>
    </div>
<%
for (int i=0; i<oneAndManyClassNames.size(); i++) {
%>
    <${oneAndManyClassNames[i]}ShowView :config="childrenConfig.${oneAndManyPropertyNames[i]}" />
    <${oneAndManyClassNames[i]}EditView :config="childrenConfig.${oneAndManyPropertyNames[i]}" />
<%
}
%>
  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseShow from '@/utils/baseShow' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: '${className}Show',
<%
if (!oneAndManyClassNames.isEmpty()){
%>
  components: {
<%
  for (int i=0; i<oneAndManyClassNames.size(); i++) {
%>
    ${oneAndManyClassNames[i]}ShowView: () => import ('${mdView}/${oneAndManyPropertyNames[i]}/show'),
    ${oneAndManyClassNames[i]}EditView: () => import ('${mdView}/${oneAndManyPropertyNames[i]}/edit')${i<oneAndManyPropertyNames.size()-1?',':''}
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
          appendToBody: false,
          sVisible: undefined,
          sCallBack: undefined
        }
      }
    }
  },
  data() {
    return {
      ...baseShow.data,
      // 表单数据
      temp: {},
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
      childrenConfig: {
<%
for (int i=0; i<oneAndManyPropertyNames.size(); i++) {
%>
        ${oneAndManyPropertyNames[i]}: {
          id: undefined,
          domainName: '${oneAndManyPropertyNames[i]}',
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
      deleteLoading: false
    }
  },
  computed: {
    ...baseShow.computed
  },
  created() {
    // this.params.deep = true // 如果需要显示子类中的属性，可以放开，但是会出现N+1数据库查询问题，影响性能
  },
  methods: {
    ...baseShow.methods
  }
}
</script>

<style lang="scss">
@import "~@/styles/base.scss";
</style>

<style lang="scss" scoped>
@import "~@/styles/baseShow.scss";
</style>
