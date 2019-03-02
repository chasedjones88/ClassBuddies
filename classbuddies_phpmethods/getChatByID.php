<?php
		$chatID = $_POST['chatID']; 

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();								// dies quietly
		}
		
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM chats WHERE chatID=?")) {
			$statement->bind_param("i", $chatID);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			while ($row = $result->fetch_assoc()) {
				echo $row["chatOwner"] . "\n" . $row["chatDesc"] . "\n" . $row["chatWinURL"];
			}
		}
?>
