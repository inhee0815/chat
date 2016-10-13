<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%
	String userid = request.getParameter("userid");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<title>채팅 화면</title>
<link href="style.css?<?=filemtime('style.css')?>" rel="stylesheet"
	type="text/css">
</head>
<body>

	<div class="userid" id="userid"><%=userid%></div>
	<div class="wrapper">
	<div class="msg_box" style="right: 290px">
		<div style="color: black; text-align: center;" class="msg_head">
			비즈커머스개발팀
		</div>
		<div class="msg_wrap" style="display: block;">
			<div class="msg_body" id="msg_body"></div>
			<div class="msg_etc">
			<input type="button" class="emoji_button" onclick="popupOpen('emoji.html');"/>
			<input type="button" class="pic_button" onclick="popupOpen('picture.html')"/>
			</div>
			<div class="msg_footer">
				<textarea class="msg_input" id="msg_input" onkeyup="resize(this)"></textarea>
				<input type="button" class="send_button" value="전송" onclick="sendMessage();" />
			</div>
		</div>
	</div>
	<div class="room_box" >
		<div style="color: black; text-align: center;" class="room_head">
			대화상대
		</div>
		<div class="room_body" id="room_body"></div>
		<div class="room_footer" id="room_footer">
		<input type="button" class="info_button" onclick="popupOpen('info.html');"/>
		<input type="button" class="logout_button" onclick="checkLogout();"/>
		</div>
	</div>
	</div>

