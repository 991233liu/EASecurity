
<template>
  <el-dialog :title="$t('domain.def.' + config.eStatus)+'——'+$t('domain.orgUser.pageTitle')" :visible.sync="config.eVisible" :append-to-body="config.appendToBody" width="80%" class="edit-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container edit-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="edit-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'account'|i18n('orgUser')" prop="account">
                <el-input id="account" v-model="temp.account" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'masterPosts'|i18n('orgUser')" prop="masterPosts">
                <el-radio-group id="masterPosts" v-model="temp.masterPosts"><el-radio :label="true">{{ 'true'|fmatBoolean }}</el-radio><el-radio :label="false">{{ 'false'|fmatBoolean }}</el-radio></el-radio-group>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'dateCreated'|i18n('orgUser')" prop="dateCreated">
                <el-date-picker id="dateCreated" v-model="temp.dateCreated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastUpdated'|i18n('orgUser')" prop="lastUpdated">
                <el-date-picker id="lastUpdated" v-model="temp.lastUpdated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'org'|i18n('orgUser')" prop="org">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.org.id, 'org', 'Org')">{{ temp.org }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.org.id, 'org', 'Org')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('org', 'Org', 'orgUser', temp.id)">{{ $t('domain.def.add') }}</el-link>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'posts'|i18n('orgUser')" prop="posts">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.posts.id, 'posts', 'Posts')">{{ temp.posts }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.posts.id, 'posts', 'Posts')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('posts', 'Posts', 'orgUser', temp.id)">{{ $t('domain.def.add') }}</el-link>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'user'|i18n('orgUser')" prop="user">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.user.id, 'user', 'User')">{{ temp.user }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.user.id, 'user', 'User')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('user', 'User', 'orgUser', temp.id)">{{ $t('domain.def.add') }}</el-link>
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

    <OrgShowView :config="childrenConfig.org" />
    <OrgSelectView :config="childrenConfig.org" />
    <OrgEditView :config="childrenConfig.org" />

    <PostsShowView :config="childrenConfig.posts" />
    <PostsSelectView :config="childrenConfig.posts" />
    <PostsEditView :config="childrenConfig.posts" />

    <UserShowView :config="childrenConfig.user" />
    <UserSelectView :config="childrenConfig.user" />
    <UserEditView :config="childrenConfig.user" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'OrgUserEdit',

  components: {

    OrgShowView: () => import ('@/views/md/org/show'),
    OrgSelectView: () => import ('@/views/md/org/select'),
    OrgEditView: () => import ('@/views/md/org/edit'),

    PostsShowView: () => import ('@/views/md/posts/show'),
    PostsSelectView: () => import ('@/views/md/posts/select'),
    PostsEditView: () => import ('@/views/md/posts/edit'),

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
      temp: { org: null, posts: null, user: null },
      // 表单初始化数据，新建页面打开默认值
      tempDef: {},
      enumClasses: {

      },
      params: {},
      // 页面校验规则
      rules: {

        account: [],

        masterPosts: [{ type: 'boolean', message: 'masterPosts must be boolean', trigger: 'change' }],

        dateCreated: [],

        lastUpdated: [],

        org: [{ required: true, message: 'org is required', trigger: 'change' }],

        posts: [{ required: true, message: 'posts is required', trigger: 'change' }],

        user: [{ required: true, message: 'user is required', trigger: 'change' }]

      },
      childrenConfig: {

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

        posts: {
          id: undefined,
          domainName: 'posts',
          props: ['id'], // select.vue中显示的列，列如['id','name']
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.posts.id, null, this.childrenConfig.posts.domainName)
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
