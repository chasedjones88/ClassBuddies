<?php
		$search = "%" . $_POST['search'] . "%";

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();									// fails quitely, creating an empty list
		}
		
		$sql = "SELECT userID FROM user WHERE  ( LENGTH(userPWHash) > 1 ) AND ( userName LIKE ? OR userClasses LIKE ? OR userMajor LIKE ? )";
		
		if ($statement = mysqli_prepare($conn, $sql)) {
			$statement->bind_param("sss", $search, $search, $search);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			while ($row = $result->fetch_assoc()) {
				echo ($row["userID"] . "\n"); 
			}
		}	
?>
