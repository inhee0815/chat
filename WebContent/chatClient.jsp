<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="msg_box" style="right: 290px">
		<div class="msg_head">
			chat box
			<div class="close">x</div>
		</div>
		<div class="msg_wrap" style="display: block;">
			<div id="msg_body">
				
				<div id="msg_push"></div>
			</div>
			<div class="msg_footer">
				<textarea class="msg_input" id="msg_input" onkeyup="resize(this)"></textarea>
			</div>
		</div>
	</div>

	<!-- <fieldset>
  <textarea id="messageWindow" rows="10" cols="50" readonly="true"></textarea>
  <br /> <input id="inputMessage" type="text" /> <input type="submit"
   value="send" onclick="javascript:send()" />
 
 </fieldset> -->

</body>
<script type="text/javascript">
	var textarea = document.getElementById("msg_push");
	var webSocket = new WebSocket('ws://172.21.25.189:8080/project5/chatServer');
	var inputMessage = document.getElementById("msg_input");
	var sendE = document.getElementById("send");
	// 메세지 입력후 엔터 누름

	webSocket.onerror = function(event) {
		onError(event)
	};
	webSocket.onopen = function(event) {
		onOpen(event)
	};
	webSocket.onmessage = function(event) {
		onMessage(event)
	};
	inputMessage.onkeydown = function(event) {
		if (!event)
			event = window.event;
		if (event.keyCode == 13) {
			if (inputMessage.value != "") {
				var div = document.createElement('div');
				div.className='msg_b'
				div.innerHTML = "나 : " + inputMessage.value;
				document.getElementById('msg_body').appendChild(div);
				webSocket.send(inputMessage.value);
				inputMessage.value = "";
			}
		}
	};
	
	function onMessage(event) {
		var div = document.createElement('div');
		div.className='msg_a';
		div.innerHTML = "상대 : " + event.data;
		document.getElementById('msg_body').appendChild(div);
	}
	function onOpen(event) {
		textarea.innerHTML = "연결 성공";
	}
	function onError(event) {
		alert(event.data);
	}
	 function resize(obj) {
	      obj.style.height = "1px";
	      obj.style.height = (20+obj.scrollHeight)+"px";
	    }

	/* function add_item(){
	       // pre_set 에 있는 내용을 읽어와서 처리..
	       var div = document.createElement('div');
	       div.innerHTML = document.getElementById('pre_set').innerHTML;
	       document.getElementById('field').appendChild(div);
	   } */
</script>

</html>


