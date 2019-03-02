<?php
		$userID = $_POST['userID'];

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();									// fails quitely, creating an empty list
		}
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM chatUsers WHERE userID=?")) {
			$statement->bind_param("i", $userID);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			while ($row = $result->fetch_assoc()) {
				echo ($row["chatID"] . "\n"); 
			}
		}	
?>
