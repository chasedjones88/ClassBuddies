<?php
		$userID_1 = $_POST['userID_1'];
		$userID_2 = $_POST['userID_2']; 
		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();
		}
		
		$sql = "SELECT chatID FROM chats WHERE (chatOwner = ? AND chatFriendID = ? ) OR (chatOwner = ? AND chatFriendID = ? )"; 
		$statement = mysqli_prepare($conn, $sql); 
		if ($statement->bind_param("iiii", $userID_1, $userID_2, $userID_2, $userID_1)) {
			 
			$statement->execute(); 
			$result = $statement->get_result(); 

			while ($row = $result->fetch_assoc()) {
				echo ($row["chatID"] . "\n"); 
			}
 
		}
		else {
			echo "failed";
		}
?>
