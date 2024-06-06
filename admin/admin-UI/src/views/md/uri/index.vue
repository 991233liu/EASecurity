
<template>
  <el-form ref="vForm" label-position="right" label-width="auto" @submit.native.prevent>
    <div class="card-container search-card">
      <el-card shadow="never">
        <div slot="header" class="card-header">
          <span>{{ $t('domain.uri.pageTitle') }}</span>
        </div>
        <el-row :gutter="10" align="middle">

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'uri'|i18n('uri')">
              <el-input v-model="tableQuery.search.uri" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'classFullName'|i18n('uri')">
              <el-input v-model="tableQuery.search.classFullName" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'methodName'|i18n('uri')">
              <el-input v-model="tableQuery.search.methodName" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'methodSignature'|i18n('uri')">
              <el-input v-model="tableQuery.search.methodSignature" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'easType'|i18n('uri')">
              <el-input v-model="tableQuery.search.easType" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item label-width="0" class="form-button-item">
              <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
                {{ $t('table.search') }}
              </el-button>
              <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">
                {{ $t('table.add') }}
              </el-button>
              <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" :disabled="listLoading || buttonDisabled" @click="handleDownload">
                {{ $t('table.export') }}
              </el-button>
              <el-button class="filter-item" type="danger" icon="el-icon-delete" :disabled="listLoading || buttonDisabled" @click="handleDeleteAll">
                {{ $t('table.delete') }}
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
          <el-table-column type="index" :label="$t('table.id')" :index="tableIndex" width="55" align="center" />

          <el-table-column v-if="config.props.uri.visible" :label="'uri'|i18n('uri')" prop="uri" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.uri }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.classFullName.visible" :label="'classFullName'|i18n('uri')" prop="classFullName" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.classFullName }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.methodName.visible" :label="'methodName'|i18n('uri')" prop="methodName" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.methodName }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.methodSignature.visible" :label="'methodSignature'|i18n('uri')" prop="methodSignature" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.methodSignature }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.easType.visible" :label="'easType'|i18n('uri')" prop="easType" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.easType | fmatEnum(enumClasses['com.easecurity.core.access.annotation.EasType']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.fromTo.visible" :label="'fromTo'|i18n('uri')" prop="fromTo" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.fromTo | fmatEnum(enumClasses['com.easecurity.core.basis.re.UriEnum$FromTo']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.status.visible" :label="'status'|i18n('uri')" prop="status" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.status | fmatEnum(enumClasses['com.easecurity.core.basis.re.UriEnum$Status']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.uriIps.visible" :label="'uriIps'|i18n('uri')" prop="uriIps" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span v-for="(item, index) in row.uriIps" :key="index" class="link-type span-inline" @click="handleShowChildren(item.id, 'uriIps', 'UriIp')">{{ item }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.id.visible" :label="'id'|i18n('uri')" prop="id" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.id }}</span>
            </template>
          </el-table-column>

          <el-table-column align="center" min-width="150" class-name="small-padding fixed-width">
            <template #header>
              <span>{{ $t('table.actions') }} </span>
              <el-popover ref="actPopover" placement="bottom" :title="$t('table.actPopover')" width="350" trigger="click">
                <el-card shadow="never">
                  <el-col v-for="(v, k) in config.props" :key="k" :md="12" :sm="12" :xs="24" class="grid-cell">
                    <el-checkbox v-model="config.props[k].visible">
                      {{ v.title }}
                    </el-checkbox>
                  </el-col>
                </el-card>
                <div class="static-content-item">
                  <el-button type="primary" size="mini" @click="headerReset">{{ $t('table.reset') }}</el-button>
                </div>
              </el-popover>
              <el-link v-popover:actPopover :underline="false"><svg-icon icon-class="list2" /></el-link>
            </template>
            <template slot-scope="{row}">
              <el-button type="primary" size="mini" @click="handleUpdate(row.id)">
                {{ $t('table.edit') }}
              </el-button>
              <el-button size="mini" type="danger" @click="handleDelete(row.id)">
                {{ $t('table.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="tableQuery.max" style="padding: 0px 16px" @pagination="getData" />
      </el-card>
    </div>
    <ShowView :config="config" />
    <EditView :config="config" />

    <UriIpShowView :config="childrenConfig.uriIp" />
    <UriIpEditView :config="childrenConfig.uriIp" />

  </el-form>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { i18nLabel as i18n } from '@/utils/i18n'
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseIndex from '@/utils/baseIndex' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可
import ShowView from './show'
import EditView from './edit'

import UriIpShowView from '@/views/md/uriIp/show'
import UriIpEditView from '@/views/md/uriIp/edit'

export default {
  name: 'UriList', // name值需要与router中的name相同，否则页签的“keep-alive”功能不生效

  components: { Pagination, ShowView, EditView, UriIpShowView, UriIpEditView },
  data() {
    return {
      ...baseIndex.data,
      config: {
        id: undefined,
        domainName: 'uri',
        // 属性列表
        props: {

          uri: {
            title: i18n('uri', 'uri'),
            defVisible: true,
            visible: true
          },

          classFullName: {
            title: i18n('classFullName', 'uri'),
            defVisible: true,
            visible: true
          },

          methodName: {
            title: i18n('methodName', 'uri'),
            defVisible: true,
            visible: true
          },

          methodSignature: {
            title: i18n('methodSignature', 'uri'),
            defVisible: true,
            visible: true
          },

          easType: {
            title: i18n('easType', 'uri'),
            defVisible: true,
            visible: true
          },

          fromTo: {
            title: i18n('fromTo', 'uri'),
            defVisible: true,
            visible: true
          },

          status: {
            title: i18n('status', 'uri'),
            defVisible: false
          },

          uriIps: {
            title: i18n('uriIps', 'uri'),
            defVisible: false
          },

          id: {
            title: i18n('id', 'uri'),
            defVisible: false
          }

        },
        // 导出Excel配置
        download: {
          excelName: this.$t('domain.uri.pageTitle'),
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

        'com.easecurity.core.access.annotation.EasType': this.getEnum('com.easecurity.core.access.annotation.EasType'),

        'com.easecurity.core.basis.re.UriEnum$FromTo': this.getEnum('com.easecurity.core.basis.re.UriEnum$FromTo'),

        'com.easecurity.core.basis.re.UriEnum$Status': this.getEnum('com.easecurity.core.basis.re.UriEnum$Status')

      },
      childrenConfig: {

        uriIp: {
          id: undefined,
          domainName: 'uriIp',
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.uriIp.id, null, this.childrenConfig.uriIp.domainName)
            }
          },
          eStatus: 'update',
          eVisible: false,
          eCallBack: (refresh) => {
            if (refresh) {
              this.getData()
            }
          }
        }

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
