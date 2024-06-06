
<template>
  <el-form ref="vForm" label-position="right" label-width="auto" @submit.native.prevent>
    <div class="card-container search-card">
      <el-card shadow="never">
        <div slot="header" class="card-header">
          <span>{{ $t('domain.uriPosts.pageTitle') }}</span>
        </div>
        <el-row :gutter="10" align="middle">

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'postsId'|i18n('uriPosts')">
              <el-input v-model="tableQuery.search.postsId" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'annotation'|i18n('uriPosts')">
              <el-input v-model="tableQuery.search.annotation" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'group1'|i18n('uriPosts')">
              <el-input v-model="tableQuery.search.group1" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'fromTo'|i18n('uriPosts')">
              <el-input v-model="tableQuery.search.fromTo" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'status'|i18n('uriPosts')">
              <el-input v-model="tableQuery.search.status" type="text" clearable />
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

          <el-table-column v-if="config.props.postsId.visible" :label="'postsId'|i18n('uriPosts')" prop="postsId" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.postsId }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.annotation.visible" :label="'annotation'|i18n('uriPosts')" prop="annotation" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.annotation }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.group1.visible" :label="'group1'|i18n('uriPosts')" prop="group1" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.group1 }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.fromTo.visible" :label="'fromTo'|i18n('uriPosts')" prop="fromTo" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.fromTo | fmatEnum(enumClasses['com.easecurity.core.basis.au.UriPostsEnum$FromTo']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.status.visible" :label="'status'|i18n('uriPosts')" prop="status" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.status | fmatEnum(enumClasses['com.easecurity.core.basis.au.UriPostsEnum$Status']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.uri.visible" :label="'uri'|i18n('uriPosts')" prop="uri" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type span-inline" @click="handleShowChildren(row.uri.id, 'uri', 'Uri')">{{ row.uri }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.id.visible" :label="'id'|i18n('uriPosts')" prop="id" sortable="custom" min-width="100px" align="center">
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

    <UriShowView :config="childrenConfig.uri" />
    <UriEditView :config="childrenConfig.uri" />

  </el-form>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { i18nLabel as i18n } from '@/utils/i18n'
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseIndex from '@/utils/baseIndex' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可
import ShowView from './show'
import EditView from './edit'

import UriShowView from '@/views/md/uri/show'
import UriEditView from '@/views/md/uri/edit'

export default {
  name: 'UriPostsList', // name值需要与router中的name相同，否则页签的“keep-alive”功能不生效

  components: { Pagination, ShowView, EditView, UriShowView, UriEditView },
  data() {
    return {
      ...baseIndex.data,
      config: {
        id: undefined,
        domainName: 'uriPosts',
        // 属性列表
        props: {

          postsId: {
            title: i18n('postsId', 'uriPosts'),
            defVisible: true,
            visible: true
          },

          annotation: {
            title: i18n('annotation', 'uriPosts'),
            defVisible: true,
            visible: true
          },

          group1: {
            title: i18n('group1', 'uriPosts'),
            defVisible: true,
            visible: true
          },

          fromTo: {
            title: i18n('fromTo', 'uriPosts'),
            defVisible: true,
            visible: true
          },

          status: {
            title: i18n('status', 'uriPosts'),
            defVisible: true,
            visible: true
          },

          uri: {
            title: i18n('uri', 'uriPosts'),
            defVisible: true,
            visible: true
          },

          id: {
            title: i18n('id', 'uriPosts'),
            defVisible: false
          }

        },
        // 导出Excel配置
        download: {
          excelName: this.$t('domain.uriPosts.pageTitle'),
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

        'com.easecurity.core.basis.au.UriPostsEnum$FromTo': this.getEnum('com.easecurity.core.basis.au.UriPostsEnum$FromTo'),

        'com.easecurity.core.basis.au.UriPostsEnum$Status': this.getEnum('com.easecurity.core.basis.au.UriPostsEnum$Status')

      },
      childrenConfig: {

        uri: {
          id: undefined,
          domainName: 'uri',
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.uri.id, null, this.childrenConfig.uri.domainName)
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
