<template>
  <el-dialog :title="$t('domain.' + config.eStatus)" :visible.sync="config.eVisible" width="80%" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" :model="temp" :rules="rules" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container">
        <el-card shadow="never">
          <div slot="header" class="clear-fix">
            <span>主从表单</span>
          </div>
          <el-row>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="$t('domain.gifCaptcha.gkey')" prop="gkey">
                <el-input id="gkey" v-model="temp.gkey" />
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="$t('domain.gifCaptcha.gvalue')" prop="gvalue">
                <el-input id="gvalue" v-model="temp.gvalue" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="$t('domain.gifCaptcha.validTime')" prop="validTime">
                <el-date-picker id="validTime" v-model="temp.validTime" type="datetime" placeholder="Please pick a date" />
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="$t('domain.gifCaptcha.sessionId')" prop="sessionId">
                <el-input id="sessionId" v-model="temp.sessionId" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="config.eVisible = false">
        {{ $t('domain.cancel') }}
      </el-button>
      <el-button type="primary" :disabled="loading || deleteLoading" :loading="saveLoading" @click="config.eStatus == 'create' ? createData() : updateData()">
        {{ $t('domain.save') }}
      </el-button>
      <el-button type="danger" :disabled="loading || saveLoading" :loading="deleteLoading" @click="deleteData">
        {{ $t('domain.delete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseEdit from '@/utils/baseEdit' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'DemoEdit',
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
      // 表单数据，如需设置页面打开默认值，可在此对象中添加
      temp: {},
      // 页面校验规则
      rules: {
        gkey: [{ required: true, message: 'gkey is required', trigger: 'change' }],
        gvalue: [{ required: true, message: 'gvalue is required', trigger: 'change' }],
        validTime: [{ type: 'date', required: true, message: 'validTime is required', trigger: 'change' }]
      },
      loading: false,
      saveLoading: false,
      deleteLoading: false
    }
  },
  computed: {
    ...baseEdit.computed
  },
  methods: {
    ...baseEdit.methods
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/baseEdit.scss";
</style>
