function Utils() {
  'use strict';

  function formatDate(date) {
    if (!date instanceof Date) {
      return;
    }

    return date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日'
  }

  return {
    formatDate: formatDate
  }
}