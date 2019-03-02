<?php
		$userID = $_POST['userID']; 
		$friendID = $_POST['friendID'];

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();								// dies quietly
		}
		
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM user WHERE userID=?")) {
			$statement->bind_param("i", $friendID);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			while ($row = $result->fetch_assoc()) {
				echo $row["userName"] . "\n" . $row["userClasses"] . "\n" . $row["userMajor"] . "\n" . $row["userEmail"] . "\n";
			}
		}
		
		if ($statement = mysqli_prepare($conn, "SELECT friendNote FROM friends WHERE (userID=? AND friendID=?)")) {
			$statement->bind_param("ii", $userID, $friendID);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			while ($row = $result->fetch_assoc()) {
				echo $row["friendNote"];
			}
		}
?>
