
<template>
  <el-dialog :title="$t('domain.def.show')+'——'+$t('domain.menu.pageTitle')" :visible.sync="config.sVisible" :append-to-body="config.appendToBody" width="80%" class="show-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container show-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="show-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'name'|i18n('menu')">
                <span>{{ temp.name }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'code'|i18n('menu')">
                <span>{{ temp.code }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'level'|i18n('menu')">
                <span>{{ temp.level | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$Level']) }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'sortNumber'|i18n('menu')">
                <span>{{ temp.sortNumber }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'openUrl'|i18n('menu')">
                <span>{{ temp.openUrl }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'icon'|i18n('menu')">
                <span>{{ temp.icon }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'fullName'|i18n('menu')">
                <span>{{ temp.fullName }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'status'|i18n('menu')">
                <span>{{ temp.status | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$Status']) }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'displayStatus'|i18n('menu')">
                <span>{{ temp.displayStatus | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$DisplayStatus']) }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'disablePrompt'|i18n('menu')">
                <span>{{ temp.disablePrompt }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'noPermissionsPrompt'|i18n('menu')">
                <span>{{ temp.noPermissionsPrompt }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'accessType'|i18n('menu')">
                <span>{{ temp.accessType | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$AccessType']) }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'parent'|i18n('menu')">
                <span class="link-type span-inline" @click="handleShowChildren(temp.parent.id, 'parent', 'Menu')">{{ temp.parent }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'children'|i18n('menu')">
                <span v-for="(item, index) in temp.children" :key="index" class="link-type span-inline" @click="handleShowChildren(item.id, 'children', 'Menu')">{{ item }}</span>
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

    <MenuShowView :config="childrenConfig.menu" />
    <MenuEditView :config="childrenConfig.menu" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseShow from '@/utils/baseShow' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'MenuShow',

  components: {

    MenuShowView: () => import ('@/views/md/menu/show'),
    MenuEditView: () => import ('@/views/md/menu/edit')

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

        'com.easecurity.core.basis.re.MenuEnum$Level': this.getEnum('com.easecurity.core.basis.re.MenuEnum$Level'),

        'com.easecurity.core.basis.re.MenuEnum$Status': this.getEnum('com.easecurity.core.basis.re.MenuEnum$Status'),

        'com.easecurity.core.basis.re.MenuEnum$DisplayStatus': this.getEnum('com.easecurity.core.basis.re.MenuEnum$DisplayStatus'),

        'com.easecurity.core.basis.re.MenuEnum$AccessType': this.getEnum('com.easecurity.core.basis.re.MenuEnum$AccessType')

      },
      params: {},
      childrenConfig: {

        menu: {
          id: undefined,
          domainName: 'menu',
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
