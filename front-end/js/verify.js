function verify(utils) {
  $('#table-container').html('<table id="table"></table>');
  var $table = $('#table');
  $table.bootstrapTable({
    columns: [{
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
    }, {
      field: 'operation',
      title: '操作'
    }],
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
  });

  $('.verify-operation').each(function (index, elm) {
    $(elm).find('a').each(function (i, a) {
      if (i === 0) {
        a.onclick = pass;
      } else {
        a.onclick = reject
      }
    })
  });

  function pass() {
    $(this).parent().html('已通过');
  }

  function reject() {
    $(this).parent().html('已驳回');
  }
}