function verify(role) {
  'use strict'

  $('#table-container').html('<table id="table"></table>')
  var $table = $('#table')

  var tableColumns = [{
    field: 'invoiceId',
    title: '发票号'
  }, {
    field: 'user',
    title: '录入者'
  }, {
    field: 'money',
    title: '金额'
  }, {
    field: 'supplierName',
    title: '供应商名字'
  }, {
    field: 'date',
    title: '开票日期'
  }]

  if (role === 'department' || role === 'financial') {
    tableColumns.push({field: 'operation', title: '操作'})
  }

  if (role === 'financial') {
    tableColumns.splice(tableColumns.length - 2, 0, {field: 'auditing', title: '经办人'})
  }

  $table.bootstrapTable({
    search: true,
    pagination: true,
    pageSize: 10,
    columns: tableColumns,
    data: [{
      invoiceId: '23213213',
      user: '大锤',
      money: '212',
      supplierName: '我发了看得见费',
      date: utils.formatDate(new Date()),
      operation: '<div class="verify-operation"><a href="javascript:void(0)">通过</a>&nbsp;<a href="javascript:void(0)">驳回</a></div>'
    }, {
      invoiceId: '231231232',
      user: '二锤',
      money: '221',
      supplierName: '大丰收',
      date: utils.formatDate(new Date()),
      operation: '<div class="verify-operation"><a href="javascript:void(0)">通过</a>&nbsp;<a href="javascript:void(0)">驳回</a></div>'
    }, {
      invoiceId: '32132123123123123',
      user: '三锤',
      money: '312',
      supplierName: '分胜负',
      date: utils.formatDate(new Date()),
      operation: '<div class="verify-operation"><a href="javascript:void(0)">通过</a>&nbsp;<a href="javascript:void(0)">驳回</a></div>'
    }]
  })

  $('.verify-operation').each(function (index, elm) {
    $(elm).find('a').each(function (i, a) {
      if (i === 0) {
        a.onclick = pass
      } else {
        a.onclick = reject
      }
    })
  })

  function pass() {
    var self = this
    var modal = $('#small-modal')
    modal.modal()
    utils.setModal(modal, {
      header: '通过确认',
      body: '是否确认通过审核',
      confirm: function () {
        modal.modal('hide')
        $(self).parent().html('已通过')
      }
    })
  }

  function reject() {
    var self = this
    var modal = $('#small-modal')
    modal.modal()
    utils.setModal(modal, {
      header: '驳回确认',
      body: '是否确认驳回审核',
      confirm: function () {
        modal.modal('hide')
        $(self).parent().html('已驳回')
      }
    })
  }
}