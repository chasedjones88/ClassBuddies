package cs131.classbuddies;

import android.os.StrictMode;
import android.text.Html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import java.net.Socket;

class Server{

    private static String   serverIP    = "198.143.12.118";
    private static int      serverPort  = 80;
    private static String   serverURL   = "http://server.classbuddies.net/phpdbmethods/";
    private static String   chatURL     = "http://server.classbuddies.net/chat/chatwindow.php";


    /* METHOD: 		getChatUrl(int,int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, chatID
     * RETURNS:
     * 	  String    : formatted URL string for chat window
     */
    public static String getChatUrl(int userID, int chatID) {

        return chatURL +
               "?user=" + String.valueOf(userID) +
               "&chat=" + String.valueOf(chatID);
    }



    /* METHOD: 		serverConnected()
     * OVERLOADS: 	none
     * REQUIRES: 	none
     * RETURNS:
     * 		true 		: server connected
     * 		false		: cannot make connection to server.
     */
    public static boolean  serverConnected(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(serverIP, serverPort), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }



    /* METHOD: 		userCreate(String, String, String)
     * OVERLOADS: 	none
     * REQUIRES: 	name, passwordHash, email
     * RETURNS:
     *  	 0 			: successful creation
     *      -1			: user exists
     *      -2			: email exists
     *      -9			: db connect error
     *      -10			: db unspecified error
     */
    public static int userCreate(String name, String pwHash, String email) {
        // Adds user to database. Returns userID or 0 on error.

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("pwHash", pwHash);
        params.put("email", email);

        String returnMsg = send_receive("userCreate.php", params);


        returnMsg = returnMsg.trim();
        return isNumeric(returnMsg)? Integer.parseInt(returnMsg) : -10;
    }



    /* METHOD: 		userEmailConfirmed(String, String)
     * OVERLOADS: 	none
     * REQUIRES: 	name, passwordHash
     * RETURNS:
     * 	 int > 0	: userID
     *      0		: user doesn't exist
     *     -1		: user exists, email NOT authorized
     *     -9  		: db output error
     *     -10		: unspecified error
     */
    public static int userEmailConfirmed(String userName, String userPWhash){

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("name", userName);
        params.put("pwHash", userPWhash);

        String response = send_receive("userEmailConfirmed.php", params);
        response = response.trim();

        return isNumeric(response)? Integer.parseInt(response) : -10;
    }



    /* METHOD: 		testSetEmailConfirmed(String, String, boolean)
     *              FOR TEST PURPOSES ONLY
     * OVERLOADS: 	none
     * REQUIRES: 	name, passwordHash, emailConfirmedValue
     * RETURNS:
     * 	    1	    : successful operation
     *      0		: failed operation
     */
    public static int testSetEmailConfirmed(String userName, String userPWhash, boolean emailConfirmedValue){

        int setValue = (emailConfirmedValue) ? 1 : 0;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("name", userName);
        params.put("pwHash", userPWhash);
        params.put("confirmedValue", setValue);

        String response = send_receive("testSetEmailConfirmed.php", params);
        response = response.trim();

        return isNumeric(response)? Integer.parseInt(response) : 0;
    }



    /* METHOD: 		getUserByID(int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID
     * RETURNS:
     * 		User 	: success
     * 		Null	: fail
     */
    public static User getUserByID(int userID){

        final int USER_LINES = 6;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);

