function admin() {
  'use strict'

  $('.main').prepend($('<section class="admin-bar"><button class="btn btn-primary" id="add-user-btn">添加用户</button></section>'))
  $('#add-user-btn').click(addUser)

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
      '          <label for="username">用户名:</label>',
      '          <input id="username" class="form-control" type="text" name="username" required>',
      '        </section>',
      '        <section class="form-group">',
      '          <label for="name">姓名:</label>',
      '          <input id="name" class="form-control" type="text" name="name" required>',
      '        </section>',
      '        <section class="form-group">',
      '          <label for="password">密码:</label>',
      '          <input id="password" class="form-control" type="text" name="password" required>',
      '        </section>',
      '        <section class="form-group">',
      '          <label for="type">角色:</label>',
      '          <select id="type" class="form-control" name="type" required></select>',
      '        </section>',
      '      </div>',
      '      <div class="modal-footer">',
      '        <button class="btn btn-primary" type="submit">添加</button>',
      '        <button class="btn btn-danger" data-dismiss="modal">取消</button>',
      '      </div>',
      '    </form>',
      '  </div>',
      '</div>'].join(''))

    $addUserModal.find('form').get(0).onsubmit = submitAdd
    $('body').append($addUserModal)

    $('#username').keyup(utils.debounce(function () {
      var username = $(this).val()
      var self = this

      if (!username) {
        return
      }

      $http.get('/user/noNeedLogin/validateUser/' + username)
        .then(function (resp) {
          if (resp.status === 0) {
            utils.errorValidate($(self), false)
          } else {
            utils.errorValidate($(self), true)
          }
        })
    }, 350))
  }

  $('#table-container').html('<table id="table"></table>')
  var $table = $('#table')

  var tableColumns = [{
    field: 'id',
    title: 'ID'
  }, {
    field: 'username',
    title: '用户名'
  }, {
    field: 'name',
    title: '姓名'
  }, {
    field: 'role',
    title: '角色'
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

  var getUsers = $http.get('/user/super/queryUser')
  var getUserTypes = $http.get('/user/getType')

  $http.all([getUserTypes, getUsers])
    .then(function (resps) {
      var userType = resps[0].result
      var userData = resps[1].result

      for (var i in userType) {
        if (i !== '0') {
          $('#type').append('<option value="' + i + '">' + userType[i] + '</option>')
        }
      }

      var data = userData.map(function (user) {
        return $.extend({}, user, {
          role: userType[user.type],
          operation: user.type === 0 ? '' : '<button class="btn btn-warning reset-password">重置密码</button>'
        })
      })

      $table.bootstrapTable('load', data)

      $table.find('button.reset-password').each(function (index, elm) {
        elm.onclick = resetConfirm.bind(elm)
      })

      $table.bootstrapTable('hideLoading')
    })

  function addUser() {
    $('#add-user-modal').modal({
      backdrop: 'static',
      keyboard: false
    })
  }

  /**
   * 提交新增用户
   * @param e
   */
  function submitAdd(e) {
    e.preventDefault()

    var hasError

    $('#add-user-modal')
      .find('.form-group')
      .each(function (index, elm) {
        hasError = $(elm).hasClass('has-error')
      })

    if (hasError) {
      return
    }

    var data = {
      username: this.username.value,
      name: this.name.value,
      password: this.password.value,
      type: this.type.value
    }

    $http.post('/user/super/applyUser', data)
      .then(function () {
        location.reload(true)
      })
      .fail(function () {

      })
  }

  function resetConfirm() {
    var modal = $('#small-modal')
    modal.modal()
    var id = $(this).parent().parent().find('td').get(0).innerHTML
    var name = $(this).parent().parent().find('td').get(1).innerHTML

    utils.setModal(modal, {
      header: '重置确认',
      body: 'ID为' + id + '的用户' + name + '的密码将会被重置为123456',
      confirm: resetPassword.bind(null, id)
    })
  }

  function resetPassword(no) {
    $.post('/user/reset', {
      no: no
    })
      .done(function () {
        location.href = 'success.html'
      })
      .fail(function () {
        $('#fail-modal').modal()
      })
  }
}

