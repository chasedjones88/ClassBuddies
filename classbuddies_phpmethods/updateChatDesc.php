<?php
		$userID = $_POST['userID'];
		$chatID = $_POST['chatID'];
		$chatDesc = $_POST['chatDesc']; 
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on db connect error.
			exit();
		}
		
		$sql = "UPDATE chats SET
				chatDesc = ?
				WHERE chatOwner = ? AND chatID = ?"; 
		
		if ($statement = mysqli_prepare($conn, $sql)) {
			$statement->bind_param("sii", $chatDesc, $userID, $chatID);
			if ($statement->execute()) {
				echo "1";						// app requirement - returns '0' on successful entry. 
			} else {
				echo "-10"; 					// app requirement - returns '-10' db unspecified error
			}
		}	else {
			echo "-11"; 
		}
?>
