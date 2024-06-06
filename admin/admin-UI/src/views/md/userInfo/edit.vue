
<template>
  <el-dialog :title="$t('domain.def.' + config.eStatus)+'——'+$t('domain.userInfo.pageTitle')" :visible.sync="config.eVisible" :append-to-body="config.appendToBody" width="80%" class="edit-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container edit-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="edit-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'account'|i18n('userInfo')" prop="account">
                <el-input id="account" v-model="temp.account" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'name'|i18n('userInfo')" prop="name">
                <el-input id="name" v-model="temp.name" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'status'|i18n('userInfo')" prop="status">
                <el-select id="status" v-model="temp.status" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.b.UserInfoEnum$Status']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'avatar'|i18n('userInfo')" prop="avatar">
                <el-input id="avatar" v-model="temp.avatar" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'email'|i18n('userInfo')" prop="email">
                <el-input id="email" v-model="temp.email" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'phone'|i18n('userInfo')" prop="phone">
                <el-input id="phone" v-model="temp.phone" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'dateCreated'|i18n('userInfo')" prop="dateCreated">
                <el-date-picker id="dateCreated" v-model="temp.dateCreated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastUpdated'|i18n('userInfo')" prop="lastUpdated">
                <el-date-picker id="lastUpdated" v-model="temp.lastUpdated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'user'|i18n('userInfo')" prop="user">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.user.id, 'user', 'User')">{{ temp.user }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.user.id, 'user', 'User')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('user', 'User', 'userInfo', temp.id)">{{ $t('domain.def.add') }}</el-link>
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

    <UserShowView :config="childrenConfig.user" />
    <UserSelectView :config="childrenConfig.user" />
    <UserEditView :config="childrenConfig.user" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'UserInfoEdit',

  components: {

    UserShowView: () => import ('@/views/md/user/show'),
    UserSelectView: () => import ('@/views/md/user/select'),
    UserEditView: () => import ('@/views/md/user/edit')

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
      temp: { user: null },
      // 表单初始化数据，新建页面打开默认值
      tempDef: { status: 'ENABLED' },
      enumClasses: {

        'com.easecurity.core.basis.b.UserInfoEnum$Status': this.getEnum('com.easecurity.core.basis.b.UserInfoEnum$Status')

      },
      params: {},
      // 页面校验规则
      rules: {

        account: [{ required: true, message: 'account is required', trigger: 'blur' }],

        name: [],

        status: [],

        avatar: [],

        email: [],

        phone: [],

        dateCreated: [],

        lastUpdated: [],

        user: [{ required: true, message: 'user is required', trigger: 'change' }]

      },
      childrenConfig: {

        user: {
          id: undefined,
          domainName: 'user',
          props: ['id'], // select.vue中显示的列，列如['id','name']
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
