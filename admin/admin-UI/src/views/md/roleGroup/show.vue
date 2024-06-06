
<template>
  <el-dialog :title="$t('domain.def.show')+'——'+$t('domain.roleGroup.pageTitle')" :visible.sync="config.sVisible" :append-to-body="config.appendToBody" width="80%" class="show-dialog" :close-on-click-modal="false" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container show-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle" class="show-row">

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'name'|i18n('roleGroup')">
                <span>{{ temp.name }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'code'|i18n('roleGroup')">
                <span>{{ temp.code }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'dateCreated'|i18n('roleGroup')">
                <span>{{ temp.dateCreated }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'lastUpdated'|i18n('roleGroup')">
                <span>{{ temp.lastUpdated }}</span>
              </el-form-item>
            </el-col>

            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="'roles'|i18n('roleGroup')">
                <span v-for="(item, index) in temp.roles" :key="index" class="link-type span-inline" @click="handleShowChildren(item.id, 'roles', 'Role')">{{ item }}</span>
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

    <RoleShowView :config="childrenConfig.role" />
    <RoleEditView :config="childrenConfig.role" />

  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseShow from '@/utils/baseShow' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'RoleGroupShow',

  components: {

    RoleShowView: () => import ('@/views/md/role/show'),
    RoleEditView: () => import ('@/views/md/role/edit')

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

      },
      params: {},
      childrenConfig: {

        role: {
          id: undefined,
          domainName: 'role',
          appendToBody: true,
          sVisible: false,
          sCallBack: (refresh, openEdit) => {
            if (refresh) {
              this.getData()
            }
            if (openEdit) {
              this.handleEditChildren(this.childrenConfig.role.id, null, this.childrenConfig.role.domainName)
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
