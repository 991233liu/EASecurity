import baseIndex from './baseIndex'

export default {
  data: {
    ...baseIndex.data,
    tableQuery: {
      search: {},
      offset: 0,
      max: 10,
      sort: null,
      order: null
    },
    list: null,
    total: 0
  },
  computed: {
    ...baseIndex.computed
  },
  methods: {
    ...baseIndex.methods,
    // 打开页面事件
    openDialog() {
      this.getData()
    },
    // 计算列是否显示
    columnVisible(prop) {
      let visible = false
      if (this.config.props) {
        this.config.props.forEach(item => {
          if (prop === item) {
            visible = true
            return
          }
        })
      }
      return visible
    },
    // 保存按钮
    saveData() {
      this.config.seVisible = false
      const ids = []
      this.$refs['tableRef'].selection.forEach(item => {
        ids.push(item)
      })
      this.config.seCallBack(this.config.parentProp, ids, this.config.parentHasMany)
    }
  }
}
