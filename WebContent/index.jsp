<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="UTF-8">
  <title>登录</title>
  <link href="css/bootstrap.min.css" rel="stylesheet">
  <link href="css/myStyle/style.css" rel="stylesheet">
</head>
<body class="login-body">
<section class="login-container-bg"></section>
<section class="login-container">
  <form onsubmit="login(event)" method="post" action="/InvoiceMS/user/noNeedLogin/login">
    <header class="login-header">
      <h3>用户登录</h3>
    </header>
    <div class="form-group">
      <label for="username">用户名:</label>
      <input id="username"  name = "username" class="form-control" type="text" required>
    </div>
    <div class="form-group">
      <label for="password">登录密码:</label>
      <input id="password"  name = "password" class="form-control" type="password" required>
    </div>
    <footer class="login-footer">
      <label for="remember-me">记住我</label><input id="remember-me" type="checkbox">
      <button class="btn btn-primary" type="submit">登录</button>
    </footer>
  </form>
</section>

<div id="alert-modal" class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog"
     aria-labelledby="mySmallModalLabel">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span>
        </button>
        <h4 class="modal-title" id="mySmallModalLabel">登录失败</h4>
      </div>
      <div class="modal-body">
        提示登录错误原因
      </div>
    </div>
  </div>
</div>
<script type="text/javascript" src="js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript">

</script>
</body>
</html>