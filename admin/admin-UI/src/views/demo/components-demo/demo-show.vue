<template>
  <el-dialog :title="$t('domain.show')" :visible.sync="config.sVisible" width="80%" @open="openDialog">
    <el-form ref="dataForm" v-loading="loading" label-position="right" label-width="auto" label-suffix="：" @submit.native.prevent>
      <div class="card-container">
        <el-card shadow="never">
          <div slot="header" class="clear-fix">
            <span>主从表单</span>
          </div>
          <el-row>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="$t('domain.gifCaptcha.gkey')">
                <span>{{ temp.gkey }}</span>
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="$t('domain.gifCaptcha.gvalue')">
                <span>{{ temp.gvalue }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="$t('domain.gifCaptcha.validTime')">
                <span>{{ temp.validTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </el-form-item>
            </el-col>
            <el-col :md="12" :sm="12" :xs="24" min-width="400px" class="grid-cell">
              <el-form-item :label="$t('domain.gifCaptcha.sessionId')">
                <span>{{ temp.sessionId }}</span>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
      </div>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="config.sVisible = false">
        {{ $t('domain.cancel') }}
      </el-button>
      <el-button type="primary" :disabled="loading || deleteLoading" @click="editData">
        {{ $t('domain.edit') }}
      </el-button>
      <el-button type="danger" :disabled="loading" :loading="deleteLoading" @click="deleteData">
        {{ $t('domain.delete') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
// import * as base from '@/api/base' // 通用的后台接口方法，如果需要放开接口
import baseShow from '@/utils/baseShow' // 通用的list页面方法，如果需要二次开发，在此页面复写相关方法即可

export default {
  name: 'DemoShow',
  props: {
    config: {
      type: Object,
      default: () => {
        return {
          id: undefined,
          domainName: undefined,
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
      loading: false,
      deleteLoading: false
    }
  },
  computed: {
    ...baseShow.computed
  },
  methods: {
    ...baseShow.methods
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/baseShow.scss";
</style>
