
<template>
  <el-dialog :title="$t('domain.def.' + config.eStatus)+'——'+$t('domain.menu.pageTitle')" :visible.sync="config.eVisible" :append-to-body="config.appendToBody" width="80%" class="edit-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container edit-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="edit-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'name'|i18n('menu')" prop="name">
                <el-input id="name" v-model="temp.name" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'code'|i18n('menu')" prop="code">
                <el-input id="code" v-model="temp.code" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'level'|i18n('menu')" prop="level">
                <el-select id="level" v-model="temp.level" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.re.MenuEnum$Level']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'sortNumber'|i18n('menu')" prop="sortNumber">
                <el-input id="sortNumber" v-model="temp.sortNumber" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'openUrl'|i18n('menu')" prop="openUrl">
                <el-input id="openUrl" v-model="temp.openUrl" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'icon'|i18n('menu')" prop="icon">
                <el-input id="icon" v-model="temp.icon" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'fullName'|i18n('menu')" prop="fullName">
                <el-input id="fullName" v-model="temp.fullName" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'status'|i18n('menu')" prop="status">
                <el-select id="status" v-model="temp.status" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.re.MenuEnum$Status']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'displayStatus'|i18n('menu')" prop="displayStatus">
                <el-select id="displayStatus" v-model="temp.displayStatus" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.re.MenuEnum$DisplayStatus']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'disablePrompt'|i18n('menu')" prop="disablePrompt">
                <el-input id="disablePrompt" v-model="temp.disablePrompt" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'noPermissionsPrompt'|i18n('menu')" prop="noPermissionsPrompt">
                <el-input id="noPermissionsPrompt" v-model="temp.noPermissionsPrompt" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'accessType'|i18n('menu')" prop="accessType">
                <el-select id="accessType" v-model="temp.accessType" :placeholder="$t('domain.def.selectPlaceholder')"><el-option v-for="(item, index) in enumClasses['com.easecurity.core.basis.re.MenuEnum$AccessType']" :key="index" :label="item.title" :value="item.code" /></el-select>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'parent'|i18n('menu')" prop="parent">
                <div class="span-inline"><span class="link-type" @click="handleShowChildren(temp.parent.id, 'parent', 'Menu')">{{ temp.parent }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(temp.parent.id, 'parent', 'Menu')">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('parent', 'Menu', 'menu', temp.id)">{{ $t('domain.def.add') }}</el-link>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'children'|i18n('menu')" prop="children">
                <div v-for="(item, index) in temp.children" :key="index" class="span-inline"><span class="link-type" @click="handleShowChildren(item.id, 'children', 'Menu')">{{ item }}</span><el-link type="danger" icon="el-icon-delete" @click="handleDeleteChildren(item.id, 'children', 'Menu', true)">{{ $t('domain.def.delete') }}</el-link></div><el-link type="primary" icon="el-icon-edit" @click="handleCreateChildren('children', 'Menu', 'menu', temp.id, true)">{{ $t('domain.def.add') }}</el-link>
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

    <MenuShowView :config="childrenConfig.menu" />
    <MenuSelectView :config="childrenConfig.menu" />
    <MenuEditView :config="childrenConfig.menu" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'MenuEdit',

  components: {

    MenuShowView: () => import ('@/views/md/menu/show'),
    MenuSelectView: () => import ('@/views/md/menu/select'),
    MenuEditView: () => import ('@/views/md/menu/edit')

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
      temp: { parent: null, children: null },
      // 表单初始化数据，新建页面打开默认值
      tempDef: { displayStatus: 'NOPERMISSIONSHIDDEN', accessType: 'LOGIN' },
      enumClasses: {

        'com.easecurity.core.basis.re.MenuEnum$Level': this.getEnum('com.easecurity.core.basis.re.MenuEnum$Level'),

        'com.easecurity.core.basis.re.MenuEnum$Status': this.getEnum('com.easecurity.core.basis.re.MenuEnum$Status'),

        'com.easecurity.core.basis.re.MenuEnum$DisplayStatus': this.getEnum('com.easecurity.core.basis.re.MenuEnum$DisplayStatus'),

        'com.easecurity.core.basis.re.MenuEnum$AccessType': this.getEnum('com.easecurity.core.basis.re.MenuEnum$AccessType')

      },
      params: {},
      // 页面校验规则
      rules: {

        name: [{ required: true, message: 'name is required', trigger: 'blur' }],

        code: [],

        level: [],

        sortNumber: [{ type: 'integer', message: 'sortNumber must be integer', trigger: 'change' }],

        openUrl: [],

        icon: [],

        fullName: [],

        status: [],

        displayStatus: [],

        disablePrompt: [],

        noPermissionsPrompt: [],

        accessType: [],

        parent: [{ required: true, message: 'parent is required', trigger: 'change' }],

        children: [{ type: 'array', message: 'children must be array', trigger: 'change' }]

      },
      childrenConfig: {

        menu: {
          id: undefined,
          domainName: 'menu',
          props: ['id'], // select.vue中显示的列，列如['id','name']
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.menu.id, null, this.childrenConfig.menu.domainName)
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
