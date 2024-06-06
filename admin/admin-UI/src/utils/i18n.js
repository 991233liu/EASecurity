import i18n from '@/lang' // internationalization

// translate router.meta.title, be used in breadcrumb sidebar tagsview
export function generateTitle(title) {
  const hasKey = this.$te('route.' + title)

  if (hasKey) {
    // $t :this method from vue-i18n, inject in @/lang/index.js
    const translatedTitle = this.$t('route.' + title)

    return translatedTitle
  }
  return title
}

// label的国际化，如果没有找到，则返回原值或者默认值
export function i18nLabel(label, domain, defStr) {
  let hasKey = i18n.te('domain.' + domain + '.' + label)
  if (hasKey) return i18n.t('domain.' + domain + '.' + label)

  hasKey = i18n.te('domain.def.' + label)
  if (hasKey) return i18n.t('domain.def.' + label)

  return defStr || label
}

// 国际化，如果没有找到，则返回默认值
export function i18nDef(key, defStr) {
  return i18n.te(key) ? i18n.t(key) : defStr
}
