<?php
		$name = $_POST['name']; 
		$class = $_POST['class']; 
		$desc = $_POST['desc'];
		$locationText = $_POST['locationText']; 
		$lat = $_POST['lat']; 
		$lng = $_POST['lng']; 
		$ownerID = $_POST['ownerID']; 
		$chatID = $_POST['chatID']; 
		$start = $_POST['start']; 
		$end = $_POST['end']; 

		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on error.
			exit();
		}
		
		if ($statement = mysqli_prepare($conn, "INSERT INTO groups (groupName, groupClass, groupDesc, groupLocationText, groupCoordLat, groupCoordLong, ownerID, groupChatID, groupStartTime, groupEndTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
			$statement->bind_param("ssssddiiss", $name, $class, $desc, $locationText, $lat, $lng, $ownerID, $chatID, $start, $end);
			if ($statement->execute()) {
				echo mysqli_insert_id($conn);		// app requirement - returns groupID on success
			} else {
				echo "-1"; 						// app requirement - returns '-1' db unspecified error
			}
		} else {
			echo "-1";							// app requirement - returns '-1' db unspecified error
		}
?>
