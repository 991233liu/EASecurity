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
  if (hasManyNames.contains(extId) || hasOneNames.contains(extId)) return false
  else return true
}
%>
<template>
  <el-form ref="vForm" label-position="right" label-width="auto" @submit.native.prevent>
    <div class="card-container search-card">
      <el-card shadow="never">
        <div slot="header" class="card-header">
          <span>{{ \$t('domain.${propertyName}.pageTitle') }}</span>
        </div>
        <el-row :gutter="10" align="middle">
<%
for (int i=0; i<props2.size() && i<5; i++) {
%>
          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'${props2[i].name}'|i18n('${propertyName}')">
              ${vue.searchFieldHTML(domainClass, hasMany, hasOne, props2[i])}
            </el-form-item>
          </el-col>
<%
}
%>
          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item label-width="0" class="form-button-item">
              <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
                {{ \$t('table.search') }}
              </el-button>
              <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">
                {{ \$t('table.add') }}
              </el-button>
              <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" :disabled="listLoading || buttonDisabled" @click="handleDownload">
                {{ \$t('table.export') }}
              </el-button>
              <el-button class="filter-item" type="danger" icon="el-icon-delete" :disabled="listLoading || buttonDisabled" @click="handleDeleteAll">
                {{ \$t('table.delete') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <div class="card-container list-card">
      <el-card style="{width: 100% !important}" shadow="never">
        <el-table
          ref="tableRef"
          v-loading="listLoading"
          :data="list"
          stripe
          border
          highlight-selection-row
          style="width: 100%;"
          @sort-change="sortChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column type="index" :label="\$t('table.id')" :index="tableIndex" width="55" align="center" />
<%
for (int i=0; i<props2.size(); i++) {
  int width = 100
  if (props2[i].propertyType instanceof String) {
    width = 200
  }
  boolean showClick = (i<2 && !hasManyNames.contains(props2[i].name) && !hasOneNames.contains(props2[i].name)) ? true : false
%>
          <el-table-column v-if="config.props.${props2[i].name}.visible" :label="'${props2[i].name}'|i18n('${propertyName}')" prop="${props2[i].name}" sortable="custom" min-width="${width}px" align="center">
            <template slot-scope="{row}">
              ${vue.showFieldHTML(domainClass, hasMany, hasOne, props2[i], "row.", "row.id", showClick)}
            </template>
          </el-table-column>
<%
}
%>
          <el-table-column align="center" min-width="150" class-name="small-padding fixed-width">
            <template #header>
              <span>{{ \$t('table.actions') }} </span>
              <el-popover ref="actPopover" placement="bottom" :title="\$t('table.actPopover')" width="350" trigger="click">
                <el-card shadow="never">
                  <el-col v-for="(v, k) in config.props" :key="k" :md="12" :sm="12" :xs="24" class="grid-cell">
                    <el-checkbox v-model="config.props[k].visible">
                      {{ v.title }}
                    </el-checkbox>
                  </el-col>
                </el-card>
                <div class="static-content-item">
                  <el-button type="primary" size="mini" @click="headerReset">{{ \$t('table.reset') }}</el-button>
                </div>
              </el-popover>
              <el-link v-popover:actPopover :underline="false"><svg-icon icon-class="list2" /></el-link>
            </template>
            <template slot-scope="{row}">
              <el-button type="primary" size="mini" @click="handleUpdate(row.id)">
                {{ \$t('table.edit') }}
              </el-button>
              <el-button size="mini" type="danger" @click="handleDelete(row.id)">
                {{ \$t('table.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="tableQuery.max" style="padding: 0px 16px" @pagination="getData" />
      </el-card>
    </div>
    <ShowView :config="config" />
    <EditView :config="config" />
<%
for (int i=0; i<oneAndManyClassNames.size(); i++) {
%>
    <${oneAndManyClassNames[i]}ShowView :config="childrenConfig.${oneAndManyPropertyNames[i]}" />
    <${oneAndManyClassNames[i]}EditView :config="childrenConfig.${oneAndManyPropertyNames[i]}" />
<%
}
%>
  </el-form>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { i18nLabel as i18n } from '@/utils/i18n'
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseIndex from '@/utils/baseIndex' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可
import ShowView from './show'
import EditView from './edit'
<%
for (int i=0; i<oneAndManyClassNames.size(); i++) {
%>
import ${oneAndManyClassNames[i]}ShowView from '${mdView}/${oneAndManyPropertyNames[i]}/show'
import ${oneAndManyClassNames[i]}EditView from '${mdView}/${oneAndManyPropertyNames[i]}/edit'
<%
}
%>
export default {
  name: '${className}List', // name值需要与router中的name相同，否则页签的“keep-alive”功能不生效
<%
def componentstr = ''
if (oneAndManyClassNames.size()==1)
  componentstr = ', '+oneAndManyClassNames[0]+'ShowView, '+oneAndManyClassNames[0]+'EditView'
if (oneAndManyClassNames.size()>1)
  componentstr = ', '+oneAndManyClassNames.join('ShowView, ')+'ShowView, '+oneAndManyClassNames.join('EditView, ')+'EditView'
%>
  components: { Pagination, ShowView, EditView${componentstr} },
  data() {
    return {
      ...baseIndex.data,
      config: {
        id: undefined,
        domainName: '${propertyName}',
        // 属性列表
        props: {
<%
for (int i=0; i<props2.size(); i++) {
  if (i<6) {
%>
          ${props2[i].name}: {
            title: i18n('${props2[i].name}', '${propertyName}'),
            defVisible: true,
            visible: true
          }${i<props2.size()-1?',':''}
<%
  } else {
%>
          ${props2[i].name}: {
            title: i18n('${props2[i].name}', '${propertyName}'),
            defVisible: false
          }${i<props2.size()-1?',':''}
<%
  }
}
%>
        },
        // 导出Excel配置
        download: {
          excelName: this.\$t('domain.${propertyName}.pageTitle'),
          excelHeader: undefined, // 导出Excel中列的名字，默认为config.props.*.title
          excelVal: undefined // 导出Excel中值的字段名，默认为config.props.*
        },
        sVisible: false,
        sCallBack: (refresh, openEdit) => {
          if (refresh) {
            this.handleFilter()
          }
          if (openEdit) {
            this.handleUpdate(this.config.id)
          }
        },
        eStatus: 'create',
        eVisible: false,
        eCallBack: (refresh) => {
          if (refresh) {
            this.handleFilter()
          }
        }
      },
      enumClasses: {
<%
enumClass.eachWithIndex { entry, i ->
%>
        '${entry.value.name}': this.getEnum('${entry.value.name}')${i<enumClass.size()-1?',':''}
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
      }
    }
  },
  computed: {
    ...baseIndex.computed
  },
  created() {
    // this.tableQuery.deep = true // 如果需要显示子类中的属性，可以放开，但是会出现N+1数据库查询问题，影响性能
    this.getData()
  },
  methods: {
    ...baseIndex.methods
  }
}
</script>

<style lang="scss">
@import "~@/styles/base.scss";
</style>

<style lang="scss" scoped>
@import "~@/styles/baseIndex.scss";
</style>
