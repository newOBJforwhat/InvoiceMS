function admin() {
  'use strict';

  $('.main').prepend($('<section class="admin-bar"><button class="btn btn-primary" id="add-user-btn">添加用户</button></section>'));
  $('#add-user-btn').click(addUser);

  if (!$('#add-user-modal').length) {
    var $addUserModal = $([
      '<div id="add-user-modal" class="modal fade bs-example-modal-sm in" tabindex="-1">',
      '  <div class="modal-dialog modal-sm" role="document">',
      '    <form class="modal-content" name="newUser">',
      '      <div class="modal-header">',
      '        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>',
      '        </button>',
      '        <h4 class="modal-title">添加用户</h4>',
      '      </div>',
      '      <div class="modal-body">',
      '        <section class="form-group">',
      '          <label for="no">工号:</label>',
      '          <input id="no" class="form-control" type="text" name="no" required>',
      '        </section>',
      '        <section class="form-group">',
      '          <label for="name">姓名:</label>',
      '          <input id="name" class="form-control" type="text" name="name" required>',
      '        </section>',
      '        <section class="form-group">',
      '          <label for="role">角色:</label>',
      '          <select id="role" class="form-control" name="department" required></select>',
      '        </section>',
      '      </div>',
      '      <div class="modal-footer">',
      '        <button class="btn btn-primary" type="submit">添加</button>',
      '        <button class="btn btn-danger" data-dismiss="modal">取消</button>',
      '      </div>',
      '    </form>',
      '  </div>',
      '</div>'].join(''));
    $addUserModal.find('form').get(0).onsubmit = submitAdd;
    $('body').append($addUserModal);
  }

  if (!$('#reset-confirm').length) {
    var $resetConfirmModal = $(
      ['<div id="reset-confirm" class="modal fade bs-example-modal-sm in" tabindex="-1">',
        '  <div class="modal-dialog modal-sm" role="document">',
        '    <div class="modal-content">',
        '      <div class="modal-header">',
        '        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>',
        '        </button>',
        '        <h4 class="modal-title">重置确认</h4>',
        '      </div>',
        '      <div class="modal-body">',
        '        工号为<span id="reset-confirm-no"></span>用户<span id="reset-confirm-name"></span>的密码将会被重置为123456',
        '      </div>',
        '      <div class="modal-footer">',
        '        <button class="btn btn-primary">确认</button>',
        '      </div>',
        '    </div>',
        '  </div>',
        '</div>'].join('')
    );
    $resetConfirmModal.find('.modal-footer > button').click(resetPassword);
    $('body').append($resetConfirmModal)
  }

  $('#table-container').html('<table id="table"></table>');
  var $table = $('#table');
  $table.bootstrapTable({
    search: true,
    columns: [{
      field: 'no',
      title: '工号'
    }, {
      field: 'name',
      title: '姓名'
    }, {
      field: 'role',
      title: '角色'
    }, {
      field: 'operation',
      title: '操作'
    }],
    data: [{
      no: 1,
      name: '王大全',
      role: '录入员',
      operation: '<button class="btn btn-warning reset-password">重置密码</button>'
    }, {
      no: 2,
      name: '李云龙',
      role: '录入员',
      operation: '<button class="btn btn-warning reset-password">重置密码</button>'
    }]
  });

  $table.find('button.reset-password').each(function (index, elm) {
    elm.onclick = resetConfirm.bind(elm)
  });

  function addUser() {
    $('#add-user-modal').modal({
      backdrop: 'static',
      keyboard: false
    });
  }

  /**
   * 获取部门列表
   */
  (function () {
    // 测试代码
    var data = ['普通用户', '部门审核', '财务'];

    data.forEach(function (department) {
      $('#role').append('<option>' + department + '</option>')
    });
  })();

  /**
   * 提交新增用户
   * @param e
   */
  function submitAdd(e) {
    e.preventDefault();

    $.post('/user/add', {
      no: this.no.value,
      name: this.name.value,
      department: this.department.value
    })
      .done(function () {
        location.reload(true);
      })
      .fail(function () {

      });
  }

  function resetConfirm() {
    $('#reset-confirm').modal();
    var no = $(this).parent().parent().find('td').get(0).innerHTML;
    var name = $(this).parent().parent().find('td').get(1).innerHTML;

    $('#reset-confirm-no').text(no);
    $('#reset-confirm-name').text(name);
  }

  function resetPassword() {
    $.post('/user/reset', {
      no: $('#reset-confirm-no').text()
    })
      .done(function () {
        location.href = 'success.html';
      })
      .fail(function () {
        $('#fail-modal').modal();
      });
  }
}

