<?php
		$userID = $_POST['userID'];
		$chatID = $_POST['chatID']; 
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();								// dies quitely
		}
		
		if ($statement = mysqli_prepare($conn, "DELETE FROM chatUsers WHERE (`chatID` = ? AND `userID` = ?)")) {
			$statement->bind_param("ii", $chatID, $userID);
			if ($statement->execute()) {
				echo "1";						// app requirement - returns '1' on successful execution. 
			}
		}
?>
