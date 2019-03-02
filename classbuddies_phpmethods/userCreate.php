<?php
		$name = $_POST['name'];
		$pwHash = $_POST['pwHash']; 
		$email = $_POST['email']; 
		
		$unique = md5(uniqid("a")) . "-" . md5(uniqid("b", true));  

		require("_dbconn.php");

		$conn = mysqli_connect($dbServer, $dbUsername, $dbPassword, $dbDatabase);

		if (mysqli_connect_errno()) {
			echo '-9'; 							// app requirement- returns '-9' on db connect error.
			exit();
		}
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM user WHERE ( UPPER(userName) = UPPER(?) )")) {
			$statement->bind_param("s", $name); 
			$statement->execute(); 
			$statement->store_result(); 
			 
			
			if ($statement->num_rows != 0) {
				echo "-1";						// app requirement - returns '-1' with duplicate username.
				exit(); 
			}
		}
		
		if ($statement = mysqli_prepare($conn, "SELECT * FROM user WHERE (userEmail = ?)")) {
			$statement->bind_param("s", $email); 
			$statement->execute(); 
			$statement->store_result(); 
			
			if ($statement->num_rows != 0) {
				echo "-2";						// app requirement - returns '-2' with duplicate email.
				exit(); 
			}
		}
		
		if ($statement = mysqli_prepare($conn, "INSERT INTO user (userName, userPWHash, userEmail, userValidation) VALUES (?, ?, ?, ?)")) {
			$statement->bind_param("ssss", $name, $pwHash, $email, $unique);
			if ($statement->execute()==false) {
				echo "-10"; 					// app requirement - returns '-10' db unspecified error
				exit(); 
			}
		}	
		

		require_once "Mail.php";
		require_once "Mail/mime.php";

		
		$email_subject = "ClassBuddies Email Confirmation";
		$email_from = "noreply@classbuddies.net"; 
		$email_url = "http://server.classbuddies.net/confirm/?uid=" . str_replace(" ", "-", $name) . "&activation=" . $unique;
		$email_message = "Please click on the link below or copy/paste the link into your web browser to activate your ClassBuddies account:\n\n" . $email_url . "\n\nThanks,\nClassBuddies";


		 
		$mail_headers['From']    = $email_from;
		$mail_headers['To']      = $email;
		$mail_headers['Subject'] = $email_subject;

		$mail_body               = $email_message;

		$mail_params['host']      = 'ssl://mail.classbuddies.net';
		$mail_params['port']      = 465;
		$mail_params['auth']      = true;
		$mail_params['localhost'] = 'server.classbuddies.net'; 
		$mail_params['username']  = $emailusername; 
		$mail_params['password']  = $emailpassword;
		// $mail_params['debug'] = true;

		$mail_object = Mail::factory('smtp', $mail_params);
		$mail_object->send($email, $mail_headers, $mail_body);
		
		if (PEAR::isError($mail_object)) {
			echo("-12");
		} else {
			echo("0");
		}
?>
