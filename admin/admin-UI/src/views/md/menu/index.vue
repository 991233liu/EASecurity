
<template>
  <el-form ref="vForm" label-position="right" label-width="auto" @submit.native.prevent>
    <div class="card-container search-card">
      <el-card shadow="never">
        <div slot="header" class="card-header">
          <span>{{ $t('domain.menu.pageTitle') }}</span>
        </div>
        <el-row :gutter="10" align="middle">

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'name'|i18n('menu')">
              <el-input v-model="tableQuery.search.name" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'code'|i18n('menu')">
              <el-input v-model="tableQuery.search.code" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'level'|i18n('menu')">
              <el-input v-model="tableQuery.search.level" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'sortNumber'|i18n('menu')">
              <el-input v-model="tableQuery.search.sortNumber" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'openUrl'|i18n('menu')">
              <el-input v-model="tableQuery.search.openUrl" type="text" clearable />
            </el-form-item>
          </el-col>

          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item label-width="0" class="form-button-item">
              <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
                {{ $t('table.search') }}
              </el-button>
              <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">
                {{ $t('table.add') }}
              </el-button>
              <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" :disabled="listLoading || buttonDisabled" @click="handleDownload">
                {{ $t('table.export') }}
              </el-button>
              <el-button class="filter-item" type="danger" icon="el-icon-delete" :disabled="listLoading || buttonDisabled" @click="handleDeleteAll">
                {{ $t('table.delete') }}
              </el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <div class="card-container list-card">
      <el-card style="{width: 100% !important}" shadow="never">
        <el-table
          ref="tableRef"
          v-loading="listLoading"
          :data="list"
          stripe
          border
          highlight-selection-row
          style="width: 100%;"
          @sort-change="sortChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column type="index" :label="$t('table.id')" :index="tableIndex" width="55" align="center" />

          <el-table-column v-if="config.props.name.visible" :label="'name'|i18n('menu')" prop="name" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.name }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.code.visible" :label="'code'|i18n('menu')" prop="code" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.code }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.level.visible" :label="'level'|i18n('menu')" prop="level" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.level | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$Level']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.sortNumber.visible" :label="'sortNumber'|i18n('menu')" prop="sortNumber" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.sortNumber }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.openUrl.visible" :label="'openUrl'|i18n('menu')" prop="openUrl" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.openUrl }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.icon.visible" :label="'icon'|i18n('menu')" prop="icon" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.icon }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.fullName.visible" :label="'fullName'|i18n('menu')" prop="fullName" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.fullName }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.status.visible" :label="'status'|i18n('menu')" prop="status" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.status | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$Status']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.displayStatus.visible" :label="'displayStatus'|i18n('menu')" prop="displayStatus" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.displayStatus | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$DisplayStatus']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.disablePrompt.visible" :label="'disablePrompt'|i18n('menu')" prop="disablePrompt" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.disablePrompt }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.noPermissionsPrompt.visible" :label="'noPermissionsPrompt'|i18n('menu')" prop="noPermissionsPrompt" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.noPermissionsPrompt }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.accessType.visible" :label="'accessType'|i18n('menu')" prop="accessType" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.accessType | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$AccessType']) }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.parent.visible" :label="'parent'|i18n('menu')" prop="parent" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span class="link-type span-inline" @click="handleShowChildren(row.parent.id, 'parent', 'Menu')">{{ row.parent }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.children.visible" :label="'children'|i18n('menu')" prop="children" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span v-for="(item, index) in row.children" :key="index" class="link-type span-inline" @click="handleShowChildren(item.id, 'children', 'Menu')">{{ item }}</span>
            </template>
          </el-table-column>

          <el-table-column v-if="config.props.id.visible" :label="'id'|i18n('menu')" prop="id" sortable="custom" min-width="100px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.id }}</span>
            </template>
          </el-table-column>

          <el-table-column align="center" min-width="150" class-name="small-padding fixed-width">
            <template #header>
              <span>{{ $t('table.actions') }} </span>
              <el-popover ref="actPopover" placement="bottom" :title="$t('table.actPopover')" width="350" trigger="click">
                <el-card shadow="never">
                  <el-col v-for="(v, k) in config.props" :key="k" :md="12" :sm="12" :xs="24" class="grid-cell">
                    <el-checkbox v-model="config.props[k].visible">
                      {{ v.title }}
                    </el-checkbox>
                  </el-col>
                </el-card>
                <div class="static-content-item">
                  <el-button type="primary" size="mini" @click="headerReset">{{ $t('table.reset') }}</el-button>
                </div>
              </el-popover>
              <el-link v-popover:actPopover :underline="false"><svg-icon icon-class="list2" /></el-link>
            </template>
            <template slot-scope="{row}">
              <el-button type="primary" size="mini" @click="handleUpdate(row.id)">
                {{ $t('table.edit') }}
              </el-button>
              <el-button size="mini" type="danger" @click="handleDelete(row.id)">
                {{ $t('table.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="tableQuery.max" style="padding: 0px 16px" @pagination="getData" />
      </el-card>
    </div>
    <ShowView :config="config" />
    <EditView :config="config" />

    <MenuShowView :config="childrenConfig.menu" />
    <MenuEditView :config="childrenConfig.menu" />

  </el-form>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { i18nLabel as i18n } from '@/utils/i18n'
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseIndex from '@/utils/baseIndex' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可
import ShowView from './show'
import EditView from './edit'

import MenuShowView from '@/views/md/menu/show'
import MenuEditView from '@/views/md/menu/edit'

export default {
  name: 'MenuList', // name值需要与router中的name相同，否则页签的“keep-alive”功能不生效

  components: { Pagination, ShowView, EditView, MenuShowView, MenuEditView },
  data() {
    return {
      ...baseIndex.data,
      config: {
        id: undefined,
        domainName: 'menu',
        // 属性列表
        props: {

          name: {
            title: i18n('name', 'menu'),
            defVisible: true,
            visible: true
          },

          code: {
            title: i18n('code', 'menu'),
            defVisible: true,
            visible: true
          },

          level: {
            title: i18n('level', 'menu'),
            defVisible: true,
            visible: true
          },

          sortNumber: {
            title: i18n('sortNumber', 'menu'),
            defVisible: true,
            visible: true
          },

          openUrl: {
            title: i18n('openUrl', 'menu'),
            defVisible: true,
            visible: true
          },

          icon: {
            title: i18n('icon', 'menu'),
            defVisible: true,
            visible: true
          },

          fullName: {
            title: i18n('fullName', 'menu'),
            defVisible: false
          },

          status: {
            title: i18n('status', 'menu'),
            defVisible: false
          },

          displayStatus: {
            title: i18n('displayStatus', 'menu'),
            defVisible: false
          },

          disablePrompt: {
            title: i18n('disablePrompt', 'menu'),
            defVisible: false
          },

          noPermissionsPrompt: {
            title: i18n('noPermissionsPrompt', 'menu'),
            defVisible: false
          },

          accessType: {
            title: i18n('accessType', 'menu'),
            defVisible: false
          },

          parent: {
            title: i18n('parent', 'menu'),
            defVisible: false
          },

          children: {
            title: i18n('children', 'menu'),
            defVisible: false
          },

          id: {
            title: i18n('id', 'menu'),
            defVisible: false
          }

        },
        // 导出Excel配置
        download: {
          excelName: this.$t('domain.menu.pageTitle'),
          excelHeader: undefined, // 导出Excel中列的名字，默认为config.props.*.title
          excelVal: undefined // 导出Excel中值的字段名，默认为config.props.*
        },
        sVisible: false,
        sCallBack: (refresh, openEdit) => {
          if (refresh) {
            this.handleFilter()
          }
          if (openEdit) {
            this.handleUpdate(this.config.id)
          }
        },
        eStatus: 'create',
        eVisible: false,
        eCallBack: (refresh) => {
          if (refresh) {
            this.handleFilter()
          }
        }
      },
      enumClasses: {

        'com.easecurity.core.basis.re.MenuEnum$Level': this.getEnum('com.easecurity.core.basis.re.MenuEnum$Level'),

        'com.easecurity.core.basis.re.MenuEnum$Status': this.getEnum('com.easecurity.core.basis.re.MenuEnum$Status'),

        'com.easecurity.core.basis.re.MenuEnum$DisplayStatus': this.getEnum('com.easecurity.core.basis.re.MenuEnum$DisplayStatus'),

        'com.easecurity.core.basis.re.MenuEnum$AccessType': this.getEnum('com.easecurity.core.basis.re.MenuEnum$AccessType')

      },
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

      }
    }
  },
  computed: {
    ...baseIndex.computed
  },
  created() {
    // this.tableQuery.deep = true // 如果需要显示子类中的属性，可以放开，但是会出现N+1数据库查询问题，影响性能
    this.getData()
  },
  methods: {
    ...baseIndex.methods
  }
}
</script>

<style lang="scss">
@import "~@/styles/base.scss";
</style>

<style lang="scss" scoped>
@import "~@/styles/baseIndex.scss";
</style>
