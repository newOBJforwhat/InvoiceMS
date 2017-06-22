function input() {
  'use strict'

  var $inputForm = $(
    '  <form class="type-in-form" name="typeInForm">' +
    '    <header class="form-header">' +
    '      <h2>输入发票信息</h2>' +
    '    </header>' +
    '    <div class="form-group">' +
    '      <label for="invoice-id">发票号:</label>' +
    '      <input id="invoice-id" class="form-control" type="text" name="invoiceNumber" required>' +
    '    </div>' +
    '    <div class="form-group">' +
    '      <label for="money">金额:</label>' +
    '      <input id="money" class="form-control" type="text" name="money" required>' +
    '    </div>' +
    '    <div class="form-group dropdown">' +
    '      <label for="supplier-name">供应商名字:</label>' +
    '      <input id="supplier-name" class="form-control" type="text" name="supplierName" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" required autocomplete="off">' +
    '      <ul id="supplier-list" class="dropdown-menu" aria-labelledby="supplier-name"></ul>' +
    '    </div>' +
    '    <div class="form-group">' +
    '      <label for="date">开票日期:</label>' +
    '      &nbsp;&nbsp;&nbsp;' +
    '      <button id="date" class="btn btn-primary" onclick="return false">选择日期</button>' +
    '    </div>' +
    '    <footer class="form-footer">' +
    '      <button class="btn btn-danger submit-button" type="reset">清空</button>' +
    '      &nbsp;&nbsp;' +
    '      <button class="btn btn-primary submit-button" type="submit">提交</button>' +
    '    </footer>' +
    '  </form>')

  $('#form').replaceWith($inputForm)

  $inputForm.get(0).onsubmit = submitForm

  updateSupplierList('')

  $('#supplier-list').click(function (e) {
    var $supplierName = $('#supplier-name')
    $supplierName.val($(e.target).attr('data-sname'))
    updateSupplierList($supplierName.val())
  })

  $('#supplier-name').keyup(utils.debounce(function (e) {
    updateSupplierList(e.target.value)
  }, 350))

  var date

  $('#date').datepicker({
    format: 'yyyy年mm月dd日',
    language: 'zh-CN',
    autoclose: true
  })

  $(document).on('changeDate', function (event) {
    date = event.date
    $('#date').html(event.format())
  })

  function submitForm(e) {
    e.preventDefault()

    if (!date) {
      $('.fail-modal-body').html('请选择开票时间')
      $('#fail-modal').modal()
      return
    }

    $http.post('/invoice/staff/enterInvoice', {
      invoiceNumber: this.invoiceNumber.value,
      money: this.money.value,
      supplierName: this.supplierName.value,
      invoiceDate: date.toJSON()
    })
      .then(function (resp) {
        var $modal = $('#success-modal')

        if (resp.status === 1) {
          utils.setModal($modal, {
            header: '操作成功',
            body: '发票信息已提交，请等待审核'
          })
          $modal.modal()
        } else {
          utils.setModal($modal, {
            header: '操作失败',
            body: resp.info
          })
          $modal.modal()
        }
      })
      .fail(function () {
        $('.fail-modal-body').html('提交失败, 请重试, 多次失败请联系管理员')
        $('#fail-modal').modal()
      })
  }

  function updateSupplierList(numberOrName) {
    var $supplierList = $('#supplier-list')

    $http.post('/supplier/findLike/', {
      numberOrName: numberOrName
    })
      .then(function (resp) {
        $supplierList.html('')
        resp.result.forEach(function (supplier) {
          $supplierList.append($('<li class="supplier-list-item" data-sid="' + supplier.id + '"' + ' data-sname="' + supplier.name + '">供应商编号：' + supplier.number + ' 供应商名称：' + supplier.name + '</li>'))
        })
      })
  }
}