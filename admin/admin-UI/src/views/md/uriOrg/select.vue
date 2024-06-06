
<template>
  <el-dialog :title="$t('domain.def.select')+'——'+$t('domain.uriOrg.pageTitle')" :visible.sync="config.seVisible" :append-to-body="config.appendToBody" width="80%" class="show-dialog" @open="openDialog">
    <el-form ref="dataForm" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container select-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle">

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'orgId'|i18n('uriOrg')">
                <el-input v-model="tableQuery.search.orgId" type="text" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'annotation'|i18n('uriOrg')">
                <el-input v-model="tableQuery.search.annotation" type="text" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'group1'|i18n('uriOrg')">
                <el-input v-model="tableQuery.search.group1" type="text" clearable />
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

            <el-table-column v-if="columnVisible('orgId')" :label="'orgId'|i18n('uriOrg')" prop="orgId" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span class="link-type" @click="handleShow(row.id)">{{ row.orgId }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('annotation')" :label="'annotation'|i18n('uriOrg')" prop="annotation" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span class="link-type" @click="handleShow(row.id)">{{ row.annotation }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('group1')" :label="'group1'|i18n('uriOrg')" prop="group1" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.group1 }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('fromTo')" :label="'fromTo'|i18n('uriOrg')" prop="fromTo" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.fromTo | fmatEnum(enumClasses['com.easecurity.core.basis.au.UriOrgEnum$FromTo']) }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('status')" :label="'status'|i18n('uriOrg')" prop="status" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.status | fmatEnum(enumClasses['com.easecurity.core.basis.au.UriOrgEnum$Status']) }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('uri')" :label="'uri'|i18n('uriOrg')" prop="uri" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span class="link-type span-inline" @click="handleShowChildren(row.uri.id, 'uri', 'Uri')">{{ row.uri }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('id')" :label="'id'|i18n('uriOrg')" prop="id" sortable="custom" min-width="100px" align="center">
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
  name: 'UriOrgSelect',
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
