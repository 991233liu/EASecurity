
<template>
  <el-dialog :title="$t('domain.def.show')+'——'+$t('domain.role.pageTitle')" :visible.sync="config.sVisible" :append-to-body="config.appendToBody" width="80%" class="show-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container show-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="show-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'name'|i18n('role')">
                <span>{{ temp.name }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'code'|i18n('role')">
                <span>{{ temp.code }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'fullName'|i18n('role')">
                <span>{{ temp.fullName }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'dateCreated'|i18n('role')">
                <span>{{ temp.dateCreated }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastUpdated'|i18n('role')">
                <span>{{ temp.lastUpdated }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'org'|i18n('role')">
                <span class="link-type span-inline" @click="handleShowChildren(temp.org.id, 'org', 'Org')">{{ temp.org }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'roleGroup'|i18n('role')">
                <span class="link-type span-inline" @click="handleShowChildren(temp.roleGroup.id, 'roleGroup', 'RoleGroup')">{{ temp.roleGroup }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'roleUsers'|i18n('role')">
                <span v-for="(item, index) in temp.roleUsers" :key="index" class="link-type span-inline" @click="handleShowChildren(item.id, 'roleUsers', 'RoleUser')">{{ item }}</span>
              </el-form-item>
            </el-col>

          </el-row>
        </el-card>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="config.sVisible = false">
        {{ $t('domain.def.cancel') }}
      </el-button>
      <el-button type="primary" :disabled="loading || deleteLoading" @click="editData">
        {{ $t('domain.def.edit') }}
      </el-button>
      <el-button type="danger" :disabled="loading" :loading="deleteLoading" @click="deleteData">
        {{ $t('domain.def.delete') }}
      </el-button>
    </div>

    <RoleUserShowView :config="childrenConfig.roleUser" />
    <RoleUserEditView :config="childrenConfig.roleUser" />

    <OrgShowView :config="childrenConfig.org" />
    <OrgEditView :config="childrenConfig.org" />

    <RoleGroupShowView :config="childrenConfig.roleGroup" />
    <RoleGroupEditView :config="childrenConfig.roleGroup" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseShow from '@/utils/baseShow' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'RoleShow',

  components: {

    RoleUserShowView: () => import ('@/views/md/roleUser/show'),
    RoleUserEditView: () => import ('@/views/md/roleUser/edit'),

    OrgShowView: () => import ('@/views/md/org/show'),
    OrgEditView: () => import ('@/views/md/org/edit'),

    RoleGroupShowView: () => import ('@/views/md/roleGroup/show'),
    RoleGroupEditView: () => import ('@/views/md/roleGroup/edit')

  },

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

      },
      params: {},
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

        org: {
          id: undefined,
          domainName: 'org',
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.org.id, null, this.childrenConfig.org.domainName)
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

        roleGroup: {
          id: undefined,
          domainName: 'roleGroup',
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.roleGroup.id, null, this.childrenConfig.roleGroup.domainName)
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
