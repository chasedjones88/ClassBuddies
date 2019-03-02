<?php
		$userID = $_POST['userID'];
		$friendID = $_POST['friendID'];
		$friendNote = $_POST['friendNote'];
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on db connection failure .
			exit();
		}
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM friends WHERE (userID = ? AND friendID = ?)")) {
			$statement->bind_param("ii", $userID, $friendID); 
			$statement->execute(); 
			$statement->store_result(); 
			 
			if ($statement->num_rows != 0) {
				echo "-1";						// app requirement - returns '-1' with duplicate relationship.
				exit(); 
			}
		}
		
		if ($statement = mysqli_prepare($conn, "INSERT INTO friends (userID, friendID, friendNote) VALUES (?, ?, ?)")) {
			$statement->bind_param("iis", $userID, $friendID, $friendNote);
			if ($statement->execute()) {
				echo "0";						// app requirement - returns '0' on successful entry. 
			} else {
				echo "-10"; 					// app requirement - returns '-10' db unspecified error
			}
		}	else  { 
			echo "-11"; 
		}
		
?>
