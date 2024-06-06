import * as base from '@/api/base'
import baseAll from './base'

export default {
  data: {
    ...baseAll.data
  },
  computed: {
    ...baseAll.computed
  },
  methods: {
    ...baseAll.methods,
    // 打开页面事件
    openDialog() {
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      this.getData()
    },
    // 加载数据
    getData() {
      if (this.config.eStatus === 'update') {
        this.loading = true
        base.show(this.config.domainName, this.config.id).then(response => {
          this.temp = response
          this.loading = false
        }).catch((error) => {
          console.log(error)
          this.loading = false
        })
      } else {
        for (const [k, v] of Object.entries(this.tempDef)) {
          this.temp[k] = v
        }
      }
    },
    // 新建页面保存按钮
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.saveLoading = true
          base.save(this.config.domainName, this.formDataFormat(this.temp)).then(response => {
            this.saveLoading = false
            this.config.eVisible = false
            this.config.eCallBack(true)
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 5 * 1000
            })
          }).catch((error) => {
            console.log(error)
            this.saveLoading = false
          })
        }
      })
    },
    // 编辑页面保存按钮
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.saveLoading = true
          base.update(this.config.domainName, this.formDataFormat(this.temp)).then(response => {
            this.saveLoading = false
            this.config.eCallBack(true)
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 5 * 1000
            })
          }).catch((error) => {
            console.log(error)
            this.saveLoading = false
          })
        }
      })
    },
    // 删除按钮
    deleteData() {
      this.deleteLoading = true
      base.deleteOne(this.config.domainName, this.config.id).then(response => {
        this.deleteLoading = false
        this.config.eVisible = false
        this.config.eCallBack(true)
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 5 * 1000
        })
      }).catch((error) => {
        console.log(error)
        this.deleteLoading = false
      })
    },
    // 表单数据保存到后台前格式化
    formDataFormat(data) {
      return data
    }
  }
}
