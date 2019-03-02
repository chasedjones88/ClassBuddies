<?php
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();									// fails quitely, creating an empty list
		}
		
		$sql = "SELECT * FROM groups WHERE status = 0 AND groupEndTime > SUBTIME(SYSDATE(), '0 3:0:0')"; 
		
		$statement = mysqli_prepare($conn, $sql); 
		$statement->execute(); 
		$result = $statement->get_result(); 
			
		while ($row = $result->fetch_assoc()) {
			echo ($row["groupID"] . ":" . $row["groupName"] . ":" . $row["groupClass"] . ":" .$row["groupDesc"] . ":" . $row["ownerID"] . ":" . $row["groupChatID"] . ":" . $row["groupCoordLat"] . ":" . $row["groupCoordLong"] . ":" . $row["groupLocationText"] . ":" . str_replace(":",";;",$row["groupStartTime"]) . ":" . str_replace(":",";;",$row["groupEndTime"]) . ":" . $row["status"] . "\n"); 
		}
?>
