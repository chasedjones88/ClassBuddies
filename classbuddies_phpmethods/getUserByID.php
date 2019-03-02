<?php
		$userID = $_POST['userID'];

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (!$conn) {
			die("Connection failed: " . mysqli_connect_error());
		}

		if (!is_numeric($userID)) {
			die("0"); 
		}

		$sql = "SELECT * FROM user WHERE `userID`='$userID'";

		$result = $conn->query($sql); 
		
		if ($result->num_rows > 0) {
			while($row = $result->fetch_assoc()) {
				echo $row["userName"] . "\n" . $row["userIsValidated"]. "\n" . $row["userMajor"] . "\n" . $row["userClasses"] . "\n" . $row["userEmail"] . "\n" . $row["userPWHash"];
			}
		} else {
			echo "0 results";
		}
?>
