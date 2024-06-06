import { isIntNum } from './validate'
import { i18nLabel as i18n } from './i18n'

/**
 * Boolean型数据格式化
 */
export function fmatBoolean(value) {
  if (value === undefined || value === null || value === '') return ''
  return value ? i18n(value, 'domain') : i18n('false', 'domain')
}

/**
 * Enum型数据格式化
 */
export function fmatEnum(value, enumClass) {
  if (value === undefined || value === null || value === '') return ''

  if (!enumClass) return value

  // value如果为数字，则为序号，如果是string，则为code
  if (isIntNum(value)) {
    return enumClass[value].title
  } else {
    let title = value
    enumClass.forEach((it) => {
      if (it.code === value) title = it.title
    })
    return title
  }
}
