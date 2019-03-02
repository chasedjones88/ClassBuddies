<?php
		$ownerUserID = $_POST['ownerUserID'];
		$friendID = $_POST['friendID'];
		$chatDesc = $_POST['chatDesc']; 
		$url = $_POST['url']; 
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on db connect error.
			exit();
		}

		$statement = mysqli_prepare($conn, "INSERT INTO chats (chatOwner, chatFriendID, chatDesc, chatWinURL) VALUES (?, ?, ?, ?)"); 
		$statement->bind_param("iiss", $ownerUserID, $friendID, $chatDesc, $url);
		$statement->execute(); 
		echo mysqli_insert_id($conn);
?>
