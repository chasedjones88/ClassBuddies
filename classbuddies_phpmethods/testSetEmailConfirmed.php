<?php
		$name = $_POST['name'];
		$pwHash = $_POST['pwHash'];
		$emailConfirmedValue = $_POST['confirmedValue'];

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo "0";
			exit();
		}

		$sql = "UPDATE user SET
        				userIsValidated = ?
        				WHERE userName = ? AND userPWHash = ?";

		if ($statement = mysqli_prepare($conn, $sql)) {
			$statement->bind_param("iss", $emailConfirmedValue, $name, $pwHash);
			if ($statement->execute()) {
				echo "1";						// app requirement - returns '1' on successful entry.
			}
		}

        echo "0";

?>
