function Utils() {
  'use strict';

  function formatDate(date) {
    if (!date instanceof Date) {
      return;
    }

    return date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日'
  }

  function setModal(modal, content) {
    if (!modal.length) {
      return
    }

    content.header && modal.find('.modal-header').html(content.header);
    content.body && modal.find('.modal-body').html(content.body);
    if (typeof content.confirm === 'function') {
      modal.find('.confirm-button').get(0).onclick = content.confirm
    }

    if (typeof content.cancel === 'function') {
      modal.find('.cancel-button').get(0).onclick = content.cancel
    }
  }

  return {
    formatDate: formatDate,
    setModal: setModal
  }
}