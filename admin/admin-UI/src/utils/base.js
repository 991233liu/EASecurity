import * as base from '@/api/base'
import enumClazz from '@/vendor/enum'

export default {
  data: {
  },
  computed: {
  },
  methods: {
    // 查看子对象数据
    handleShowChildren(id, prop, className) {
      const clazz = className.charAt(0).toLowerCase() + className.slice(1)
      this.childrenConfig[clazz].id = id
      this.childrenConfig[clazz].sVisible = true
    },
    // 编辑子对象数据
    handleEditChildren(id, prop, className) {
      const clazz = className.charAt(0).toLowerCase() + className.slice(1)
      this.childrenConfig[clazz].id = id
      this.childrenConfig[clazz].eStatus = 'update'
      this.childrenConfig[clazz].eVisible = true
    },
    // 添加子对象数据
    handleCreateChildren(prop, className, parentClassName, parentId, hasMany = false) {
      const clazz = className.charAt(0).toLowerCase() + className.slice(1)
      this.childrenConfig[clazz].parentId = parentId
      this.childrenConfig[clazz].parentClassName = parentClassName
      this.childrenConfig[clazz].parentProp = prop
      this.childrenConfig[clazz].parentHasMany = hasMany
      this.childrenConfig[clazz].seVisible = true
    },
    // 删除子对象数据
    handleDeleteChildren(id, prop, className, hasMany = false) {
      if (hasMany) {
        const index = this.temp[prop].findIndex(v => v.id === id)
        if (index !== undefined && index > -1) this.temp[prop].splice(index, 1)
      } else {
        this.temp[prop] = null
      }
    },
    // 获取Enum类型的数据
    getEnum(enumClass) {
      // 本地Enum类型
      let ve = enumClazz[enumClass]
      if (ve) {
        return ve
      } else {
        // 如果不是本地Enum类型，则使用服务器后台
        ve = window.sessionStorage[enumClass]
        // 从sessionStorage获取更新标识，如果不存在，则远程加载并更新localStorage
        if (!ve) {
          base.show('enumClass', enumClass).then(response => {
            let items = response.items
            this.enumClasses[enumClass] = items
            window.sessionStorage.setItem(enumClass, 0)
            items = JSON.stringify(items)
            if (items !== window.localStorage[enumClass]) {
              window.localStorage.setItem(enumClass, items)
              this.$forceUpdate()
            }
          }).catch((error) => {
            console.log(error)
          })
        }
        ve = window.localStorage[enumClass]
        return ve ? JSON.parse(ve) : undefined
      }
    }
  }
}
