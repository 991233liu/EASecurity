
<template>
  <el-dialog :title="$t('domain.def.' + config.eStatus)+'——'+$t('domain.org.pageTitle')" :visible.sync="config.eVisible" :append-to-body="config.appendToBody" width="80%" class="edit-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container edit-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="edit-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'name'|i18n('org')" prop="name">
                <el-input id="name" v-model="temp.name" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'code'|i18n('org')" prop="code">
                <el-input id="code" v-model="temp.code" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'type'|i18n('org')" prop="type">
                <el-select id="type" v-model="temp.type" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.b.OrgEnum$Type']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'status'|i18n('org')" prop="status">
                <el-select id="status" v-model="temp.status" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.b.OrgEnum$Status']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'fullName'|i18n('org')" prop="fullName">
                <el-input id="fullName" v-model="temp.fullName" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'fullPathid'|i18n('org')" prop="fullPathid">
                <el-input id="fullPathid" v-model="temp.fullPathid" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'fullCode'|i18n('org')" prop="fullCode">
                <el-input id="fullCode" v-model="temp.fullCode" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'parent'|i18n('org')" prop="parent">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.parent.id, 'parent', 'Org')">{{ temp.parent }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.parent.id, 'parent', 'Org')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('parent', 'Org', 'org', temp.id)">{{ $t('domain.def.add') }}</el-link>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'dateCreated'|i18n('org')" prop="dateCreated">
                <el-date-picker id="dateCreated" v-model="temp.dateCreated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastUpdated'|i18n('org')" prop="lastUpdated">
                <el-date-picker id="lastUpdated" v-model="temp.lastUpdated" type="datetime" placeholder="Please pick a date" value-format="yyyy-MM-dd HH:mm:ss" />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'orgUsers'|i18n('org')" prop="orgUsers">
                <div v-for="(item, index) in temp.orgUsers" :key="index" class="span-inline"><span class="link-type" @click="handleShowChildren(item.id, 'orgUsers', 'OrgUser')">{{ item }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(item.id, 'orgUsers', 'OrgUser', true)">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('orgUsers', 'OrgUser', 'org', temp.id, true)">{{ $t('domain.def.add') }}</el-link>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'children'|i18n('org')" prop="children">
                <div v-for="(item, index) in temp.children" :key="index" class="span-inline"><span class="link-type" @click="handleShowChildren(item.id, 'children', 'Org')">{{ item }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(item.id, 'children', 'Org', true)">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('children', 'Org', 'org', temp.id, true)">{{ $t('domain.def.add') }}</el-link>
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

    <OrgUserShowView :config="childrenConfig.orgUser" />
    <OrgUserSelectView :config="childrenConfig.orgUser" />
    <OrgUserEditView :config="childrenConfig.orgUser" />

    <OrgShowView :config="childrenConfig.org" />
    <OrgSelectView :config="childrenConfig.org" />
    <OrgEditView :config="childrenConfig.org" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'OrgEdit',

  components: {

    OrgUserShowView: () => import ('@/views/md/orgUser/show'),
    OrgUserSelectView: () => import ('@/views/md/orgUser/select'),
    OrgUserEditView: () => import ('@/views/md/orgUser/edit'),

    OrgShowView: () => import ('@/views/md/org/show'),
    OrgSelectView: () => import ('@/views/md/org/select'),
    OrgEditView: () => import ('@/views/md/org/edit')

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
      temp: { parent: null, orgUsers: null, children: null },
      // 表单初始化数据，新建页面打开默认值
      tempDef: {},
      enumClasses: {

        'com.easecurity.core.basis.b.OrgEnum$Type': this.getEnum('com.easecurity.core.basis.b.OrgEnum$Type'),

        'com.easecurity.core.basis.b.OrgEnum$Status': this.getEnum('com.easecurity.core.basis.b.OrgEnum$Status')

      },
      params: {},
      // 页面校验规则
      rules: {

        name: [{ required: true, message: 'name is required', trigger: 'blur' }],

        code: [{ required: true, message: 'code is required', trigger: 'blur' }],

        type: [],

        status: [],

        fullName: [],

        fullPathid: [],

        fullCode: [],

        parent: [],

        dateCreated: [],

        lastUpdated: [],

        orgUsers: [{ type: 'array', message: 'orgUsers must be array', trigger: 'change' }],

        children: [{ type: 'array', message: 'children must be array', trigger: 'change' }]

      },
      childrenConfig: {

        orgUser: {
          id: undefined,
          domainName: 'orgUser',
          props: ['id'], // select.vue中显示的列，列如['id','name']
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.orgUser.id, null, this.childrenConfig.orgUser.domainName)
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
