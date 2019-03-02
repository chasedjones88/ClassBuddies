<?php

		$activationCode = isset($_GET['activation']) ? $_GET['activation'] : '';
		$response = "An error has occurred."; 

		if (strlen($activationCode)>20) {
			
			require("../phpdbmethods/_dbconn.php");
			
			$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

			if (mysqli_connect_errno()) {
				echo 'Cannot connect to database. Please try again later.'; 							
				exit();
			}
			
			$sql = "UPDATE user SET
					userIsValidated = 1
					WHERE userValidation = ?"; 
			
			$statement = mysqli_prepare($conn, $sql);
			$statement->bind_param("s", $activationCode);
			$statement->execute();

			
			if ( mysqli_affected_rows($conn) == 0 ) {
				$response = "<br><h1>Sorry</h1>We were unable to activate your account at this time. Either your account has already been activated, the activation link is invalid or an error has occurred."; 
			} 
			else {
				$response = "<br><h1>Congratulations!</h1><h3>Your account is now activated.</h3>Welcome to ClassBuddies!"; 
			}
		}
		
		echo $response;
?>
