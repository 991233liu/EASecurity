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
      this.getData()
    },
    // 加载数据
    getData() {
      this.loading = true
      base.show(this.config.domainName, this.config.id, this.params).then(response => {
        this.temp = response
        this.loading = false
      }).catch((error) => {
        console.log(error)
        this.loading = false
      })
    },
    // 编辑按钮
    editData() {
      this.config.sVisible = false
      this.config.sCallBack(false, true)
    },
    // 删除按钮
    deleteData() {
      this.deleteLoading = true
      base.deleteOne(this.config.domainName, this.config.id).then(response => {
        this.deleteLoading = false
        this.config.sVisible = false
        this.config.sCallBack(true)
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
    }
  }
}
