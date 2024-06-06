
<template>
  <el-form ref="vForm" label-position="right" label-width="auto" @submit.native.prevent>
    <div class="card-container search-card">
      <el-card shadow="never">
        <div slot="header" class="card-header">
          <span>{{ $t('domain.userInfo.pageTitle') }}</span>
        </div>
        <el-row :gutter="10" align="middle">

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'account'|i18n('userInfo')">
              <el-input v-model="tableQuery.search.account" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'name'|i18n('userInfo')">
              <el-input v-model="tableQuery.search.name" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'status'|i18n('userInfo')">
              <el-input v-model="tableQuery.search.status" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'avatar'|i18n('userInfo')">
              <el-input v-model="tableQuery.search.avatar" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'email'|i18n('userInfo')">
              <el-input v-model="tableQuery.search.email" type="text" clearable />
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

          <el-table-column v-if="config.props.account.visible" :label="'account'|i18n('userInfo')" prop="account" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.account }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.name.visible" :label="'name'|i18n('userInfo')" prop="name" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.name }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.status.visible" :label="'status'|i18n('userInfo')" prop="status" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.status | fmatEnum(enumClasses['com.easecurity.core.basis.b.UserInfoEnum$Status']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.avatar.visible" :label="'avatar'|i18n('userInfo')" prop="avatar" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.avatar }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.email.visible" :label="'email'|i18n('userInfo')" prop="email" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.email }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.phone.visible" :label="'phone'|i18n('userInfo')" prop="phone" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.phone }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.dateCreated.visible" :label="'dateCreated'|i18n('userInfo')" prop="dateCreated" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.dateCreated }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.lastUpdated.visible" :label="'lastUpdated'|i18n('userInfo')" prop="lastUpdated" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.lastUpdated }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.user.visible" :label="'user'|i18n('userInfo')" prop="user" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type span-inline" @click="handleShowChildren(row.user.id, 'user', 'User')">{{ row.user }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.id.visible" :label="'id'|i18n('userInfo')" prop="id" sortable="custom" min-width="100px" align="center">
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

    <UserShowView :config="childrenConfig.user" />
    <UserEditView :config="childrenConfig.user" />

  </el-form>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { i18nLabel as i18n } from '@/utils/i18n'
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseIndex from '@/utils/baseIndex' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可
import ShowView from './show'
import EditView from './edit'

import UserShowView from '@/views/md/user/show'
import UserEditView from '@/views/md/user/edit'

export default {
  name: 'UserInfoList', // name值需要与router中的name相同，否则页签的“keep-alive”功能不生效

  components: { Pagination, ShowView, EditView, UserShowView, UserEditView },
  data() {
    return {
      ...baseIndex.data,
      config: {
        id: undefined,
        domainName: 'userInfo',
        // 属性列表
        props: {

          account: {
            title: i18n('account', 'userInfo'),
            defVisible: true,
            visible: true
          },

          name: {
            title: i18n('name', 'userInfo'),
            defVisible: true,
            visible: true
          },

          status: {
            title: i18n('status', 'userInfo'),
            defVisible: true,
            visible: true
          },

          avatar: {
            title: i18n('avatar', 'userInfo'),
            defVisible: true,
            visible: true
          },

          email: {
            title: i18n('email', 'userInfo'),
            defVisible: true,
            visible: true
          },

          phone: {
            title: i18n('phone', 'userInfo'),
            defVisible: true,
            visible: true
          },

          dateCreated: {
            title: i18n('dateCreated', 'userInfo'),
            defVisible: false
          },

          lastUpdated: {
            title: i18n('lastUpdated', 'userInfo'),
            defVisible: false
          },

          user: {
            title: i18n('user', 'userInfo'),
            defVisible: false
          },

          id: {
            title: i18n('id', 'userInfo'),
            defVisible: false
          }

        },
        // 导出Excel配置
        download: {
          excelName: this.$t('domain.userInfo.pageTitle'),
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

        'com.easecurity.core.basis.b.UserInfoEnum$Status': this.getEnum('com.easecurity.core.basis.b.UserInfoEnum$Status')

      },
      childrenConfig: {

        user: {
          id: undefined,
          domainName: 'user',
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.user.id, null, this.childrenConfig.user.domainName)
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
