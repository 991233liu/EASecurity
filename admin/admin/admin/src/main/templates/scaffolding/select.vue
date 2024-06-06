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
  <el-dialog :title="\$t('domain.def.select')+'——'+\$t('domain.${propertyName}.pageTitle')" :visible.sync="config.seVisible" :append-to-body="config.appendToBody" width="80%" class="show-dialog" @open="openDialog">
    <el-form ref="dataForm" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container select-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle">
<%
for (int i=0; i<props2.size() && i<3; i++) {
%>
            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'${props2[i].name}'|i18n('${propertyName}')">
                ${vue.searchFieldHTML(domainClass, hasMany, hasOne, props2[i])}
              </el-form-item>
            </el-col>
<%
}
%>
            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item label-width="0" class="form-button-item">
                <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
                  {{ \$t('table.search') }}
                </el-button>
              </el-form-item>
            </el-col>
          </el-row>

          <el-table
            ref="tableRef"
            v-loading="listLoading"
            :data="list"
            stripe
            border
            highlight-selection-row
            style="width: 100%;"
            @selection-change="handleSelectionChange(\$event, config.parentHasMany)"
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
            <el-table-column v-if="columnVisible('${props2[i].name}')" :label="'${props2[i].name}'|i18n('${propertyName}')" prop="${props2[i].name}" sortable="custom" min-width="${width}px" align="center">
              <template slot-scope="{row}">
                ${vue.showFieldHTML(domainClass, hasMany, hasOne, props2[i], "row.", "row.id", showClick)}
              </template>
            </el-table-column>
<%
}
%>
          </el-table>

          <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="tableQuery.max" style="padding: 0px 16px" @pagination="getData" />
        </el-card>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="config.seVisible = false">
        {{ \$t('domain.def.cancel') }}
      </el-button>
      <el-button type="primary" :disabled="saveLoading" :loading="listLoading" @click="saveData()">
        {{ \$t('domain.def.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseSelect from '@/utils/baseSelect' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: '${className}Select',
  components: { Pagination },
  props: {
    config: {
      type: Object,
      default: () => {
        return {
          parentId: undefined,
          parentClassName: undefined,
          parentHasMany: undefined,
          parentProp: undefined,
          domainName: undefined,
          props: undefined,
          appendToBody: false,
          seVisible: undefined,
          seCallBack: undefined
        }
      }
    }
  },
  data() {
    return {
      ...baseSelect.data,
      saveLoading: false
    }
  },
  computed: {
    ...baseSelect.computed
  },
  created() {
    // this.tableQuery.deep = true // 如果需要显示子类中的属性，可以放开，但是会出现N+1数据库查询问题，影响性能
    this.tableQuery.expand = ''
  },
  methods: {
    ...baseSelect.methods
  }
}
</script>

<style lang="scss">
@import "~@/styles/base.scss";
</style>

<style lang="scss" scoped>
@import "~@/styles/baseSelect.scss";
</style>
