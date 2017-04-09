function admin() {
  'use strict';

  $('#table').bootstrapTable({
    columns: [{
      field: 'no',
      title: '工号'
    }, {
      field: 'name',
      title: '姓名'
    }, {
      field: 'department',
      title: '部门'
    }, {
      field: 'operation',
      title: '操作'
    }],
    data: [{
      no: 1,
      name: '王大全',
      department: '研发部',
      operation: '<button class="btn btn-warning" onclick="resetConfirm(this)">重置密码</button>'
    }, {
      no: 2,
      name: '李云龙',
      department: '研发部',
      operation: '<button class="btn btn-warning" onclick="resetConfirm(this)">重置密码</button>'
    }]
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
    $.get('/departments')
      .done(function (data) {
        data.forEach(function (department) {
          $('#department').append('<option>' + department + '</option>')
        });
      });

    // 测试代码
    var data = ['研发部', '测试部', '人力资源部', '保安部'];

    data.forEach(function (department) {
      $('#department').append('<option>' + department + '</option>')
    });
  })();

  /**
   * 提交新增用户
   * @param e
   * @param form
   */
  function submitAdd(e, form) {
    e.preventDefault();

    $.post('/user/add', {
      no: form.no.value,
      name: form.name.value,
      title: form.role.value,
      department: form.department.value
    })
      .done(function () {
        location.reload(true);
      })
      .fail(function () {

      });
  }

  function resetConfirm(ele) {
    $('#reset-confirm').modal();
    var no = $(ele).parent().parent().find('td').get(0).innerHTML;
    var name = $(ele).parent().parent().find('td').get(1).innerHTML;

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

  return {
    addUser: addUser,
    submitAdd: submitAdd,
    resetConfirm: resetConfirm,
    resetPassword: resetPassword
  }
}

