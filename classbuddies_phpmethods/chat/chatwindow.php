<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>ClassBuddies Chat</title>
    <style>
		html			{ height: 100%; padding:0px;margin:0px;}
		body 			{ background-color: #333; overflow:hidden; height:100%; padding:0px;margin:0px; } 
       #container 		{ font-family: 'Arial','Sans'; font-size: 8pt; color: #999999; width:100%; height:100%; overflow-y: scroll; overflow-x: auto; padding: 0px; vertical-align:bottom;}
       #container div 	{ width: 99%; word-wrap: break-word; }
        .name p      	{ font-size: 10pt; font-weight: bold; color: yellow; }
        .name span, .user span      { font-size: 12pt; color: #ddd; font-weight: normal; } 
        .name span span, .user span span	{ font-size: 8pt; color:#888; } 
		.name 			{ padding-left:5px; }
        .user           { padding-right:5px; text-align:right; }
        .user p      	{ font-size: 10pt; font-weight: bold; color: aqua; }
	</style>
</head>
<body>
	
    <div id="container">
<?php
		$user = (int) $_GET['user']; 
		$chat = (int) $_GET['chat']; 
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();	
		}
		
		$sql = "SELECT user.userName, chatMessages.messageTime, chatMessages.senderID, chatMessages.messageText " .
				"FROM chatMessages " . 
				"JOIN user " . 
				"ON chatMessages.senderID=user.userID " . 
				"WHERE (chatMessages.chatID = ?)";
		
		
		if ($statement = mysqli_prepare($conn, $sql) ){
			$statement->bind_param("i", $chat);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			$printed = false; 
			while ($row = $result->fetch_assoc()) {
				$type = ($row["senderID"] == $user) ? "user" : "name"; 
				$msgDate = new DateTime($row["messageTime"]);
				$msgDate->sub(new DateInterval('PT3H')); 
				echo "\t\t<div class=\"" . $type . "\"><p>" . $row["userName"] . "<br /><span>" . $row["messageText"] . "<br /><span>" . $msgDate->format('m-d-Y h:i:sa') . "</span></span></p></div>\n"; 
				$printed = true; 
			}
			
			if (!$printed) {
				echo "\t\t<div style=\"width:100%;text-align:center;\"><h2>This chat is currently empty</h2></div>\n"; 
				
			}
			
		}	
?>
    </div>

	<script type="text/javascript">
		var objDiv = document.getElementById("container");
		objDiv.scrollTop = objDiv.scrollHeight;
	</script>
</body>

</html>

<?php






?>
