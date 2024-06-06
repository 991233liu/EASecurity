import * as base from '@/api/base'
import baseAll from './base'

export default {
  data: {
    ...baseAll.data,
    tableQuery: {
      search: {},
      offset: 0,
      max: 10,
      sort: null,
      order: null
    },
    list: null,
    total: 0,
    listLoading: true,
    buttonDisabled: false,
    downloadLoading: false
  },
  computed: {
    ...baseAll.computed,
    // 当前页码
    page: {
      get() { return (this.tableQuery.offset / this.tableQuery.max) + 1 },
      set(val) { this.tableQuery.offset = (val - 1) * this.tableQuery.max }
    }
  },
  methods: {
    ...baseAll.methods,
    // 加载列表数据
    getData() {
      this.listLoading = true
      base.getList(this.config.domainName, this.tableQuery).then(response => {
        if (response.total) {
          this.list = response.items
          this.total = response.total
        } else {
          this.list = null
          this.total = 0
        }
        this.listLoading = false
      }).catch((error) => {
        this.listLoading = false
        console.log(error)
      })
    },
    // 搜索按钮
    handleFilter() {
      this.tableQuery.offset = 0
      this.getData()
    },
    // 查看某行
    handleShow(id) {
      this.config.id = id
      this.config.sVisible = true
    },
    // 添加按钮
    handleCreate() {
      this.config.id = null
      this.config.eStatus = 'create'
      this.config.eVisible = true
    },
    // 编辑按钮
    handleUpdate(id) {
      this.config.id = id
      this.config.eStatus = 'update'
      this.config.eVisible = true
    },
    // 行删除按钮
    handleDelete(id) {
      base.deleteOne(this.config.domainName, id).then(response => {
        this.handleFilter()
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 5 * 1000
        })
      }).catch((error) => {
        console.log(error)
      })
    },
    // 删除选中按钮
    handleDeleteAll() {
      const ids = []
      this.$refs['tableRef'].selection.forEach(item => {
        ids.push(item.id)
      })
      if (ids.length > 0) {
        this.buttonDisabled = true
        base.deleteAll(this.config.domainName, ids).then(response => {
          this.buttonDisabled = false
          this.handleFilter()
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 5 * 1000
          })
        }).catch((error) => {
          console.log(error)
          this.buttonDisabled = false
        })
      }
    },
    // 导出按钮
    handleDownload() {
      this.downloadLoading = true
      this.download(this.list)
    },
    // 导出全部按钮
    handleDownloadAll() {
      this.downloadLoading = true
      base.getList(this.config.domainName, { search: this.tableQuery.search, sort: this.tableQuery.sort, order: this.tableQuery.order }).then(response => {
        this.download(response.items)
      })
    },
    // 导出Excel
    download(data) {
      import('@/vendor/Export2Excel').then(excel => {
        const filename = this.config.download.excelName
        let header = this.config.download.excelHeader
        if (!header) {
          header = []
          for (const item in this.config.props) {
            header.push(this.config.props[item].title)
          }
        }
        let excelVal = this.config.download.excelVal
        if (!excelVal) {
          excelVal = []
          for (const item in this.config.props) {
            excelVal.push(item)
          }
        }
        data = this.downloadDataFormat(data, excelVal)
        excel.export_json_to_excel({
          header,
          data,
          filename
        })
        this.downloadLoading = false
      }).catch((error) => {
        this.downloadLoading = false
        console.log(error)
      })
    },
    // Excel值格式化
    downloadDataFormat(data, excelVal) {
      return data.map(v => excelVal.map(j => { return v[j] }))
    },
    // 列表序号
    tableIndex(index) {
      return (this.page - 1) * this.tableQuery.max + index + 1
    },
    // 列表排序
    sortChange(data) {
      const { prop, order } = data
      this.tableQuery.sort = prop
      if (order === 'descending') {
        this.tableQuery.order = 'desc'
      } else {
        this.tableQuery.order = 'asc'
      }
      this.handleFilter()
    },
    // 重置table列显示状态
    headerReset() {
      for (const item in this.config.props) {
        if (this.config.props[item].defVisible) {
          this.config.props[item].visible = true
        } else {
          this.config.props[item].visible = false
        }
      }
    },
    // 列选择事件。hasMany = true时为多选
    handleSelectionChange(selection, hasMany = false) {
      if (!hasMany && selection.length > 1) {
        this.$refs['tableRef'].clearSelection()
        this.$refs['tableRef'].toggleRowSelection(selection.pop())
      }
    }
  }
}
