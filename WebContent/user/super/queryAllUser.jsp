<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>用户管理</title>
  <link href="../../css/bootstrap.min.css" rel="stylesheet">
  <link href="../../css/bootstrap-table.min.css" rel="stylesheet">
  <link href="../../css/myStyle/style.css" rel="stylesheet">
</head>
<body>
<header class="global-header">
  <h1>发票管理系统</h1>
</header>
<section class="admin-bar">
  <button class="btn btn-primary" onclick="addUser()">添加用户</button>
</section>
<main>
  <table id="table"></table>
</main>

<div id="add-user-modal" class="modal fade bs-example-modal-sm in" tabindex="-1">
  <div class="modal-dialog modal-sm" role="document">
    <form class="modal-content" name="newUser" onsubmit="submitAdd(event, this)">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>
        </button>
        <h4 class="modal-title">添加用户</h4>
      </div>
      <div class="modal-body">
        <section class="form-group">
          <label for="no">工号:</label>
          <input id="no" class="form-control" type="text" name="no" required>
        </section>

        <section class="form-group">
          <label for="name">姓名:</label>
          <input id="name" class="form-control" type="text" name="name" required>
        </section>

        <section class="form-group">
          <label for="role">职务:</label>
          <select id="role" class="form-control" name="role" required>
            <option value="common" selected>普通用户</option>
            <option value="leader">部门管理</option>
            <option value="financial">财务</option>
            <option value="manager">管理员</option>
          </select>
        </section>

        <section class="form-group">
          <label for="department">部门:</label>
          <select id="department" class="form-control" name="department" required></select>
        </section>
      </div>
      <div class="modal-footer">
        <button class="btn btn-primary" type="submit">添加</button>
        <button class="btn btn-danger" data-dismiss="modal">取消</button>
      </div>
    </form>
  </div>
</div>

<div id="reset-confirm" class="modal fade bs-example-modal-sm in" tabindex="-1">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>
        </button>
        <h4 class="modal-title">重置确认</h4>
      </div>
      <div class="modal-body">
        工号为<span id="reset-confirm-no"></span>用户<span id="reset-confirm-name"></span>的密码将会被重置为123456
      </div>
      <div class="modal-footer">
        <button class="btn btn-primary" onclick="resetPassword()">确认</button>
      </div>
    </div>
  </div>
</div>

<div id="fail-modal" class="modal fade bs-example-modal-sm in" tabindex="-1">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content">

      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>
        </button>
        <h4 class="modal-title">提交失败</h4>
      </div>
      <div class="modal-body">
        提交失败, 请重试, 多次失败请联系管理员
      </div>
    </div>
  </div>
</div>
<script type="text/javascript" src="../../js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="../../js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="../../js/bootstrap/bootstrap-table.min.js"></script>
<script type="text/javascript" src="../../js/bootstrap/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript">
  $('#table').bootstrapTable({
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
      role: '财务',
      operation: '<button class="btn btn-warning" onclick="resetConfirm(this)">重置密码</button>'
    }, {
      no: 2,
      name: '李云龙',
      role: '普通用户',
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
        location.href = '../../success.html';
      })
      .fail(function () {
        $('#fail-modal').modal();
      });
  }

</script>
</body>
</html>