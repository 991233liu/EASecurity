
<template>
  <el-dialog :title="$t('domain.def.select')+'——'+$t('domain.menu.pageTitle')" :visible.sync="config.seVisible" :append-to-body="config.appendToBody" width="80%" class="show-dialog" @open="openDialog">
    <el-form ref="dataForm" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container select-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle">

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'name'|i18n('menu')">
                <el-input v-model="tableQuery.search.name" type="text" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'code'|i18n('menu')">
                <el-input v-model="tableQuery.search.code" type="text" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'level'|i18n('menu')">
                <el-input v-model="tableQuery.search.level" type="text" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item label-width="0" class="form-button-item">
                <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
                  {{ $t('table.search') }}
                </el-button>
              </el-form-item>
            </el-col>
          </el-row>

          <el-table
            ref="tableRef"
            v-loading="listLoading"
            :data="list"
            stripe
            border
            highlight-selection-row
            style="width: 100%;"
            @selection-change="handleSelectionChange($event, config.parentHasMany)"
            @sort-change="sortChange"
          >
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column type="index" :label="$t('table.id')" :index="tableIndex" width="55" align="center" />

            <el-table-column v-if="columnVisible('name')" :label="'name'|i18n('menu')" prop="name" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span class="link-type" @click="handleShow(row.id)">{{ row.name }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('code')" :label="'code'|i18n('menu')" prop="code" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span class="link-type" @click="handleShow(row.id)">{{ row.code }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('level')" :label="'level'|i18n('menu')" prop="level" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.level | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$Level']) }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('sortNumber')" :label="'sortNumber'|i18n('menu')" prop="sortNumber" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.sortNumber }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('openUrl')" :label="'openUrl'|i18n('menu')" prop="openUrl" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.openUrl }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('icon')" :label="'icon'|i18n('menu')" prop="icon" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.icon }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('fullName')" :label="'fullName'|i18n('menu')" prop="fullName" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.fullName }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('status')" :label="'status'|i18n('menu')" prop="status" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.status | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$Status']) }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('displayStatus')" :label="'displayStatus'|i18n('menu')" prop="displayStatus" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.displayStatus | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$DisplayStatus']) }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('disablePrompt')" :label="'disablePrompt'|i18n('menu')" prop="disablePrompt" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.disablePrompt }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('noPermissionsPrompt')" :label="'noPermissionsPrompt'|i18n('menu')" prop="noPermissionsPrompt" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.noPermissionsPrompt }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('accessType')" :label="'accessType'|i18n('menu')" prop="accessType" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.accessType | fmatEnum(enumClasses['com.easecurity.core.basis.re.MenuEnum$AccessType']) }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('parent')" :label="'parent'|i18n('menu')" prop="parent" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span class="link-type span-inline" @click="handleShowChildren(row.parent.id, 'parent', 'Menu')">{{ row.parent }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('children')" :label="'children'|i18n('menu')" prop="children" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span v-for="(item, index) in row.children" :key="index" class="link-type span-inline" @click="handleShowChildren(item.id, 'children', 'Menu')">{{ item }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('id')" :label="'id'|i18n('menu')" prop="id" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.id }}</span>
              </template>
            </el-table-column>

          </el-table>

          <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="tableQuery.max" style="padding: 0px 16px" @pagination="getData" />
        </el-card>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="config.seVisible = false">
        {{ $t('domain.def.cancel') }}
      </el-button>
      <el-button type="primary" :disabled="saveLoading" :loading="listLoading" @click="saveData()">
        {{ $t('domain.def.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseSelect from '@/utils/baseSelect' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'MenuSelect',
  components: { Pagination },
  props: {
    config: {
      type: Object,
      default: () => {
        return {
          parentId: undefined,
          parentClassName: undefined,
          parentHasMany: undefined,
          parentProp: undefined,
          domainName: undefined,
          props: undefined,
          appendToBody: false,
          seVisible: undefined,
          seCallBack: undefined
        }
      }
    }
  },
  data() {
    return {
      ...baseSelect.data,
      saveLoading: false
    }
  },
  computed: {
    ...baseSelect.computed
  },
  created() {
    // this.tableQuery.deep = true // 如果需要显示子类中的属性，可以放开，但是会出现N+1数据库查询问题，影响性能
    this.tableQuery.expand = ''
  },
  methods: {
    ...baseSelect.methods
  }
}
</script>

<style lang="scss">
@import "~@/styles/base.scss";
</style>

<style lang="scss" scoped>
@import "~@/styles/baseSelect.scss";
</style>
