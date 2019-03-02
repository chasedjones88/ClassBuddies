<?php
		$userID = $_POST['userID'];
		$groupID = $_POST['groupID']; 
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on db connection failure .
			exit();
		}
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM groupmembers WHERE (userID = ? AND groupID = ?)")) {
			$statement->bind_param("ii", $userID, $groupID); 
			$statement->execute(); 
			$statement->store_result(); 
			
			if ($statement->num_rows != 0) {
				echo "-1";						// app requirement - returns '-1' with duplicate relationship.
				exit(); 
			}
		}

		if ($statement2 = mysqli_prepare($conn, "INSERT INTO groupmembers(`groupID`, `userID`) VALUES (?, ?)")) {
			$statement2->bind_param("ii", $groupID, $userID);
			if ($statement2->execute()) {
				echo "0";						// app requirement - returns '0' on successful entry. 
			} else {
				echo "-10"; 					// app requirement - returns '-10' db unspecified error
			}
		}
?>
