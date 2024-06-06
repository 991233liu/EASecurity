
<template>
  <el-dialog :title="$t('domain.def.select')+'——'+$t('domain.uri.pageTitle')" :visible.sync="config.seVisible" :append-to-body="config.appendToBody" width="80%" class="show-dialog" @open="openDialog">
    <el-form ref="dataForm" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container select-card">
        <el-card shadow="always">
          <el-row :gutter="10" align="middle">

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'uri'|i18n('uri')">
                <el-input v-model="tableQuery.search.uri" type="text" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'classFullName'|i18n('uri')">
                <el-input v-model="tableQuery.search.classFullName" type="text" clearable />
              </el-form-item>
            </el-col>

            <el-col :md="6" :sm="12" :xs="24" class="grid-cell">
              <el-form-item :label="'methodName'|i18n('uri')">
                <el-input v-model="tableQuery.search.methodName" type="text" clearable />
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

            <el-table-column v-if="columnVisible('uri')" :label="'uri'|i18n('uri')" prop="uri" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span class="link-type" @click="handleShow(row.id)">{{ row.uri }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('classFullName')" :label="'classFullName'|i18n('uri')" prop="classFullName" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span class="link-type" @click="handleShow(row.id)">{{ row.classFullName }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('methodName')" :label="'methodName'|i18n('uri')" prop="methodName" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.methodName }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('methodSignature')" :label="'methodSignature'|i18n('uri')" prop="methodSignature" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.methodSignature }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('easType')" :label="'easType'|i18n('uri')" prop="easType" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.easType | fmatEnum(enumClasses['com.easecurity.core.access.annotation.EasType']) }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('fromTo')" :label="'fromTo'|i18n('uri')" prop="fromTo" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.fromTo | fmatEnum(enumClasses['com.easecurity.core.basis.re.UriEnum$FromTo']) }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('status')" :label="'status'|i18n('uri')" prop="status" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.status | fmatEnum(enumClasses['com.easecurity.core.basis.re.UriEnum$Status']) }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('uriIps')" :label="'uriIps'|i18n('uri')" prop="uriIps" sortable="custom" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span v-for="(item, index) in row.uriIps" :key="index" class="link-type span-inline" @click="handleShowChildren(item.id, 'uriIps', 'UriIp')">{{ item }}</span>
              </template>
            </el-table-column>

            <el-table-column v-if="columnVisible('id')" :label="'id'|i18n('uri')" prop="id" sortable="custom" min-width="100px" align="center">
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
  name: 'UriSelect',
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
