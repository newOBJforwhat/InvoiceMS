var utils = (function () {
  'use strict'

  function formatDate(date) {
    if (!date instanceof Date) {
      return
    }

    return date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日'
  }

  function setModal(modal, content) {
    if (!modal.length) {
      return
    }

    content.header && modal.find('.modal-header').html(content.header)
    content.body && modal.find('.modal-body').html(content.body)
    if (typeof content.confirm === 'function') {
      modal.find('.confirm-button').get(0).onclick = content.confirm
    }

    if (typeof content.cancel === 'function') {
      modal.find('.cancel-button').get(0).onclick = content.cancel
    }
  }

  function debounce(func, wait, immediate) {
    var timeout, args, context, timestamp, result

    var later = function () {
      // 据上一次触发时间间隔
      var last = new Date().getTime() - timestamp

      // 上次被包装函数被调用时间间隔last小于设定时间间隔wait
      if (last < wait && last > 0) {
        timeout = setTimeout(later, wait - last)
      } else {
        timeout = null
        // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
        if (!immediate) {
          result = func.apply(context, args)
          if (!timeout) context = args = null
        }
      }
    }

    return function () {
      context = this
      args = arguments
      timestamp = new Date().getTime()
      var callNow = immediate && !timeout
      // 如果延时不存在，重新设定延时
      if (!timeout) timeout = setTimeout(later, wait)
      if (callNow) {
        result = func.apply(context, args)
        context = args = null
      }

      return result
    }
  }

  function errorValidate(jqObj, isError) {
    isError ? jqObj.parent().addClass('has-error') : jqObj.parent().removeClass('has-error')
  }

  return {
    formatDate: formatDate,
    setModal: setModal,
    debounce: debounce,
    errorValidate: errorValidate
  }
})()