
<template>
  <el-dialog :title="$t('domain.def.' + config.eStatus)+'——'+$t('domain.role.pageTitle')" :visible.sync="config.eVisible" :append-to-body="config.appendToBody" width="80%" class="edit-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container edit-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="edit-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'name'|i18n('role')" prop="name">
                <el-input id="name" v-model="temp.name" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'code'|i18n('role')" prop="code">
                <el-input id="code" v-model="temp.code" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'fullName'|i18n('role')" prop="fullName">
                <el-input id="fullName" v-model="temp.fullName" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'dateCreated'|i18n('role')" prop="dateCreated">
                <el-date-picker id="dateCreated" v-model="temp.dateCreated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastUpdated'|i18n('role')" prop="lastUpdated">
                <el-date-picker id="lastUpdated" v-model="temp.lastUpdated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'org'|i18n('role')" prop="org">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.org.id, 'org', 'Org')">{{ temp.org }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.org.id, 'org', 'Org')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('org', 'Org', 'role', temp.id)">{{ $t('domain.def.add') }}</el-link>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'roleGroup'|i18n('role')" prop="roleGroup">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.roleGroup.id, 'roleGroup', 'RoleGroup')">{{ temp.roleGroup }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.roleGroup.id, 'roleGroup', 'RoleGroup')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('roleGroup', 'RoleGroup', 'role', temp.id)">{{ $t('domain.def.add') }}</el-link>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'roleUsers'|i18n('role')" prop="roleUsers">
                <div v-for="(item, index) in temp.roleUsers" :key="index" class="span-inline"><span class="link-type" @click="handleShowChildren(item.id, 'roleUsers', 'RoleUser')">{{ item }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(item.id, 'roleUsers', 'RoleUser', true)">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('roleUsers', 'RoleUser', 'role', temp.id, true)">{{ $t('domain.def.add') }}</el-link>
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

    <OrgShowView :config="childrenConfig.org" />
    <OrgSelectView :config="childrenConfig.org" />
    <OrgEditView :config="childrenConfig.org" />

    <RoleGroupShowView :config="childrenConfig.roleGroup" />
    <RoleGroupSelectView :config="childrenConfig.roleGroup" />
    <RoleGroupEditView :config="childrenConfig.roleGroup" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'RoleEdit',

  components: {

    RoleUserShowView: () => import ('@/views/md/roleUser/show'),
    RoleUserSelectView: () => import ('@/views/md/roleUser/select'),
    RoleUserEditView: () => import ('@/views/md/roleUser/edit'),

    OrgShowView: () => import ('@/views/md/org/show'),
    OrgSelectView: () => import ('@/views/md/org/select'),
    OrgEditView: () => import ('@/views/md/org/edit'),

    RoleGroupShowView: () => import ('@/views/md/roleGroup/show'),
    RoleGroupSelectView: () => import ('@/views/md/roleGroup/select'),
    RoleGroupEditView: () => import ('@/views/md/roleGroup/edit')

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
      temp: { org: null, roleGroup: null, roleUsers: null },
      // 表单初始化数据，新建页面打开默认值
      tempDef: {},
      enumClasses: {

      },
      params: {},
      // 页面校验规则
      rules: {

        name: [],

        code: [],

        fullName: [],

        dateCreated: [],

        lastUpdated: [],

        org: [{ required: true, message: 'org is required', trigger: 'change' }],

        roleGroup: [{ required: true, message: 'roleGroup is required', trigger: 'change' }],

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

        org: {
          id: undefined,
          domainName: 'org',
          props: ['id'], // select.vue中显示的列，列如['id','name']
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

        roleGroup: {
          id: undefined,
          domainName: 'roleGroup',
          props: ['id'], // select.vue中显示的列，列如['id','name']
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
