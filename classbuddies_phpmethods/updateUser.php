<?php
		$userID = $_POST['userID'];
		$name = $_POST['name'];
		$pwHash = $_POST['pwHash']; 
		$email = $_POST['email']; 
		$classes = $_POST['classes'];
		$major = $_POST['major'];

		
		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on db connect error.
			exit();
		}
		
		$sql = "UPDATE user SET
				userName = ?,
				userPWHash = ?,
				userEmail = ?,
				userClasses = ?,
				userMajor = ?
				WHERE userID = ?"; 
		
		if ($statement = mysqli_prepare($conn, $sql)) {
			$statement->bind_param("sssssi", $name, $pwHash, $email, $classes, $major, $userID);
			if ($statement->execute()) {
				echo "1";						// app requirement - returns '0' on successful entry. 
			} else {
				echo "-10"; 					// app requirement - returns '-10' db unspecified error
			}
		}	else {
			echo "-11"; 
		}
?>
