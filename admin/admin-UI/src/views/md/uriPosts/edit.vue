
<template>
  <el-dialog :title="$t('domain.def.' + config.eStatus)+'——'+$t('domain.uriPosts.pageTitle')" :visible.sync="config.eVisible" :append-to-body="config.appendToBody" width="80%" class="edit-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container edit-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="edit-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'postsId'|i18n('uriPosts')" prop="postsId">
                <el-input id="postsId" v-model="temp.postsId" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'annotation'|i18n('uriPosts')" prop="annotation">
                <el-input id="annotation" v-model="temp.annotation" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'group1'|i18n('uriPosts')" prop="group1">
                <el-input id="group1" v-model="temp.group1" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'fromTo'|i18n('uriPosts')" prop="fromTo">
                <el-select id="fromTo" v-model="temp.fromTo" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.au.UriPostsEnum$FromTo']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'status'|i18n('uriPosts')" prop="status">
                <el-select id="status" v-model="temp.status" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.au.UriPostsEnum$Status']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'uri'|i18n('uriPosts')" prop="uri">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.uri.id, 'uri', 'Uri')">{{ temp.uri }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.uri.id, 'uri', 'Uri')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('uri', 'Uri', 'uriPosts', temp.id)">{{ $t('domain.def.add') }}</el-link>
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

    <UriShowView :config="childrenConfig.uri" />
    <UriSelectView :config="childrenConfig.uri" />
    <UriEditView :config="childrenConfig.uri" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'UriPostsEdit',

  components: {

    UriShowView: () => import ('@/views/md/uri/show'),
    UriSelectView: () => import ('@/views/md/uri/select'),
    UriEditView: () => import ('@/views/md/uri/edit')

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
      temp: { uri: null },
      // 表单初始化数据，新建页面打开默认值
      tempDef: {},
      enumClasses: {

        'com.easecurity.core.basis.au.UriPostsEnum$FromTo': this.getEnum('com.easecurity.core.basis.au.UriPostsEnum$FromTo'),

        'com.easecurity.core.basis.au.UriPostsEnum$Status': this.getEnum('com.easecurity.core.basis.au.UriPostsEnum$Status')

      },
      params: {},
      // 页面校验规则
      rules: {

        postsId: [{ type: 'integer', message: 'postsId must be integer', trigger: 'change' }],

        annotation: [],

        group1: [{ type: 'integer', message: 'group1 must be integer', trigger: 'change' }, { min: 1, max: 99, message: 'group1 must be range(min: 1, max: 99)', trigger: 'change' }],

        fromTo: [],

        status: [],

        uri: [{ required: true, message: 'uri is required', trigger: 'change' }]

      },
      childrenConfig: {

        uri: {
          id: undefined,
          domainName: 'uri',
          props: ['id'], // select.vue中显示的列，列如['id','name']
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.uri.id, null, this.childrenConfig.uri.domainName)
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