</body>
<script type="text/javascript">
$(function() {
	$.ajax({
		url: '/project5/ChatListServlet',
		async: false,
		type: 'POST',
		dataType: 'json',
		data: {"userid" : $('#userid').text()},
		success: function(res) {
			$.each(res, function(k, v) {
				if (v['userid'] == $('#userid').text()){ 
					var htmlStr = "<div name='user_msg' id= 'msg_yellow' class='msg_yellow' style ='text-align:right;'>"+ v['message'] + "<span style='display:none;''>" + v['num'] + "</span></div><div class='date_yellow'>" + v['send_date'] + "</div>";
					
				} else {
					var htmlStr = "<div class='send_id'>"+v['userid']+ "</div><div name='user_msg' id= 'msg_white' class='msg_white'>"+ v['message'] + "<span style='display:none;''>" + v['num'] + "</span></div><div class='date_white'>" + v['send_date'] + "</div>";
				}
				$('#msg_body').append(htmlStr);
				
			});

			$("div[name='user_msg']").dblclick(function() {
				var selectedIndex =  $(this).children().text();
				var retVal = confirm("선택한 메시지를 삭제하시겠습니까?");
				if (retVal) {
					$(this).closest('div').remove();
					msg_rmv("post","send.jsp",selectedIndex);
				} else {
					return;
				}
				
			});

		}, error : function(res) {
			console.log(res);
		}
	});
	$.ajax({
		url: '/project5/MemberListServlet',
		async: false,
		type: 'POST',
		dataType: 'json',
		data: {},
		success: function(res) {
			$.each(res, function(k, v) {
				var htmlStr = "<div id= '" + v['userid'] + "' class='msg_push'>"+ v['userid'] + "</div>";
				$('#room_body').append(htmlStr);
			});


		}, error : function(res) {
			console.log(res);
		}
	});
});

	var webSocket = new WebSocket("ws://172.21.25.189:8080/project5/ChatServer");
	//var webSocket = new WebSocket("ws://192.168.123.106:8080/project5/ChatServer");
	//var webSocket = new WebSocket("ws://localhost:8080/project5/ChatServer");
	var inputMessage = document.getElementById("msg_input");
	
	webSocket.onerror = function(event) {
		onError(event);
	};
	
	webSocket.onopen = function(event) {
		onOpen(event);
	};
	
	webSocket.onmessage = function(event) {
		var jsonData = JSON.parse(event.data); 
		$("#msg_body").append("<div class='send_id'>"+jsonData.userid+"</div>");
		$("#msg_body").append("<div name='user_msg' id='msg_white' class='msg_white'>"+jsonData.message+"</div>");
		var d = document.getElementById("msg_body");
		d.scrollTop=d.scrollHeight-d.offsetHeight;




		//onMessage(event)
	};
	
	function onOpen(event) {
		var d = document.getElementById("msg_body");
		d.scrollTop=d.scrollHeight-d.offsetHeight;
	}
	
	inputMessage.onkeydown = function(event) {
		if (!event)
			event = window.event;
		if(event.shiftKey==true && event.keyCode==13) {
			inputMessage.focus();
			var enter = '\n';
			inputMessage.selection=document.body.createTextRange;
			inputMessage.selection.text=enter; 
			event.returnValue = false;
		}
		if (event.keyCode == 13) {
			event.preventDefault(); // 줄바꿈 막음
			var str =inputMessage.value;
			var tmp =str.replace(/\\s| /gi, '');
			if(tmp==''){
				inputMessage.focus();
				return;
			} else {
				var div = document.createElement('div');
				div.id='msg_yellow';
				div.className='msg_yellow';
				div.innerHTML = ConvertSystemSourcetoHtml(inputMessage.value);
				document.getElementById('msg_body').appendChild(div);
				var d = document.getElementById("msg_body");
				d.scrollTop=d.scrollHeight-d.offsetHeight;
				webSocket.send(ConvertSystemSourcetoHtml(inputMessage.value));
				inputMessage.value = "";
			}
		}
	};
	function sendMessage() {
		var str =inputMessage.value;
		var tmp =str.replace(/\\s| /gi, '');
		if(tmp==''){
			inputMessage.focus();
			return;
		} else {
			var div = document.createElement('div');
			div.id='msg_yellow';
			div.className='msg_yellow';
			div.innerHTML = ConvertSystemSourcetoHtml(inputMessage.value);
			document.getElementById('msg_body').appendChild(div);
			var d = document.getElementById("msg_body");
			d.scrollTop=d.scrollHeight-d.offsetHeight;
			webSocket.send(ConvertSystemSourcetoHtml(inputMessage.value));
			inputMessage.value = "";
		}
	}

	function onMessage(event) {
		var div = document.createElement('div');
		var jsonData = JSON.parse(event.data);
		if(jsonData.message != null) {
			div.id='msg_white';
			div.className='msg_white';
			div.innerHTML = jsonData.message;
			document.getElementById('msg_body').appendChild(div);
			var d = document.getElementById("msg_body");
			d.scrollTop=d.scrollHeight-d.offsetHeight;
		}
		if(jsonData.come != null) {
			div.id=jsonData.come;
			div.className='msg_push';
			div.innerHTML = jsonData.come;
			document.getElementById('room_body').appendChild(div);
		}
		if(jsonData.out != null) {
			var parentNode=document.getElementById('room_body');
			var delNode=parentNode.removeChild(document.getElementById(jsonData.out));
		}
		
	}
	function onError(event) {
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
		var form = document.createElement("form");
		form.setAttribute("method", method);
		form.setAttribute("action", path);
		var hiddenNum = document.createElement("input");
		hiddenNum.setAttribute("type", "hidden");
		hiddenNum.setAttribute("name", "num");
		hiddenNum.setAttribute("value", num);
		var hiddenId = document.createElement("input");
		hiddenId.setAttribute("type", "hidden");
		hiddenId.setAttribute("name", "userid");
		hiddenId.setAttribute("value", "<%=userid%>");
		var iframe = document.createElement("iframe");
		iframe.setAttribute("id", "hidden");
		iframe.setAttribute("name", "hiddenifr");
		iframe.setAttribute("scr", path);
		iframe.setAttribute("style", "display:none;");
		form.appendChild(hiddenNum);
		form.appendChild(hiddenId);
		document.body.appendChild(form);
		document.body.appendChild(iframe);
		form.target = "hiddenifr";
		form.submit();
	}
	function upload_img(addr, tag) {
		var div = document.createElement('div');
		div.setAttribute("id", "msg_yellow");
		div.setAttribute("class", "msg_yellow");
		var img = document.createElement("img");
		img.setAttribute("src", addr);
		img.setAttribute("height", "120");
		img.setAttribute("width", "120");
		div.appendChild(img);
		document.getElementById('msg_body').appendChild(div);
		var d = document.getElementById("msg_body");
		d.scrollTop = d.scrollHeight - d.offsetHeight;
		webSocket.send(tag);
	}


	function popupOpen(popUrl) {
		var popOption = "width=800, height=800, resizable=no, scrollbars=no, status=no;";
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
	function checkLogout(){
			var retVal = confirm("정말 로그아웃하시겠습니까?");
			if (retVal == true) {
				webSocket.close();
				location.href="login.html";
			} else {
				return;
			}
	}
</script>
</html>