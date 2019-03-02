<?php
		$search = "%" . $_POST['search'] . "%";

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();									// fails quitely, creating an empty list
		}
		
		$sql = "SELECT groupID FROM groups WHERE groupDesc LIKE ? OR groupLocationText LIKE ? OR groupName LIKE ? OR groupClass LIKE ?"; 
		
		if ($statement = mysqli_prepare($conn, $sql)) {
			$statement->bind_param("ssss", $search, $search, $search, $search);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			while ($row = $result->fetch_assoc()) {
				echo ($row["groupID"] . "\n"); 
			}
		}	
?>
