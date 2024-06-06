
<template>
  <el-dialog :title="$t('domain.def.show')+'——'+$t('domain.user.pageTitle')" :visible.sync="config.sVisible" :append-to-body="config.appendToBody" width="80%" class="show-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container show-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="show-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'account'|i18n('user')">
                <span>{{ temp.account }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'pd'|i18n('user')">
                <span>{{ temp.pd }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'acStatus'|i18n('user')">
                <span>{{ temp.acStatus | fmatEnum(enumClasses['com.easecurity.core.basis.b.UserEnum$AcStatus']) }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'pdStatus'|i18n('user')">
                <span>{{ temp.pdStatus | fmatEnum(enumClasses['com.easecurity.core.basis.b.UserEnum$PdStatus']) }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'identities'|i18n('user')">
                <span>{{ temp.identities }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'userinfo'|i18n('user')">
                <span class="link-type span-inline" @click="handleShowChildren(temp.userinfo.id, 'userinfo', 'UserInfo')">{{ temp.userinfo }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastLoginTtime'|i18n('user')">
                <span>{{ temp.lastLoginTtime }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'pdErrorTimes'|i18n('user')">
                <span>{{ temp.pdErrorTimes }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'dateCreated'|i18n('user')">
                <span>{{ temp.dateCreated }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastUpdated'|i18n('user')">
                <span>{{ temp.lastUpdated }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'roleUsers'|i18n('user')">
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

    <UserInfoShowView :config="childrenConfig.userInfo" />
    <UserInfoEditView :config="childrenConfig.userInfo" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseShow from '@/utils/baseShow' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'UserShow',

  components: {

    RoleUserShowView: () => import ('@/views/md/roleUser/show'),
    RoleUserEditView: () => import ('@/views/md/roleUser/edit'),

    UserInfoShowView: () => import ('@/views/md/userInfo/show'),
    UserInfoEditView: () => import ('@/views/md/userInfo/edit')

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

        'com.easecurity.core.basis.b.UserEnum$AcStatus': this.getEnum('com.easecurity.core.basis.b.UserEnum$AcStatus'),

        'com.easecurity.core.basis.b.UserEnum$PdStatus': this.getEnum('com.easecurity.core.basis.b.UserEnum$PdStatus')

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
