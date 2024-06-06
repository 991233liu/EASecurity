<template>
  <el-form ref="vForm" label-position="right" label-width="auto" @submit.native.prevent>
    <div class="card-container">
      <el-card shadow="never">
        <div slot="header" class="clear-fix">
          <span>主从表单</span>
        </div>
        <el-row>
          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="'gkey'|i18n('gifCaptcha')">
              <el-input v-model="tableQuery.search.gkey" type="text" clearable />
            </el-form-item>
          </el-col>
          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="$t('domain.gifCaptcha.gvalue')">
              <el-input v-model="tableQuery.search.gvalue" type="text" clearable />
            </el-form-item>
          </el-col>
          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="$t('domain.gifCaptcha.sessionId')" clearable>
              <el-input v-model="tableQuery.search.sessionId" />
            </el-form-item>
          </el-col>
          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item :label="$t('domain.gifCaptcha.validTime')">
              <el-date-picker v-model="tableQuery.search.validTime" type="date" class="full-width-input" format="yyyy-MM-dd" value-format="yyyy-MM-dd" clearable />
            </el-form-item>
          </el-col>
          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <el-form-item label="是否需要发票">
              <el-switch v-model="tableQuery.search.shifouxuyaofapiao" active-text="需要" inactive-text="不需要" />
            </el-form-item>
          </el-col>
          <el-col :md="8" :sm="12" :xs="24" class="grid-cell">
            <div class="static-content-item">
              <el-button size="mini" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
                {{ $t('table.search') }}
              </el-button>
              <el-button size="mini" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">
                {{ $t('table.add') }}
              </el-button>
              <el-button :loading="downloadLoading" size="mini" class="filter-item" type="primary" icon="el-icon-download" :disabled="listLoading || buttonDisabled" @click="handleDownload">
                {{ $t('table.export') }}
              </el-button>
              <el-button size="mini" class="filter-item" type="danger" icon="el-icon-delete" :disabled="listLoading || buttonDisabled" @click="handleDeleteAll">
                {{ $t('table.delete') }}
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <div class="card-container">
      <el-card style="{width: 100% !important}" shadow="never">
        <el-table
          ref="tableRef"
          v-loading="listLoading"
          :data="list"
          stripe
          border
          highlight-current-row
          style="width: 100%;"
          @sort-change="sortChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column type="index" :label="$t('table.id')" :index="tableIndex" width="55" align="center" />
          <el-table-column v-if="config.props.id.visible" :label="$t('domain.id')" prop="id" sortable="custom" align="center" width="80">
            <template slot-scope="{row}">
              <span>{{ row.id }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="config.props.gkey.visible" :label="$t('domain.gifCaptcha.gkey')" prop="gkey" sortable="custom" min-width="200px" align="center">
            <template slot-scope="{row}">
              <span class="link-type" @click="handleShow(row.id)">{{ row.gkey }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="config.props.gvalue.visible" :label="$t('domain.gifCaptcha.gvalue')" prop="gvalue" sortable="custom" min-width="100px">
            <template slot-scope="{row}">
              <span>{{ row.gvalue }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="config.props.validTime.visible" :label="$t('domain.gifCaptcha.validTime')" min-width="150px" align="center">
            <template slot-scope="{row}">
              <span>{{ row.validTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="config.props.sessionId.visible" :label="$t('domain.gifCaptcha.sessionId')" min-width="200px">
            <template slot-scope="{row}">
              <span>{{ row.sessionId }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" min-width="150" class-name="small-padding fixed-width">
            <template #header>
              <span>{{ $t('table.actions') }} </span>
              <el-popover ref="actPopover" placement="bottom" :title="$t('table.actPopover')" width="300" trigger="click">
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

        <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="tableQuery.max" style="padding: 0px 16px" @pagination="getList" />
      </el-card>
    </div>
    <ShowView :config="config" />
    <EditView :config="config" />
  </el-form>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseIndex from '@/utils/baseIndex' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可
import ShowView from './demo-show'
import EditView from './demo-edit'

export default {
  name: 'DemoList', // name值需要与router中的name相同，否则页签的“keep-alive”功能不生效
  components: { Pagination, ShowView, EditView },
  data() {
    return {
      ...baseIndex.data,
      config: {
        id: undefined,
        domainName: 'gifCaptcha',
        // 属性列表
        props: {
          gkey: {
            title: this.$t('domain.gifCaptcha.gkey'),
            defVisible: true,
            visible: true
          },
          gvalue: {
            title: this.$t('domain.gifCaptcha.gvalue'),
            defVisible: true,
            visible: true
          },
          sessionId: {
            title: this.$t('domain.gifCaptcha.sessionId'),
            defVisible: true,
            visible: true
          },
          validTime: {
            title: this.$t('domain.gifCaptcha.validTime'),
            defVisible: true,
            visible: true
          },
          id: {
            title: this.$t('domain.id'),
            defVisible: false
          }
        },
        // 导出Excel配置
        download: {
          excelName: this.$t('domain.gifCaptcha.pageTitle'),
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
      }
    }
  },
  computed: {
    ...baseIndex.computed
  },
  created() {
    this.getList()
  },
  methods: {
    ...baseIndex.methods
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/baseIndex.scss";
</style>
