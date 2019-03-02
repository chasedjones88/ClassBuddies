<?php
		$groupID = $_POST['groupID'];

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();									// fails quitely, creating an empty list
		}
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM groupmembers WHERE groupID=?")) {
			$statement->bind_param("i", $groupID);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			while ($row = $result->fetch_assoc()) {
				echo ($row["userID"] . "\n"); 
			}
		}	
?>
