<?php
		$userID = $_POST['userID'];
		$chatID = $_POST['chatID']; 
		$message = $_POST['chatMessage']; 
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on db connect error.
			exit();
		}

		$statement = mysqli_prepare($conn, "INSERT INTO chatMessages (chatID, senderID, messageText) VALUES (?, ?, ?)"); 
		$statement->bind_param("iss", $chatID, $userID, $message);
		$statement->execute(); 
		echo '1';
?>
