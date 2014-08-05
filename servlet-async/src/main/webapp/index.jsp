<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Chat</title>
		<script src="/jquery-1.7.1.js" type="text/javascript"></script>
		<script type="text/javascript">
		var chatapp = {
			append: function(msg) {
				$("#chatmessage").append("<div>"+msg.message+"</div>");
			}
		};
		
		$(function() {
			$("#sendBtn").click(function() {
				var msg = $("#message").val();
				$.ajax({
					type: "POST",
					url: '/sendMessage',
					data: {message: msg},
					success: function(data) {}
				});
				$("#message").val("");
			});
			document.getElementById("comet-frame").src = "/enter";
		});
		</script>
	</head>
	<body>
		<div id="chatmessage"></div>
		<input type="text" name="message" id="message" />
		<input type="button" name="sendBtn" id="sendBtn" value="보내기" />
		<iframe id="comet-frame" style="display: none;"></iframe>
	</body>
</html>
