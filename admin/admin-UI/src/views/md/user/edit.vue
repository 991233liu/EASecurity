
<template>
  <el-dialog :title="$t('domain.def.' + config.eStatus)+'——'+$t('domain.user.pageTitle')" :visible.sync="config.eVisible" :append-to-body="config.appendToBody" width="80%" class="edit-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container edit-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="edit-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'account'|i18n('user')" prop="account">
                <el-input id="account" v-model="temp.account" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'pd'|i18n('user')" prop="pd">
                <el-input id="pd" v-model="temp.pd" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'acStatus'|i18n('user')" prop="acStatus">
                <el-select id="acStatus" v-model="temp.acStatus" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.b.UserEnum$AcStatus']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'pdStatus'|i18n('user')" prop="pdStatus">
                <el-select id="pdStatus" v-model="temp.pdStatus" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.b.UserEnum$PdStatus']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'identities'|i18n('user')" prop="identities">
                <el-input id="identities" v-model="temp.identities" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'userinfo'|i18n('user')" prop="userinfo">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.userinfo.id, 'userinfo', 'UserInfo')">{{ temp.userinfo }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.userinfo.id, 'userinfo', 'UserInfo')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('userinfo', 'UserInfo', 'user', temp.id)">{{ $t('domain.def.add') }}</el-link>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastLoginTtime'|i18n('user')" prop="lastLoginTtime">
                <el-date-picker id="lastLoginTtime" v-model="temp.lastLoginTtime" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'pdErrorTimes'|i18n('user')" prop="pdErrorTimes">
                <el-input id="pdErrorTimes" v-model="temp.pdErrorTimes" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'dateCreated'|i18n('user')" prop="dateCreated">
                <el-date-picker id="dateCreated" v-model="temp.dateCreated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastUpdated'|i18n('user')" prop="lastUpdated">
                <el-date-picker id="lastUpdated" v-model="temp.lastUpdated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'roleUsers'|i18n('user')" prop="roleUsers">
                <div v-for="(item, index) in temp.roleUsers" :key="index" class="span-inline"><span class="link-type" @click="handleShowChildren(item.id, 'roleUsers', 'RoleUser')">{{ item }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(item.id, 'roleUsers', 'RoleUser', true)">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('roleUsers', 'RoleUser', 'user', temp.id, true)">{{ $t('domain.def.add') }}</el-link>
              </el-form-item>
            </el-col>

          </el-row>
        </el-card>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="config.eVisible = false">
        {{ $t('domain.def.cancel') }}
      </el-button>
      <el-button type="primary" :disabled="loading || deleteLoading" :loading="saveLoading" @click="config.eStatus == 'create' ? createData() : updateData()">
        {{ $t('domain.def.save') }}
      </el-button>
      <el-button type="danger" :disabled="loading || saveLoading" :loading="deleteLoading" @click="deleteData">
        {{ $t('domain.def.delete') }}
      </el-button>
    </div>

    <RoleUserShowView :config="childrenConfig.roleUser" />
    <RoleUserSelectView :config="childrenConfig.roleUser" />
    <RoleUserEditView :config="childrenConfig.roleUser" />

    <UserInfoShowView :config="childrenConfig.userInfo" />
    <UserInfoSelectView :config="childrenConfig.userInfo" />
    <UserInfoEditView :config="childrenConfig.userInfo" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'UserEdit',

  components: {

    RoleUserShowView: () => import ('@/views/md/roleUser/show'),
    RoleUserSelectView: () => import ('@/views/md/roleUser/select'),
    RoleUserEditView: () => import ('@/views/md/roleUser/edit'),

    UserInfoShowView: () => import ('@/views/md/userInfo/show'),
    UserInfoSelectView: () => import ('@/views/md/userInfo/select'),
    UserInfoEditView: () => import ('@/views/md/userInfo/edit')

  },

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
      temp: { userinfo: null, roleUsers: null },
      // 表单初始化数据，新建页面打开默认值
      tempDef: { pdErrorTimes: 0 },
      enumClasses: {

        'com.easecurity.core.basis.b.UserEnum$AcStatus': this.getEnum('com.easecurity.core.basis.b.UserEnum$AcStatus'),

        'com.easecurity.core.basis.b.UserEnum$PdStatus': this.getEnum('com.easecurity.core.basis.b.UserEnum$PdStatus')

      },
      params: {},
      // 页面校验规则
      rules: {

        account: [{ required: true, message: 'account is required', trigger: 'blur' }],

        pd: [],

        acStatus: [],

        pdStatus: [],

        identities: [],

        userinfo: [],

        lastLoginTtime: [],

        pdErrorTimes: [{ type: 'integer', message: 'pdErrorTimes must be integer', trigger: 'change' }],

        dateCreated: [],

        lastUpdated: [],

        roleUsers: [{ type: 'array', message: 'roleUsers must be array', trigger: 'change' }]

      },
      childrenConfig: {

        roleUser: {
          id: undefined,
          domainName: 'roleUser',
          props: ['id'], // select.vue中显示的列，列如['id','name']
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
        },

        userInfo: {
          id: undefined,
          domainName: 'userInfo',
          props: ['id'], // select.vue中显示的列，列如['id','name']
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
        }

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
