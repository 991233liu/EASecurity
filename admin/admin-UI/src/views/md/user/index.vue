
<template>
  <el-form ref="vForm" label-position="right" label-width="auto" @submit.native.prevent>
    <div class="card-container search-card">
      <el-card shadow="never">
        <div slot="header" class="card-header">
          <span>{{ $t('domain.user.pageTitle') }}</span>
        </div>
        <el-row :gutter="10" align="middle">

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'account'|i18n('user')">
              <el-input v-model="tableQuery.search.account" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'pd'|i18n('user')">
              <el-input v-model="tableQuery.search.pd" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'acStatus'|i18n('user')">
              <el-input v-model="tableQuery.search.acStatus" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'pdStatus'|i18n('user')">
              <el-input v-model="tableQuery.search.pdStatus" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'identities'|i18n('user')">
              <el-input v-model="tableQuery.search.identities" type="text" clearable />
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

          <el-table-column v-if="config.props.account.visible" :label="'account'|i18n('user')" prop="account" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.account }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.pd.visible" :label="'pd'|i18n('user')" prop="pd" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.pd }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.acStatus.visible" :label="'acStatus'|i18n('user')" prop="acStatus" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.acStatus | fmatEnum(enumClasses['com.easecurity.core.basis.b.UserEnum$AcStatus']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.pdStatus.visible" :label="'pdStatus'|i18n('user')" prop="pdStatus" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.pdStatus | fmatEnum(enumClasses['com.easecurity.core.basis.b.UserEnum$PdStatus']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.identities.visible" :label="'identities'|i18n('user')" prop="identities" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.identities }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.userinfo.visible" :label="'userinfo'|i18n('user')" prop="userinfo" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type span-inline" @click="handleShowChildren(row.userinfo.id, 'userinfo', 'UserInfo')">{{ row.userinfo }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.lastLoginTtime.visible" :label="'lastLoginTtime'|i18n('user')" prop="lastLoginTtime" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.lastLoginTtime }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.pdErrorTimes.visible" :label="'pdErrorTimes'|i18n('user')" prop="pdErrorTimes" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.pdErrorTimes }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.dateCreated.visible" :label="'dateCreated'|i18n('user')" prop="dateCreated" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.dateCreated }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.lastUpdated.visible" :label="'lastUpdated'|i18n('user')" prop="lastUpdated" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.lastUpdated }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.roleUsers.visible" :label="'roleUsers'|i18n('user')" prop="roleUsers" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span v-for="(item, index) in row.roleUsers" :key="index" class="link-type span-inline" @click="handleShowChildren(item.id, 'roleUsers', 'RoleUser')">{{ item }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.id.visible" :label="'id'|i18n('user')" prop="id" sortable="custom" min-width="100px" align="center">
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

    <RoleUserShowView :config="childrenConfig.roleUser" />
    <RoleUserEditView :config="childrenConfig.roleUser" />

    <UserInfoShowView :config="childrenConfig.userInfo" />
    <UserInfoEditView :config="childrenConfig.userInfo" />

  </el-form>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { i18nLabel as i18n } from '@/utils/i18n'
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseIndex from '@/utils/baseIndex' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可
import ShowView from './show'
import EditView from './edit'

import RoleUserShowView from '@/views/md/roleUser/show'
import RoleUserEditView from '@/views/md/roleUser/edit'

import UserInfoShowView from '@/views/md/userInfo/show'
import UserInfoEditView from '@/views/md/userInfo/edit'

export default {
  name: 'UserList', // name值需要与router中的name相同，否则页签的“keep-alive”功能不生效

  components: { Pagination, ShowView, EditView, RoleUserShowView, UserInfoShowView, RoleUserEditView, UserInfoEditView },
  data() {
    return {
      ...baseIndex.data,
      config: {
        id: undefined,
        domainName: 'user',
        // 属性列表
        props: {

          account: {
            title: i18n('account', 'user'),
            defVisible: true,
            visible: true
          },

          pd: {
            title: i18n('pd', 'user'),
            defVisible: true,
            visible: true
          },

          acStatus: {
            title: i18n('acStatus', 'user'),
            defVisible: true,
            visible: true
          },

          pdStatus: {
            title: i18n('pdStatus', 'user'),
            defVisible: true,
            visible: true
          },

          identities: {
            title: i18n('identities', 'user'),
            defVisible: true,
            visible: true
          },

          userinfo: {
            title: i18n('userinfo', 'user'),
            defVisible: true,
            visible: true
          },

          lastLoginTtime: {
            title: i18n('lastLoginTtime', 'user'),
            defVisible: false
          },

          pdErrorTimes: {
            title: i18n('pdErrorTimes', 'user'),
            defVisible: false
          },

          dateCreated: {
            title: i18n('dateCreated', 'user'),
            defVisible: false
          },

          lastUpdated: {
            title: i18n('lastUpdated', 'user'),
            defVisible: false
          },

          roleUsers: {
            title: i18n('roleUsers', 'user'),
            defVisible: false
          },

          id: {
            title: i18n('id', 'user'),
            defVisible: false
          }

        },
        // 导出Excel配置
        download: {
          excelName: this.$t('domain.user.pageTitle'),
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

        'com.easecurity.core.basis.b.UserEnum$AcStatus': this.getEnum('com.easecurity.core.basis.b.UserEnum$AcStatus'),

        'com.easecurity.core.basis.b.UserEnum$PdStatus': this.getEnum('com.easecurity.core.basis.b.UserEnum$PdStatus')

      },
      childrenConfig: {

        roleUser: {
          id: undefined,
          domainName: 'roleUser',
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.roleUser.id, null, this.childrenConfig.roleUser.domainName)
            }
          },
          eStatus: 'update',
          eVisible: false,
          eCallBack: (refresh) => {
            if (refresh) {
              this.getData()
            }
          }
        },

        userInfo: {
          id: undefined,
          domainName: 'userInfo',
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.userInfo.id, null, this.childrenConfig.userInfo.domainName)
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
