<?php
		$userID = $_POST['userID'];
		$friendID = $_POST['friendID'];
		$friendNote = $_POST['friendNote']; 

		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// returns '-9' on db connect error.
			exit();
		}
		
		$sql = "UPDATE friends SET
				friendNote = ?
				WHERE ( userID = ? AND friendID = ?)";
		
		if ($statement = mysqli_prepare($conn, $sql)) {
			$statement->bind_param("sii", $friendNote, $userID, $friendID);
			if ($statement->execute()) {
				echo "1";						// app requirement - returns '0' on successful entry. 
			} else {
				echo "-10"; 					// returns '-10' db unspecified error
			}
		}	else {
			echo "-11"; 
		}
?>
