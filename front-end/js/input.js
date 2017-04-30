function input() {
  'use strict'

  var $inputForm = $(
    '  <form class="type-in-form" name="typeInForm">'+
    '    <header class="form-header">'+
    '      <h2>输入发票信息</h2>'+
    '    </header>'+
    '    <div class="form-group">'+
    '      <label for="invoice-id">发票号:</label>'+
    '      <input id="invoice-id" class="form-control" type="text" name="invoiceId" required>'+
    '    </div>'+
    '    <div class="form-group">'+
    '      <label for="money">金额:</label>'+
    '      <input id="money" class="form-control" type="text" name="money" required>'+
    '    </div>'+
    '    <div class="form-group">'+
    '      <label for="supplier-name">供应商名字:</label>'+
    '      <input id="supplier-name" class="form-control" type="text" name="supplierName" required>'+
    '    </div>'+
    '    <div class="form-group">'+
    '      <label for="date">开票日期:</label>'+
    '      &nbsp;&nbsp;&nbsp;'+
    '      <button id="date" class="btn btn-primary" onclick="return false">选择日期</button>'+
    '    </div>'+
    '    <footer class="form-footer">'+
    '      <button class="btn btn-danger submit-button" type="reset">清空</button>'+
    '      &nbsp;&nbsp;'+
    '      <button class="btn btn-primary submit-button" type="submit">提交</button>'+
    '    </footer>'+
    '  </form>');

  $('#form').replaceWith($inputForm);

  $inputForm.get(0).onsubmit = submitForm;

  var date;

  $('#date').datepicker({
    format: 'yyyy年mm月dd日',
    language: 'zh-CN'
  });

  $(document).on('changeDate', function (event) {
    date = event.date;
    $('#date').html(event.format());
  });

  function submitForm(e) {
    e.preventDefault();

    if (!date) {
      $('.fail-modal-body').html('请选择开票时间');
      $('#fail-modal').modal();
      return;
    }

    $.post('/invoice/new', {
      invoiceId: this.invoiceId.value,
      money: this.money.value,
      supplierName: this.supplierName.value,
      date: date.toJSON()
    })
      .done(function () {
        location.href = 'success.html';
      })
      .fail(function () {
        $('.fail-modal-body').html('提交失败, 请重试, 多次失败请联系管理员');
        $('#fail-modal').modal();
      })
  }
}