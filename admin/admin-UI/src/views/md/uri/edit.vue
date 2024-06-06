
<template>
  <el-dialog :title="$t('domain.def.' + config.eStatus)+'——'+$t('domain.uri.pageTitle')" :visible.sync="config.eVisible" :append-to-body="config.appendToBody" width="80%" class="edit-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container edit-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="edit-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'uri'|i18n('uri')" prop="uri">
                <el-input id="uri" v-model="temp.uri" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'classFullName'|i18n('uri')" prop="classFullName">
                <el-input id="classFullName" v-model="temp.classFullName" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'methodName'|i18n('uri')" prop="methodName">
                <el-input id="methodName" v-model="temp.methodName" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'methodSignature'|i18n('uri')" prop="methodSignature">
                <el-input id="methodSignature" v-model="temp.methodSignature" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'easType'|i18n('uri')" prop="easType">
                <el-select id="easType" v-model="temp.easType" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.access.annotation.EasType']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'fromTo'|i18n('uri')" prop="fromTo">
                <el-select id="fromTo" v-model="temp.fromTo" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.re.UriEnum$FromTo']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'status'|i18n('uri')" prop="status">
                <el-select id="status" v-model="temp.status" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.re.UriEnum$Status']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'uriIps'|i18n('uri')" prop="uriIps">
                <div v-for="(item, index) in temp.uriIps" :key="index" class="span-inline"><span class="link-type" @click="handleShowChildren(item.id, 'uriIps', 'UriIp')">{{ item }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(item.id, 'uriIps', 'UriIp', true)">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('uriIps', 'UriIp', 'uri', temp.id, true)">{{ $t('domain.def.add') }}</el-link>
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

    <UriIpShowView :config="childrenConfig.uriIp" />
    <UriIpSelectView :config="childrenConfig.uriIp" />
    <UriIpEditView :config="childrenConfig.uriIp" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'UriEdit',

  components: {

    UriIpShowView: () => import ('@/views/md/uriIp/show'),
    UriIpSelectView: () => import ('@/views/md/uriIp/select'),
    UriIpEditView: () => import ('@/views/md/uriIp/edit')

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
      temp: { uriIps: null },
      // 表单初始化数据，新建页面打开默认值
      tempDef: { easType: 'DATABASE_ONLY' },
      enumClasses: {

        'com.easecurity.core.access.annotation.EasType': this.getEnum('com.easecurity.core.access.annotation.EasType'),

        'com.easecurity.core.basis.re.UriEnum$FromTo': this.getEnum('com.easecurity.core.basis.re.UriEnum$FromTo'),

        'com.easecurity.core.basis.re.UriEnum$Status': this.getEnum('com.easecurity.core.basis.re.UriEnum$Status')

      },
      params: {},
      // 页面校验规则
      rules: {

        uri: [{ required: true, message: 'uri is required', trigger: 'blur' }],

        classFullName: [],

        methodName: [],

        methodSignature: [],

        easType: [],

        fromTo: [],

        status: [],

        uriIps: [{ type: 'array', message: 'uriIps must be array', trigger: 'change' }]

      },
      childrenConfig: {

        uriIp: {
          id: undefined,
          domainName: 'uriIp',
          props: ['id'], // select.vue中显示的列，列如['id','name']
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.uriIp.id, null, this.childrenConfig.uriIp.domainName)
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
