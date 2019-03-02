<?php
		$groupID = $_POST['groupID'];

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			exit();								// dies quietly
		}
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM groups WHERE groupID=?")) {
			$statement->bind_param("i", $groupID);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			while ($row = $result->fetch_assoc()) {
				echo ($row["groupName"] . "\n" . $row["groupClass"] . "\n" .$row["groupDesc"] . "\n" . $row["ownerID"] . "\n" . $row["groupChatID"] . "\n" . $row["groupCoordLat"] . "\n" . $row["groupCoordLong"] . "\n" . $row["groupLocationText"] . "\n" . str_replace(":",";;",$row["groupStartTime"]) . "\n" . str_replace(":",";;",$row["groupEndTime"]) . "\n" . $row["status"]);
			}
		}
?>
