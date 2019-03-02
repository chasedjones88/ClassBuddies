<?php
		$userID = $_POST['userID'];
		$friendID = $_POST['friendID'];
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// returns '-9' on db connection failure .
			exit();
		}
		
		if ($statement = mysqli_prepare($conn, "DELETE FROM friends WHERE (`friendID` = ? AND `userID` = ?)")) {
			$statement->bind_param("ii", $friendID, $userID);
			if ($statement->execute()) {
				echo "1";						// app requirement - returns '0' on successful entry. 
			} else {
				echo "-10"; 					// returns '-10' db unspecified error
			}
		}
?>
