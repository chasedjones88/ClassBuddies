<?php
		$groupID = $_POST['groupID'];
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on db connect error.
			exit();
		}
		
		$sql = "UPDATE groups SET
				status = 1
				WHERE groupID = ?"; 
		
		if ($statement = mysqli_prepare($conn, $sql)) {
			$statement->bind_param("i", $groupID);
			if ($statement->execute()) {
				echo "1";						// app requirement - returns '1' on successful entry. 
			} else {
				echo "-10"; 					// app requirement - returns '-10' db unspecified error
			}
		}	else {
			echo "-10"; 
		}
?>