        String response = send_receive("getUserByID.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        if (lines.length == USER_LINES) {
            String name = lines[0].trim();
            boolean emailConfirmed = Boolean.parseBoolean(lines[1].trim());
            String major = lines[2].trim();
            String classes = lines[3].trim();
            String email = lines[4].trim();
            String pwHash = lines[5].trim();

            User returnUser = new User(userID, true);
            returnUser.setName(name);
            returnUser.setEmailConfirmed(emailConfirmed);
            returnUser.setMajor(major);
            returnUser.setClasses(classes);
            returnUser.setFriends(Server.getUsersFriends(userID));
            returnUser.setGroups(Server.getUsersGroups(userID));
            returnUser.setEmail(email);
            returnUser.setPWhash(pwHash);

            return returnUser;
        }
        else {
            return null;
        }
    }



    /* METHOD: 		getUsersFriends(int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID
     * RETURNS:
     * 		int[] 	: Array of friendIDs
     */
    public static int[] getUsersFriends(int userID){

        int friendCount = 0;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);

        String response = send_receive("getUsersFriends.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        for (int i=0;i < lines.length;i++) {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) { friendCount++; }
        }

        int[] friendIDs = new int[friendCount];

        friendCount = 0;
        for (int i=0; i < lines.length; i++)
        {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) {
                friendIDs[friendCount++] = Integer.parseInt(lines[i]);
            }
        }
        return friendIDs;
    }



    /* METHOD: 		getUsersGroups(int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID
     * RETURNS:
     * 		int[] 	: Array of groupIDs
     */
    public static int[] getUsersGroups(int userID){
        int groupCount = 0;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);

        String response = send_receive("getUsersGroups.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        for (int i=0;i < lines.length;i++) {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) { groupCount++; }
        }

        int[] groupIDs = new int[groupCount];

        groupCount = 0;
        for (int i=0; i < lines.length; i++)
        {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) {
                groupIDs[groupCount++] = Integer.parseInt(lines[i]);
            }
        }
        return groupIDs;
    }



    /* METHOD: 		getGroupByID(int)
     * OVERLOADS: 	none
     * REQUIRES: 	groupID
     * RETURNS:
     * 		Group 	: success
     * 		Null	: fail
     */
    public static Group getGroupByID(int groupID) {

        SimpleDateFormat mysqltime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        final int GROUP_LINES = 11;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("groupID", groupID);

        String response = send_receive("getGroupByID.php", params);
        response = response.trim();

        String lines[] = response.split("\\r?\\n");

        for(int i=0; i < lines.length; i++){
            lines[i] = desanitizeString(lines[i].trim());
        }

        if (lines.length == GROUP_LINES) {
            String groupName = lines[0];
            String groupClass = lines[1];
            String groupDesc = lines[2];
            int ownerID = Integer.parseInt(lines[3]);
            int chatID = Integer.parseInt(lines[4]);
            double gLat = Double.parseDouble(lines[5]);
            double gLong = Double.parseDouble(lines[6]);
            String locText = lines[7];


            Date groupStart = null;
            Date groupEnd = null;

            try {
                groupStart = mysqltime.parse(lines[8]);
                groupEnd = mysqltime.parse(lines[9]);
            } catch (ParseException e) {
                e.printStackTrace ( );
            }

           int stat = Integer.parseInt(lines[10]);
            Group.STATUS groupStatus = Group.STATUS.OPEN;
            switch(stat) {
                case 0: groupStatus = Group.STATUS.OPEN; break;
                case 1: groupStatus = Group.STATUS.CLOSED; break;
                case 2: groupStatus = Group.STATUS.EXPIRED; break;
                case 3: groupStatus = Group.STATUS.PRIVATE; break;
            }

            return new Group(groupID, groupName, groupClass, groupDesc,ownerID,chatID,gLat,gLong,locText, groupStart, groupEnd, groupStatus);
        }
        else {
            return null;
        }
    }



    /* METHOD: 		getFriendByID(int, int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, friendID
     * RETURNS:
     * 		Friend 	: success
     * 		Null	: fail
     */
    public static Friend getFriendByID(int userID, int friendID) {

        final int FRIEND_LINES = 5;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("friendID", friendID);

        String response = send_receive("getFriendByID.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        if (lines.length == FRIEND_LINES) {
            String name = lines[0].trim();
            String classes = lines[1].trim();
            String major = lines[2].trim();
            String email = lines[3].trim();
            String friendNote = lines[4].trim();

            Friend newFriend = new Friend(friendID);
            newFriend.setName(name);
            newFriend.setClasses(classes);
            newFriend.setMajor(major);
            newFriend.setEmail(email);
            newFriend.setFriendNote(friendNote);

            return newFriend;
        }
        else {
            return null;
        }
    }



    /* METHOD: 		getChatByID(int)
     * OVERLOADS: 	none
     * REQUIRES: 	chatID
     * RETURNS:
     * 		Chat 	: success
     * 		Null	: fail
     */
    public static Chat getChatByID(int chatID) {
        final int CHAT_LINES = 3;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("chatID", chatID);

        String response = send_receive("getChatByID.php", params);
        response = response.trim();

        String lines[] = response.split("\\r?\\n");

        if (lines.length == CHAT_LINES) {
            int owner = -1;

            if (isNumeric(lines[0].trim())) {
                owner = Integer.parseInt(lines[0].trim());
            }
            String desc = lines[1].trim();
            String chatURL = lines[2].trim();

            return new Chat(chatID, owner, desc, chatURL);
        }
        else {
            return null;
        }
    }



    /* METHOD: 		updateUser(User)
     * OVERLOADS: 	none
     * REQUIRES: 	userToUpdate
     * RETURNS:
     * 		true 	: success
     * 		false	: fail
     */
    // FRIENDS AND GROUPS ARE NOT UPDATED HERE --- they are managed individually with add/remove functions
    public static boolean updateUser(User updateUser) {

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", updateUser.getUserID());
        params.put("name", updateUser.getName());
        params.put("pwHash", updateUser.getPWhash());
        params.put("email", updateUser.getEmail());
        params.put("classes", updateUser.getClasses());
        params.put("major",  updateUser.getMajor());

        String returnMsg = send_receive("updateUser.php", params);
        returnMsg = returnMsg.trim();

        return ( isNumeric(returnMsg) && (Integer.parseInt(returnMsg)==1) );
    }



    /* METHOD: 		deleteUserAccount(User)
     * OVERLOADS: 	none
     * REQUIRES: 	userToDelete
     * RETURNS:
     * 		true 	: success
     * 		false	: fail
     */
    public static boolean deleteUserAccount(User deleteUser) {

        deleteUser.setEmail("");
        deleteUser.setPWhash("");
        deleteUser.setClasses("");
        deleteUser.setMajor("Deactivated Account");
        return updateUser(deleteUser);

    }




    /* METHOD: 		getUsersChatIDs(int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID
     * RETURNS:
     * 		int[] 	: Array of chatIDs
     */
    public static int[] getUsersChatIDs(int userID){
        int chatCount = 0;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);

        String response = send_receive("getUsersChatIDs.php", params);
        response = response.trim();

        String lines[] = response.split("\\r?\\n");


        for (int i=0;i < lines.length;i++) {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) { chatCount++; }
        }

        int[] chatIDs = new int[chatCount];

        chatCount = 0;
        for (int i=0; i < lines.length; i++)
        {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) {
                chatIDs[chatCount++] = Integer.parseInt(lines[i]);
            }
        }
        return chatIDs;
    }



    /* METHOD: 		addUserToGroup(int, int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, groupID
     * RETURNS:
     * 		0 	: 	success
     * 	   -1	: 	relationship already exists
     *     -9	: 	database connection error
     *    -10	: 	unspecified database error
     */
    public static int addUserToGroup(int userID, int groupID){

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("groupID", groupID);

        String response = send_receive("addUserToGroup.php", params);
        response = response.trim();

        if (!isNumeric(response)) { response = "-10"; }

        // Automatically have user join the chat related to this group.
        Group added = Server.getGroupByID(groupID);
        if (added != null) {
            addUserToChat(userID, added.getChatID());
        }

        return Integer.parseInt(response);
    }



    /* METHOD: 		addUserToChat(int, int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, chatID
     * RETURNS:
     * 		0 	: 	success
     * 	   -1	: 	relationship already exists
     *     -9	: 	database connection error
     *    -10	: 	unspecified database error
     */
    public static int addUserToChat(int userID, int chatID){

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("chatID", chatID);

        String response = send_receive("addUserToChat.php", params);
        response = response.trim();

        if (!isNumeric(response)) { response = "-10"; }

        return Integer.parseInt(response);
    }



    /* METHOD: 		removeUserFromGroup(int, int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, groupID
     * RETURNS:
     * 		true 	: successful delete query (even if user-group relationship doesn't exist).
     * 		false	: failed database operation
     */
    public static boolean removeUserFromGroup(int userID, int groupID){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("groupID", groupID);
        String response = send_receive("removeUserFromGroup.php", params);
        response = response.trim();
        return ( isNumeric(response) && (Integer.parseInt(response)==1) );
    }



    /* METHOD: 		addFriendForUser(int, int, String)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, friendID, friendNote
     * RETURNS:
     * 		0 	: 	success
     * 	   -1	: 	relationship already exists
     *     -9	: 	database connection error
     *    -10	: 	unspecified datbase error
     */
    public static int addFriendForUser(int userID, int friendID, String friendNote){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("friendID", friendID);
        params.put("friendNote", friendNote);

        String response = send_receive("addFriendForUser.php", params);
        response = response.trim();

        if (!isNumeric(response)) { response = "-10"; }

        return Integer.parseInt(response);
    }



    /* METHOD: 		removeFriendForUser(int, int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, friendID
     * RETURNS:
     * 		true 	: success
     * 		false	: failed
     */
    public static boolean removeFriendForUser(int userID, int friendID){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("friendID", friendID);

        String response = send_receive("removeFriendForUser.php", params);
        response = response.trim();

        return ( isNumeric(response) && (Integer.parseInt(response)==1) );
    }



    /* METHOD: 		setFriendNote(int, int, String)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, groupID, friendNote
     * RETURNS:
     * 		true 	: success
     * 		false	: failed
     */
    public static boolean setFriendNote(int userID, int friendID, String note){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("friendID", friendID);
        params.put("friendNote", note);

        String response = send_receive("setFriendNote.php", params);
        response = response.trim();

        return ( isNumeric(response) && (Integer.parseInt(response)==1) );
    }




    /* METHOD: 		groupCreate(String, String, String, String, double, double, int, Date, Date)
     * OVERLOADS: 	none
     * REQUIRES: 	name, className, description, locationText, latitude, longitude, ownerID, startDate, endDate
     * RETURNS:
     * 		groupID	: success
     * 		-1		: failed
     * 		-9		: db connect error
     */
    public static int groupCreate(String name, String className, String desc, String locationText, double lat, double lng, int ownerID, Date start, Date end) {

        Chat newChat = Server.createChat(ownerID, 0, "Group: " + name);

        SimpleDateFormat mysqlTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("name", sanitizeString(name));
        params.put("class", sanitizeString(className));
        params.put("desc", sanitizeString(desc));
        params.put("locationText", sanitizeString(locationText));
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("ownerID", ownerID);
        if (newChat != null) {
            params.put("chatID", newChat.getID());
        }
        params.put("start", mysqlTime.format(start));
        params.put("end",mysqlTime.format(end));

        String response = send_receive("groupCreate.php", params);

        return ( isNumeric(response) ) ? Integer.parseInt(response) : -1;

    }



    /* METHOD: 		getAllGroups()
     * OVERLOADS: 	none
     * REQUIRES: 	none
     * RETURNS:
     * 		Group[] : success
     * 		Null	: failed or no groups
     */
    public static Group[] getAllGroups(){


        SimpleDateFormat mysqlTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("groupID", 0);

        String response = send_receive("getAllGroups.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        int groupCount = 0;											// Count groups
        for (int i=0; i < lines.length;i++) {
            if (lines[i].length() > 2) { groupCount++; }
        }

        int groupIndex = 0; 										// Keeps groups index
        if (groupCount != 0) {
            Group allGroups[] = new Group[groupCount];

            for (int i=0; i < lines.length;i++) {
                lines[i] = lines[i].trim();
                if (lines[i].length() > 2) {

                    String[] parts = lines[i].split(":");

                    int groupID = Integer.parseInt(parts[0].trim());
                    String groupName = desanitizeString(parts[1].trim());
                    String groupClass = desanitizeString(parts[2].trim());
                    String groupDescription = desanitizeString(parts[3].trim());
                    int ownerID = (parts[4].length() > 0) ? Integer.parseInt(parts[4].trim()) : 0;
                    int chatID = (parts[5].length() > 0) ? Integer.parseInt(parts[5].trim()) : 0;
                    double coordLong = Double.parseDouble(parts[6].trim());
                    double coordLat = Double.parseDouble(parts[7].trim());
                    String groupLocationText = desanitizeString(parts[8].trim());
                    Date groupStart = null;
                    Date groupEnd = null;
                    try {
                        groupStart = mysqlTime.parse(desanitizeString(parts[9].trim()));
                        groupEnd = mysqlTime.parse(desanitizeString(parts[10].trim()));
                    } catch (ParseException e) {
                        e.printStackTrace ( );
                    }

                    int stat = Integer.parseInt(parts[11]);
                    Group.STATUS groupStatus = Group.STATUS.CLOSED;

                    switch(stat) {
                        case 0: groupStatus = Group.STATUS.OPEN; break;
                        case 1: groupStatus = Group.STATUS.CLOSED; break;
                        case 2: groupStatus = Group.STATUS.EXPIRED; break;
                        case 3: groupStatus = Group.STATUS.PRIVATE; break;
                    }
                    allGroups[groupIndex++] = new Group(groupID, groupName, groupClass, groupDescription, ownerID, chatID, coordLong, coordLat, groupLocationText, groupStart, groupEnd, groupStatus);
                }
            }

            return allGroups;
        }
        else {
            return null;
        }
    }



    /* METHOD: 		getGroupUserList(int)
     * OVERLOADS: 	none
     * REQUIRES: 	groupID
     * RETURNS:
     * 		int[]
     */
    public static int[] getGroupUserList(int groupID){

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("groupID", groupID);

        String response = send_receive("getGroupUserList.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        int userCount = 0;

        for (int i=0;i < lines.length;i++) {
            lines[i] = desanitizeString(lines[i].trim());
            if (isNumeric(lines[i])) { userCount++; }
        }

        int[] userIDs = new int[userCount];

        userCount = 0;
        for (int i=0; i < lines.length; i++)
        {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) {
                userIDs[userCount++] = Integer.parseInt(lines[i]);
            }
        }
        return userIDs;
    }



    /* METHOD: 		getChatUserList(int)
     * OVERLOADS: 	none
     * REQUIRES: 	chatID
     * RETURNS:
     * 		int[]
     */
    public static int[] getChatUserList(int chatID){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("chatID", chatID);

        String response = send_receive("getChatUserList.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        int userCount = 0;

        for (int i=0;i < lines.length;i++) {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) { userCount++; }
        }

        int[] userIDs = new int[userCount];

        userCount = 0;
        for (int i=0; i < lines.length; i++)
        {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) {
                userIDs[userCount++] = Integer.parseInt(lines[i]);
            }
        }
        return userIDs;
    }



    /* METHOD: 		createChat(String)
     * OVERLOADS: 	none
     * REQUIRES: 	chatDescription
     * RETURNS:
     * 		Chat    : success
     * 		Null	: failed
     */
    public static Chat createChat(int ownerUserID, int friendID, String chatDesc){

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("ownerUserID", ownerUserID);
        params.put("friendID",friendID);
        params.put("chatDesc", chatDesc);
        params.put("url", chatURL);

        String response = send_receive("createChat.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        if (isNumeric(lines[0].trim())) {
            int chatID = Integer.parseInt(lines[0].trim());

            // Add chat to users list
            addUserToChat (ownerUserID, chatID);
            if (friendID > 0) { addUserToChat (friendID, chatID); }

            return new Chat(chatID, ownerUserID, chatDesc, chatURL);
        }
        else {
            return null;
        }
    }



    /* METHOD: 		getPrivateChat(int,int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID1, userID2
     * RETURNS:
     * 		Chat    : success
     * 		Null	: failed
     */
    public static Chat getPrivateChat(int userID_1, int userID_2){

        Chat returnChat = null;

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID_1", userID_1);
        params.put("userID_2",userID_2);

        String response = send_receive("getPrivateChat.php", params);
        response = response.trim();

        if (isNumeric(response)){
            int chatID = Integer.parseInt(response);
            if (chatID > 500000) {
                returnChat = getChatByID(chatID);
                // Force both users to rejoin existing chat in case they left.
                addUserToChat(userID_1, chatID);
                addUserToChat(userID_2, chatID);
            }
        }
        return returnChat;
    }



    /* METHOD: 		removeUserFromChat(int, int)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, chatID
     * RETURNS:
     * 		true 	: successful delete query (even if user-chat relationship doesn't exist).
     * 		false	: failed database operation
     */
    public static boolean removeUserFromChat(int userID, int chatID){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("chatID", chatID);
        String response = send_receive("removeUserFromChat.php", params);
        response = response.trim();
        return ( isNumeric(response) && (Integer.parseInt(response)==1) );
    }



    /* METHOD: 		updateChatDesc(int, int, String)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, chatID, chatDescription
     * RETURNS:
     * 		Chat    : success
     * 		Null	: failed -- can fail if userID != chat creator
     */
    public static boolean updateChatDesc(int userID, int chatID, String chatDesc){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("chatID", chatID);
        params.put("chatDesc", chatDesc);

        String response = send_receive("updateChatDesc.php", params);
        response = response.trim();

        return ( isNumeric(response) && (Integer.parseInt(response)==1) );
    }



    /* METHOD: 		sendChatMessage(String)
     * OVERLOADS: 	none
     * REQUIRES: 	userID, chatID, chatMessage
     * RETURNS:
     * 		Chat    : success
     * 		Null	: failed
     */
    public static boolean sendChatMessage(int userID, int chatID, String chatMessage){

        chatMessage = Html.escapeHtml(chatMessage).toString();      // Clean HTML out of string to prevent hijacking.

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("userID", userID);
        params.put("chatID", chatID);
        params.put("chatMessage", chatMessage);

        String response = send_receive("sendChatMessage.php", params);
        response = response.trim();

        return ( isNumeric(response) && (Integer.parseInt(response)==1) );
    }



    /* METHOD: 		searchGroups(String)
     * OVERLOADS: 	none
     * REQUIRES: 	searchString
     * RETURNS:
     * 		int[]   : array of groupIDs
     * 		empty	: failed to find matches
     */
    public static int[] searchGroups(String search){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("search", search);

        String response = send_receive("searchGroups.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        int groupCount = 0;

        for (int i=0;i < lines.length;i++) {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) { groupCount++; }
        }

        int[] searchGroups = new int[groupCount];

        groupCount = 0;
        for (int i=0; i < lines.length; i++)
        {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) {
                searchGroups[groupCount++] = Integer.parseInt(lines[i]);
            }
        }
        return searchGroups;
    }



    /* METHOD: 		searchUsers(String)
     * OVERLOADS: 	none
     * REQUIRES: 	searchString
     * RETURNS:
     * 		int[]   : array of userIDs
     * 		empty	: failed to find matches
     */
    public static int[] searchUsers(String search){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("search", search);

        String response = send_receive("searchUsers.php", params);
        response = response.trim();
        String lines[] = response.split("\\r?\\n");

        int userCount = 0;

        for (int i=0;i < lines.length;i++) {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) { userCount++; }
        }

        int[] searchResponses = new int[userCount];

        userCount = 0;
        for (int i=0; i < lines.length; i++)
        {
            lines[i] = lines[i].trim();
            if (isNumeric(lines[i])) {
                searchResponses[userCount++] = Integer.parseInt(lines[i]);
            }
        }
        return searchResponses;
    }



    /* METHOD: 		deactivateGroup(int)
     * OVERLOADS: 	none
     * REQUIRES: 	groupID
     * RETURNS:
     * 		true    : success
     * 		false	: failed
     */
    public static boolean deactivateGroup(int groupID){
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("groupID", groupID);

        String response = send_receive("deactivateGroup.php", params);
        response = response.trim();

        return ( isNumeric(response) && (Integer.parseInt(response)==1) );
    }



    /* METHOD: 		isNumeric(String)
     * OVERLOADS: 	none
     * REQUIRES: 	s
     * RETURNS:
     * 		True 	: is number
     *		False	: not number
     */
    private static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }



   	/* METHOD: 		sanitizeString(String)
     * OVERLOADS: 	none
     * REQUIRES: 	string
     * RETURNS:
     * 		String  : string clean of ":" and "\n"
     */
    private static String sanitizeString(String dirty_string){
        String cleanString = dirty_string.replaceAll(":", ";;");
        cleanString = cleanString.replaceAll("\n","-!n-");

        return cleanString;
    }



    /* METHOD: 		desanitizeString(String)
     * OVERLOADS: 	none
     * REQUIRES: 	string
     * RETURNS:
     * 		String  : string clean of ":" and "\n"
    */
    private static String desanitizeString(String clean_string){
        String dirtyString = clean_string.replaceAll(";;", ":");
        dirtyString = dirtyString.replaceAll("-!n-","\n");

        return dirtyString;
    }



    /* METHOD: 		hyphenString(String, int)
     * OVERLOADS: 	none
     * REQUIRES: 	word, length
     * RETURNS:
     * 	  String    : hyphenated at length repeatedly
     */
    public static String hyphenString(String str, int length) {
        if (str.length() <= length) {
            return str;
        }
        else {
            return str.substring(0, length) + "-" + hyphenString(str.substring(length, str.length()), length);
        }
    }



    /* METHOD: 		send_receive(String,Map)
     * OVERLOADS: 	none
     * REQUIRES: 	urlPage, params
     * RETURNS:
     * 		String  : Server Response
     */
    private static String send_receive(String urlPage, Map<String,Object> params) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        StringBuilder returnMsg = new StringBuilder ( );

        String fullURL = serverURL + urlPage;

        byte[] postDataBytes = null;

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            try {
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                postDataBytes = postData.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        try {
            HttpURLConnection phpConn;
            URL url = new URL(fullURL);
            phpConn = (HttpURLConnection)url.openConnection();
            phpConn.setConnectTimeout(1000);
            phpConn.setRequestMethod("POST");
            phpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (postDataBytes != null) {
                phpConn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            }
            phpConn.setDoOutput(true);
            if (postDataBytes != null) {
                phpConn.getOutputStream().write(postDataBytes);
            }

            Reader response = new BufferedReader(new InputStreamReader(phpConn.getInputStream(), "UTF-8"));

            for (int c; (c = response.read()) >= 0;) {
                returnMsg.append ((char) c);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnMsg.toString ( );
    }


    public static void main(String[] args) {



    }
}
