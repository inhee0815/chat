<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>회원가입 페이지</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<form name="signupform" method="post" action="UserServlet">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tbody align="center">
				<tr height="30" align="center">
					<td class="key"  >이름<span>*</span></td>
					<td><input type="text" name="user_name" /></td>
				</tr>
				<tr height="50" align="center">
					<td class="key">아이디<span>*</span></td>
					<td><input type="text" name="user_id" id="user_id"
						onblur="idSubmit('post','idcheck.jsp');" />
						<div>4자 이상의 영문(소문자)과 숫자만 사용할 수 있습니다.</div>
						<span class="idmsg" id="idLayer"></span></td>
				</tr>
				<tr height="50" align="center">
					<td class="key">비밀번호<span>*</span></td>
					<td><input type="password" name="user_pass">
						<div>8자 이상의 영문과 숫자, 특수문자만 사용할 수 있습니다.</div></td>
				</tr>
				<tr height="30" align="center">
					<td class="key">비밀번호 확인<span>*</span></td>
					<td><input type="password" name="user_pass2" /></td>
				</tr>
			</tbody>
		</table>
		<input type="button" value="회원가입" onclick="writeCheck();" />
	</form>

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
		
		if(!form.user_id.value.match(/^[a-zA-Z][a-zA-Z0-9]{3,11}$/)) { //첫글자 영문, 영문소문자, 숫자 조합 
			//alert("ID 첫글자는 영문으로 시작하며 영문과 숫자조합만 가능합니다.");
			return false;
		}
		return true;
	}
	
	function idCheck(toggle) {
		
		if( !idformCheck() || form.user_id.value.length < 4 || form.user_id.length > 12 || toggle) {
			document.getElementById('idLayer').innerHTML="사용할 수 없는 id입니다.";
			form.user_id.focus();
		} else {
			document.getElementById('idLayer').innerHTML="OK!";
		}
	}

	
	function passformCheck() {
		
		if (form.user_pass.value.length < 8) {
			//alert("비밀번호는 문자, 숫자, 특수문자의 조합으로 최소 8자리로 입력해주세요.");
			return false;
		} 

		if (!form.user_pass.value.match(/([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])/)) {
			//alert("비밀번호는 문자, 숫자, 특수문자의 조합으로 최소 8자리로 입력해주세요.");
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
