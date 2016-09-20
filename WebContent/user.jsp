<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>회원가입 페이지</title>
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet"
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<div class="container">
		<h2>회원 가입</h2>
		<form role="form" name="signupform" method="post" action="UserServlet">
			<div class="form-group">
				<label for="Name">이름</label> <input type="text" class="form-control"
					id="Name" name="user_name">
			</div>
			<div class="form-group">
				<label for="Id">아이디</label> <input type="text" class="form-control"
					id="Id" name="user_id" onblur="idSubmit('post','idcheck.jsp');">
				<div>4자 이상의 영문(소문자)과 숫자만 사용할 수 있습니다.</div>
				<span class="idmsg" id="idLayer"></span>
			</div>
			<div class="form-group">
				<label for="Password1">비밀번호</label> <input type="password"
					class="form-control" id="Password1" name="user_pass">
				<div>8자 이상의 영문과 숫자, 특수문자만 사용할 수 있습니다.</div>
			</div>
			<div class="form-group">
				<label for="Password2">비밀번호 확인</label> <input type="password"
					class="form-control" id="Password2" name="user_pass2">
			</div>
			<div class="form-group">
				<button class="btn btn-primary" onclick="writeCheck();">회원가입</button>
				<button class="btn btn-default" onClick="history.back(); return false;">취소</button>
			</div>
		</form>
	</div>


</body>
<script>
 var form = document.signupform;
 function writeCheck() {
  
  if (!form.user_name.value) {
   alert("이름을 입력하세요.");
   form.user_name.focus();
   return;
  }
  if (!form.user_id.value) {
   alert("아이디를 입력하세요.");
   form.user_id.focus();
   return;
  }
  if (!form.user_pass.value) {
   alert("비밀번호를 입력하세요.");
   form.user_pass.focus();
   return;
  }
  if (!form.user_pass2.value) {
   alert("비밀번호를 다시 입력하세요.");
   form.user_pass2.focus();
   return;
  }
  if (form.user_pass.value != form.user_pass2.value) {
   alert("비밀번호가 다릅니다.");
   form.user_pass2.focus();
   return;
  }
  if(!passformCheck()) 
   return;
  if(!idformCheck())
   return;
  
  form.submit();
 }
 function idformCheck() {
  
  if(!form.user_id.value.match(/^[a-z][a-z\d]{3,11}$/)) { //첫글자 영문, 영문소문자, 숫자 조합 
   //alert("4자 이상의 영문(소문자)과 숫자만 사용할 수 있습니다.");
   return false;
  }
  return true;
 }
 
 function idCheck(toggle) {
  
  if( !idformCheck() || form.user_id.value.length < 4 || form.user_id.length > 12 || toggle) {
   document.getElementById('idLayer').innerHTML="<font color=#ff2222>사용할 수 없는 id입니다.</font>" ;
   form.user_id.focus();
  } else {
   document.getElementById('idLayer').innerHTML="<font color=#0000ff>OK!</font>";
  }
 }

 
 function passformCheck() {

  var chk_num = form.user_pass.value.search(/[0-9]/g); 
  var chk_eng = form.user_pass.value.search(/[a-z]/ig); 
  var strSpecial = form.user_pass.value.search(/[!@#$%^&*()?_~]/g);
  
  if (form.user_pass.value.length < 8) {
   alert("비밀번호는 최소 8자리로 입력해주세요.");
   return false;
  } 

  if (chk_num < 0 || chk_eng < 0 || strSpecial < 0) {
   alert("비밀번호는 문자, 숫자, 특수문자의 조합으로 입력해주세요.");
   return false;
  } 
  return true;
 }

 function idSubmit(method, path) {
  var id = document.signupform.user_id.value;
  var form = document.createElement("form");
  form.setAttribute("method", method);
  form.setAttribute("action", path);
  var hiddenId = document.createElement("input");
  hiddenId.setAttribute("type", "hidden");
  hiddenId.setAttribute("name", "hidden_id");
  hiddenId.setAttribute("value", id);
  var iframe = document.createElement("iframe");
  iframe.setAttribute("id", "hidden");
  iframe.setAttribute("name", "hiddenifr");
  iframe.setAttribute("scr", path);
  iframe.setAttribute("style", "display:none;");
  form.appendChild(hiddenId);
  document.body.appendChild(form);
  document.body.appendChild(iframe);
  form.target = "hiddenifr";
  form.submit();
 }
 
 

</script>
</html>

