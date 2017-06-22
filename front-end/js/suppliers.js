function suppliers() {
  'use strict'

  $('.main').prepend($('<section class="admin-bar"><button class="btn btn-primary" id="add-supplier-btn">添加供应商</button></section>'))
  $('#add-supplier-btn').click(addSupplier)

  if (!$('#add-supplier-modal').length) {
    var $addSupplierModal = $([
      '<div id="add-supplier-modal" class="modal fade bs-example-modal-sm in" tabindex="-1">',
      '  <div class="modal-dialog modal-sm" role="document">',
      '    <form class="modal-content" name="newSupplier">',
      '      <div class="modal-header">',
      '        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>',
      '        </button>',
      '        <h4 class="modal-title">添加供应商</h4>',
      '      </div>',
      '      <div class="modal-body">',
      '        <section class="form-group">',
      '          <label for="supplier-number">供应商编号:</label>',
      '          <input id="supplier-number" class="form-control" type="text" name="supplierNumber" required>',
      '        </section>',
      '        <section class="form-group">',
      '          <label for="supplier-name">供应商名称:</label>',
      '          <input id="supplier-name" class="form-control" type="text" name="supplierName" required>',
      '        </section>',
      '      </div>',
      '      <div class="modal-footer">',
      '        <button class="btn btn-primary" type="submit">添加</button>',
      '        <button class="btn btn-danger" data-dismiss="modal">取消</button>',
      '      </div>',
      '    </form>',
      '  </div>',
      '</div>'].join(''))

    $addSupplierModal.find('form').get(0).onsubmit = submitAdd
    $('body').append($addSupplierModal)
  }

  if (!$('#update-supplier-modal').length) {
    var $updateSupplierModal = $([
      '<div id="update-supplier-modal" class="modal fade bs-example-modal-sm in" tabindex="-1">',
      '  <div class="modal-dialog modal-sm" role="document">',
      '    <form class="modal-content" name="newSupplier">',
      '      <div class="modal-header">',
      '        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>',
      '        </button>',
      '        <h4 class="modal-title">更新供应商信息</h4>',
      '      </div>',
      '      <div class="modal-body">',
      '        <section class="form-group">',
      '          <label for="supplier-number">供应商编号:</label>',
      '          <input id="supplier-number" class="form-control" type="text" name="supplierNumber" readonly>',
      '        </section>',
      '        <section class="form-group">',
      '          <label for="supplier-name">供应商名称:</label>',
      '          <input id="supplier-name" class="form-control" type="text" name="supplierName" required>',
      '        </section>',
      '      </div>',
      '      <div class="modal-footer">',
      '        <button class="btn btn-primary" type="submit">确认</button>',
      '        <button class="btn btn-danger" data-dismiss="modal">取消</button>',
      '      </div>',
      '    </form>',
      '  </div>',
      '</div>'].join(''))

    $updateSupplierModal.find('form').get(0).onsubmit = submitUpdate
    $('body').append($updateSupplierModal)
  }

  $('#table-container').html('<table id="table"></table>')

  var $table = $('#table')
  var supplierList = []

  var tableColumns = [{
    field: 'number',
    title: '供应商编号'
  }, {
    field: 'name',
    title: '供应商名称'
  }, {
    field: 'operation',
    title: '操作'
  }]

  $table.bootstrapTable({
    search: true,
    pagination: true,
    pageSize: 10,
    columns: tableColumns
  })

  $table.bootstrapTable('showLoading')

  $http.get('/supplier/super/findList/1')
    .then(function (resp) {
      resp.result.data.forEach(function (supplier) {
        $.extend(supplier, {
          operation: ['<button class="btn btn-warning update-info">更新信息</button>',
            '&nbsp;',
            '<button class="btn btn-danger delete-supplier">删除供应商</button>']
            .join('')
        })
      })

      supplierList = resp.result.data
      $table.bootstrapTable('load', supplierList)

      $table.find('.update-info').each(function (index, elm) {
        $(elm).click(function () {
          var $updateSupplierModal = $('#update-supplier-modal')
          var form = $updateSupplierModal.find('.modal-content').get(0)

          $(form).attr('data-sid', supplierList[index].id)
          form.supplierNumber.value = supplierList[index].number
          form.supplierName.value = supplierList[index].name

          $updateSupplierModal.modal({
            backdrop: 'static',
            keyboard: false
          })
        })
      })

      $table.find('.delete-supplier').each(function (index, elm) {
        $(elm).click(function () {
          var modal = $('#small-modal')
          modal.modal()

          var number = supplierList[index].number
          var name = supplierList[index].name

          utils.setModal(modal, {
            header: '删除确认',
            body: '编号为' + number + '的供应商' + name + '的将会被删除，不可恢复',
            confirm: function () {
              $http.post('/supplier/super/deleteSupplier', {
                sid: supplierList[index].id
              })
                .then(function (resp) {
                  if (resp.status === 1) {
                    toastr.success('供应商' + name + '已成功删除')
                    setTimeout(function () {
                      location.reload()
                    }, 2000)
                  } else {
                    toastr.error(resp.info || '未知异常')
                  }
                })
            }
          })
        })
      })

      $table.bootstrapTable('hideLoading')
    })

  function addSupplier() {
    $('#add-supplier-modal').modal({
      backdrop: 'static',
      keyboard: false
    })
  }

  function submitAdd(e) {
    e.preventDefault()

    var form = e.target

    $http.post('/supplier/super/addSupplier', {
      number: form.supplierNumber.value,
      name: form.supplierName.value
    })
      .then(function (resp) {
        if (resp.status === 1) {
          toastr.success('成功添加供应商')
          setTimeout(function () {
            location.reload()
          }, 2000)
        } else {
          toastr.error(resp.info || '未知异常')
        }
      })
  }

  function submitUpdate(e) {
    e.preventDefault()

    var form = e.target

    $http.post('/supplier/super/updateInfo', {
      sid: $(form).attr('data-sid'),
      name: form.supplierName.value
    })
      .then(function (resp) {
        if (resp.status === 1) {
          toastr.success('成功更新供应商')
          setTimeout(function () {
            location.reload()
          }, 2000)
        } else {
          toastr.error(resp.info || '未知异常')
        }
      })
  }
}