<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%
	String ipAddress = request.getRemoteAddr();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<title>Insert title here</title>
<link href="style.css?<?=filemtime('style.css')?>" rel="stylesheet"
	type="text/css">
</head>
<body>
	<form id="frm" method="post" enctype="multipart/form-data"
		action="img.jsp">
		<input name="Filedata" class="upload" id="uploadInputBox" type="file" />
		<input type="submit" value="전송" onclick="imgUp();" />
		<iframe id="ifr" name="ifr" style="display: none;"></iframe>
	</form>
	<mark>username: <%=request.getAttribute("username")%></mark>
	<br>
	<div class="msg_box" style="right: 290px">
		<div style="color: black; text-align: center;" class="msg_head">
			비즈커머스개발팀
			<div class="close" onclick="button_close();">X</div>
			<div class="emoji" onclick="popupOpen();">★</div>
		</div>
		<div class="msg_wrap" style="display: block;">
			<div class="msg_body" id="msg_body"></div>
			</br>
			<div class="msg_footer">
				<textarea class="msg_input" id="msg_input" onkeyup="resize(this)"></textarea>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript">
$(function() {

	console.log('js start');
	$.ajax({
		url: '/project5/ChatListServlet',
		async: false,
		type: 'POST',
		dataType: 'json',
		data: {},
		success: function(res) {
			
			$.each(res, function(k, v) {
				console.log(k);
				console.log(v['addr']);
				console.log(v['message']);
				console.log(v['num']);
				if (v['addr'] == "<%=ipAddress%>"){ 
					var htmlStr = "<div id= 'msg_b' class='msg_b'>"+ v['message'] + "</div>";
				} else {
					var htmlStr = "<div id= 'msg_a' class='msg_a'>"+ v['message'] + "</div>";
				}
				$('#msg_body').append(htmlStr);
				div.ondblclick = function() {
				var retVal = confirm("선택한 메시지를 삭제하시겠습니까?");
				if (retVal == true) {
				this.parentElement.removeChild(this);
				msg_rmv("post","send.jsp", <%=num%>);
				} else {
					return;
					}
					};
			});
		}, error : function(res) {
			console.log(res);
		}
	});
});

	var webSocket = new WebSocket("ws://172.21.25.189:8080/project5/ChatServer");
	var inputMessage = document.getElementById("msg_input");
	var sendE = document.getElementById("send");

	webSocket.onerror = function(event) {
		onerror(event);
	};

	webSocket.onopen = function(event) {
		onOpen(event)
	};

	webSocket.onclose = function(event) {F
		onClose(event)
	};

	webSocket.onmessage = function(event) {
		onMessage(event)
	};

	inputMessage.onkeydown = function(event) {
		if (!event)
		event = window.event;

		if (event.keyCode == 13) {
			event.preventDefault(); // 줄바꿈 막음
			var str =inputMessage.value;
			var tmp =str.replace(/\\s| /gi, '');
			if(tmp==''){
				inputMessage.focus();
				return;
			} else {
				var div = document.createElement('div');
				div.id='msg_b';
				div.className='msg_b';
				div.innerHTML = ConvertSystemSourcetoHtml(inputMessage.value);
				document.getElementById('msg_body').appendChild(div);
				var d = document.getElementById("msg_body");
				d.scrollTop=d.scrollHeight-d.offsetHeight;
				webSocket.send(ConvertSystemSourcetoHtml(inputMessage.value));
				inputMessage.value = "";
			}
		}
	};

	function onMessage(event) {
		var div = document.createElement('div');
		var jsonData = JSON.parse(event.data);
		div.id='msg_a';
		div.className='msg_a';
		if(jsonData.message != null) {
			div.innerHTML = jsonData.message;
			document.getElementById('msg_body').appendChild(div);
			var d = document.getElementById("msg_body");
			d.scrollTop=d.scrollHeight-d.offsetHeight;

		}
	}

	function onOpen(event) {
		var d = document.getElementById("msg_body");
		d.scrollTop=d.scrollHeight-d.offsetHeight;
		var div = document.createElement('div');
		var jsonData = JSON.parse(event.data);
		div.id='msg_push';
		div.className='msg_push';
		if(jsonData.message != null) {
			div.innerHTML = jsonData.message;
			document.getElementById('msg_body').appendChild(div);
			var d = document.getElementById("msg_body");
			d.scrollTop=d.scrollHeight-d.offsetHeight;
		}
	}

	function onClose(event) {
		var div = document.createElement('div');
		var jsonData = JSON.parse(event.data);
		div.id='msg_push';
		div.className='msg_push';
		if(jsonData.message != null) {
			div.innerHTML = jsonData.message;
			document.getElementById('msg_body').appendChild(div);
			var d = document.getElementById("msg_body");
			d.scrollTop=d.scrollHeight-d.offsetHeight;
		}
	}
		
	function onerror(event) {
		alert(event.data);
	}

	function resize(obj) {
		obj.style.height = "1px";
		obj.style.height = (20+obj.scrollHeight)+"px";
	}

	function button_close(str) {
		alert("Coming Soon..");
	}
	
	function msg_rmv(method,path,num) {
		alert(num);
		var form = document.createElement("form");
		form.setAttribute("method", method);
		form.setAttribute("action", path);
		var hiddenNum = document.createElement("input");
		hiddenNum.setAttribute("type", "hidden");
		hiddenNum.setAttribute("name", "num");
		hiddenNum.setAttribute("value", num);
		var hiddenIp = document.createElement("input");
		hiddenIp.setAttribute("type", "hidden");
		hiddenIp.setAttribute("name", "ip");
		hiddenIp.setAttribute("value", "<%=ipAddress%>");
		var iframe = document.createElement("iframe");
		iframe.setAttribute("id", "hidden");
		iframe.setAttribute("name", "hiddenifr");
		iframe.setAttribute("scr", path);
		iframe.setAttribute("style", "display:none;");
		form.appendChild(hiddenNum);
		form.appendChild(hiddenIp);
		document.body.appendChild(form);
		document.body.appendChild(iframe);
		form.target = "hiddenifr";
		form.submit();
	}

	function imgUp() {
		document.getElementById("frm").target = "ifr";
		return false;
	}

	function upload_img(addr, tag) {
		var div = document.createElement('div');
		div.setAttribute("id", "msg_b");
		div.setAttribute("class", "msg_b");
		var img = document.createElement("img");
		img.setAttribute("src", addr);
		img.setAttribute("height", "100");
		img.setAttribute("width", "100");
		div.appendChild(img);
		document.getElementById('msg_body').appendChild(div);
		var d = document.getElementById("msg_body");
		d.scrollTop = d.scrollHeight - d.offsetHeight;
		webSocket.send(tag);
	}

	function popupOpen() {
		var popUrl = "test.html";
		var popOption = "width=370, height=360, resizable=no, scrollbars=no, status=no;";
		window.open(popUrl, "", popOption);
	}

	function ConvertSystemSourcetoHtml(str) {
		str = str.replace(/</gi, "&lt;");
		str = str.replace(/>/gi, "&gt;");
		str = str.replace(/\"/gi, "&quot;");
		str = str.replace(/\'/gi, "&#39;");
		str = str.replace(/\n/g, "<br/>");
		return str;
	}
</script>
</html>