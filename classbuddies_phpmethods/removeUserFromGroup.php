<?php
		$userID = $_POST['userID'];
		$groupID = $_POST['groupID']; 
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();								// dies quitely
		}
		
		if ($statement = mysqli_prepare($conn, "DELETE FROM groupmembers WHERE (`groupID` = ? AND `userID` = ?)")) {
			$statement->bind_param("ii", $groupID, $userID);
			if ($statement->execute()) {
				echo "1";						// app requirement - returns '1' on successful delete. 
			}
		}
?>
