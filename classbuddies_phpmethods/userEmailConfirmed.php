<?php
		$name = $_POST['name'];
		$pwHash = $_POST['pwHash']; 
		$return = 0; 							// app requirement - returns '0' when user not found. 

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on db error.
			exit();
		}
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM user WHERE UPPER(userName)=UPPER(?) AND userPWHash=?")) {
			$statement->bind_param("ss", $name, $pwHash);
			$statement->execute(); 
			$result = $statement->get_result(); 
			
			while ($row = $result->fetch_assoc()) {
				if ($row['userIsValidated'] == '0') { 
					$return = -1;  					// app requirement - returns '-1' when user is found, but not authorized.
				} else {
					$return = $row['userID']; 		// app requirement - returns 'userID' if found and email is validated.
				}
			}
		} else {
			$return = -9; 							// app requirement- returns '-9' on db error. 
		}	
		echo $return;
?>
