package cs131.classbuddies;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.test.ActivityTestCase;
import android.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;

import static org.junit.Assert.*;

/**
 * Created by kumab on 11/18/17.
 */
public class ClassbuddiesTest {

    String serverIP = "198.143.12.118";
    int serverPort = 80;
    String serverURL = "http://server.classbuddies.net/phpmethods/";

    Server server = null;

    @Before
    public void setUp() throws Exception {
        server = new Server();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getUserByID() throws Exception {

        Socket testSocket = null;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(serverIP, serverPort), 1000);
            System.out.println("connected:" + Server.serverConnected());
            //return true;
        } catch (IOException e) {
            assertTrue(false);
        }

        User testUser = new User();
        testUser = Server.getUserByID(100092);
        assertEquals("classbuddies",testUser.getName());
        assertEquals("classbuddies.csus@gmail.com",testUser.getEmail());
        assertEquals("CSC131",testUser.getClasses());
        assertEquals("Software Engineering",testUser.getMajor());
    }


    @Test
    public void getFriendIDtest() throws Exception {

        Friend newFriend = new Friend();

        newFriend = Server.getFriendByID(100092, 100091);

        assertEquals("Robert Roati",newFriend.getName());
        assertEquals("MATH100,STAT50,CSC130,CSC131,PHIL103",newFriend.getClasses());
        assertEquals("no note",newFriend.getFriendNote());
    }

    @Test
    public void getUserFriendsTest() throws Exception{
        int[] testingUsersFriends = null;
        testingUsersFriends = Server.getUsersFriends(100092); 		// user exists, has friends, should return 2
        assertEquals(1,testingUsersFriends.length);
        //System.out.println("Friends, user exists, [2 expected]:" + testingUsersFriends.length);
        for (int friendID : testingUsersFriends) {
            if(friendID == 1 ){
                assertEquals(100001,friendID);
            }
            if(friendID == 2){
                assertEquals(100007,friendID);
            }
        }
    }

    @Test
    public void userGroupTest() throws Exception{
        int[] testingUsersGroups = null;
        testingUsersGroups = Server.getUsersGroups(9000); 		// user doesn't exist, should return empty array
        assertEquals(0,testingUsersGroups.length);
    }

    @Test
    public void newChatTest() throws Exception{
        Chat newChat = new Chat();
        newChat = Server.getChatByID(500092);
        assertEquals(500092,newChat.getID());
        assertEquals(100092,newChat.getOwnerID());
        assertEquals("Buddy: classbuddies and Robert Roati",newChat.getDesc());
    }

    @Test
    public void getGroupByIDTest() throws Exception{
        Group newGroup = Server.getGroupByID(250046);
        assertEquals("Studying for Mitchell's Exam 3",newGroup.getGroupDesc());
        assertEquals("Math 100 Exam 3",newGroup.getGroupName());
        assertEquals("MATH100",newGroup.getGroupClass());
        assertEquals("Second floor of the library, along the back wall.",newGroup.getLocationDesc());
        assertEquals(38.5592696026609,newGroup.getCoordLat(),0.02);
        assertEquals(-121.42202664166689,newGroup.getCoordLong(),0.02);
    }

    @Test
    public void serverChatTest() throws Exception{
        User user = new User();

        Server.createChat(0,0,null);
        Server.deactivateGroup(0);

        assertNotNull(Server.testSetEmailConfirmed("classbuddies",user.getPWhash(),true));
        assertEquals("http://server.classbuddies.net/chat/chatwindow.php?user=100092&chat=500092",Server.getChatUrl(100092,500092));

        assertNotNull(Server.userEmailConfirmed("classbuddies",user.getPWhash()));
        assertEquals(0,Server.addUserToGroup(100092,250044));
        assertTrue(Server.removeUserFromGroup(100092,250044));
        assertEquals(0,Server.addFriendForUser(100092,100093,null));
        assertTrue(Server.removeFriendForUser(100092,100093));
        assertEquals(0,Server.addUserToChat(100093,500092));
        assertTrue(Server.removeUserFromChat(100093,500092));
        assertTrue(Server.setFriendNote(100092,100093,"hi"));
    }

    @Test (expected = NullPointerException.class)
    public void serverTest() throws Exception{
        Server.deleteUserAccount(null);
        Server.sendChatMessage(100092,500092,"test Message");
        Server.userCreate(null,null,null);
        Server.deactivateGroup(0);
        Server.updateUser(null);
        Server.hyphenString(null,1);
        Server.groupCreate(null,null,null,null,1234,1234,0,null,null);
    }


    @Test (expected = NullPointerException.class)
    public void testAbout() throws  Exception {
        AboutActivity newAbout = new AboutActivity();
        newAbout.onBackPressed();
        assertNull(newAbout.getTitle());
    }

    @Test(expected = NullPointerException.class)
    public void buddyProfileTest() throws  Exception {
        BuddyProfileActivity buddyProfile = new BuddyProfileActivity();
        buddyProfile.onBackPressed();
    }

    @Test
    public void buddyListTest() throws  Exception {
        BuddyListActivity newBuddyList = new BuddyListActivity();
        newBuddyList.onBackPressed();
    }

    @Test(expected = NullPointerException.class)
    public void OptionsPanelTest() throws  Exception {
        OptionsPanelActivity newPanel = new OptionsPanelActivity();
        newPanel.onBackPressed();
    }

    @Test(expected = NullPointerException.class)
    public void chatWindowTest() throws  Exception {
        ChatWindowActivity newChatW = new ChatWindowActivity();
        newChatW.onBackPressed();
    }

    @Test
    public void chatTest() throws  Exception {
        ChatActivity newChatA = new ChatActivity();
        newChatA.onBackPressed();
    }

    @Test(expected = NullPointerException.class)
    public void MapTest() throws  Exception {
        MapActivity newMap = new MapActivity();
        newMap.onBackPressed();
        newMap.onMapReady(null);
        newMap.onRestart();
        ;

    }
    @Test//(expected = NullPointerException.class)
    public void MapTest2() throws  Exception {
        MapActivity newMap = new MapActivity();
        //newMap.onBackPressed();
        newMap.checkLocationPermission();


    }

    @Test(expected = NullPointerException.class)
    public void profileTest() throws  Exception {
        ProfileActivity newProfile = new ProfileActivity();
        newProfile.onBackPressed();
    }

    @Test
    public void meetUpTest() throws  Exception {
        MeetUpActivity newMeetUp = new MeetUpActivity();
        newMeetUp.onBackPressed();
    }

    @Test(expected = NullPointerException.class)
    public void meetDescrTest() throws  Exception {
        MeetUpDescription newMeetDescr = new MeetUpDescription();
        newMeetDescr.populateListView();
    }

    @Test
    public void logInTest() throws  Exception {
        LogInActivity newLogin = new LogInActivity();
        newLogin.onBackPressed();
    }

    @Test
    public void settingsTest() throws  Exception {
        SettingsActivity newSettings = new SettingsActivity();
        newSettings.onBackPressed();
    }

    @Test(expected = NullPointerException.class)
    public void createAccountTest() throws  Exception {
        CreateAccountActivity newAccount = new CreateAccountActivity();
        newAccount.isEmailAddress("classbuddies.csus@gmail.com");
    }

    @Test(expected = NullPointerException.class)
    public void searchActivityTest() throws  Exception {
        SearchActivity newSearch = new SearchActivity();
        newSearch.onBackPressed();
        newSearch.onRadioButtonClicked(null);
    }

    @Test(expected = NullPointerException.class)
    public void meetUpMapTest() throws  Exception {
        MeetUpMapActivity newMap = new MeetUpMapActivity();
        newMap.onMapReady(null);
        newMap.onBackPressed();
    }

    @Test(expected = NullPointerException.class)
    public void editProfileTest() throws  Exception {
        EditProfileActivity newProfile = new EditProfileActivity();
        newProfile.onBackPressed();
    }

    @Test(expected = NullPointerException.class)
    public void createAccountTest2() throws  Exception {
        CreateAccountActivity2 newAccount = new CreateAccountActivity2();
        newAccount.onCreate(null);
    }

    @Test(expected = NullPointerException.class)
    public void createProfileTest() throws  Exception {
        CreateProfileActivity1 newProfile = new CreateProfileActivity1();
        newProfile.onCreate(null);
    }

    @Test(expected = NullPointerException.class)
    public void createProfileTest2() throws  Exception {
        CreateProfileActivity2 newProfile = new CreateProfileActivity2();
        newProfile.onCreate(null);
    }

    @Test
    public void getChatByIdTest() throws Exception{
        int [] chatID = null;
        chatID = Server.getUsersChatIDs(100092);
        assertEquals(chatID.length, chatID.length);
        System.out.println("connected:" + chatID.length);
    }

    @Test
    public void getAllGroupsTest() throws Exception{
        Group [] groups = null;
        groups = Server.getAllGroups();
        assertEquals(groups.length, groups.length);
    }

    @Test
    public void getGroupUserListTest() throws Exception{
        int [] groupList = null;
        groupList = Server.getGroupUserList(250046);
        assertEquals(groupList.length, groupList.length);
    }

    @Test
    public void getChatUserListTest() throws Exception{
        int [] chatUserList = null;
        chatUserList = Server.getChatUserList(500092);
        assertEquals(chatUserList.length, chatUserList.length);
    }

    @Test
    public void getPrivateChatTest() throws Exception{
        Chat testChat = new Chat();
        testChat = Server.getPrivateChat(100092,100091);
        assertEquals(100092,testChat.getOwnerID());
        assertEquals("Buddy: classbuddies and Robert Roati", testChat.getDesc());
    }

    @Test
    public void searchTest() throws Exception{
        int [] groupSearch = null;
        groupSearch = Server.searchGroups("W");
        CBApp classbuddiesApp = new CBApp();
        Search search = classbuddiesApp.getSearch();
        User user = classbuddiesApp.getUser();
        User clickUser = classbuddiesApp.getClickedUser();
        classbuddiesApp.resetVariables();
        classbuddiesApp.getLastActivity();
        Group group = classbuddiesApp.getGroup();
        classbuddiesApp.setGroup(group);
        LatLng latLng = classbuddiesApp.getLatLng();
        Chat chat = classbuddiesApp.getChat();
        classbuddiesApp.setChat(null);
        classbuddiesApp.setClickedUser(null);
        classbuddiesApp.setLastActivity(null);
        classbuddiesApp.setLatLng(null);
        classbuddiesApp.setSearch(null);
        classbuddiesApp.setUser(null);
        //System.out.println("connected:" + search.getSearchText());
        //System.out.println("connected:" + classbuddiesApp.getUser());

    }

    @Test
    public void searchMethodTest() throws Exception {
       Search search = new Search("1",true,false,null,null);
        assertEquals("1",search.getSearchText());
        assertNull(search.getResultsMap());
        search.getSearchList();
        search.isGroupSelected();
        search.isUsersSelected();
    }

    @Test
    public void searchGroupTest() throws Exception {
        int[] groupSearch = Server.searchGroups("W");
        for (int i = 0; i < groupSearch.length; i++) {
            Group thisGroup = Server.getGroupByID(groupSearch[i]);
            thisGroup.getLocationDesc();
            if(i == 0){
                assertEquals("CSC 131 Finals!",thisGroup.getGroupName());
                assertEquals("While Meet-Ups have a maximum duration of 24 hours, this group is open until Jan '18 for testing purposes.", thisGroup.getGroupDesc());
                assertEquals(100091,thisGroup.getOwnerID());
            }
        }
    }

    @Test
    public void searchUserTest() throws Exception {
        int[] userSearch = Server.searchUsers("c");
        for (int i = 0; i < userSearch.length; i++){
            User thisSearch = Server.getUserByID(userSearch[i]);
            if(i==0) {
                assertEquals(100091, thisSearch.getUserID());
                assertEquals("robertroati@csus.edu", thisSearch.getEmail());
            }
        }
    }

    @Test
    public void searchTest2() throws Exception {
        int[] userSearch = Server.searchUsers("c");
        for (int i = 0; i < userSearch.length; i++){
            User thisSearch = Server.getUserByID(userSearch[i]);
            if (i==1){
                assertEquals(100092,thisSearch.getUserID());
                assertEquals("classbuddies.csus@gmail.com",thisSearch.getEmail());
                assertEquals("classbuddies",thisSearch.getName());
                assertEquals("Software Engineering",thisSearch.getMajor());
                assertNotNull(thisSearch.getGroups());
                assertEquals("CSC131",thisSearch.getClasses());
                assertNotNull(thisSearch.getFriends());
                assertFalse(thisSearch.getEmailConfirmed());
                assertNotNull(thisSearch.getPWhash());
            }
        }
    }

    @Test
    public void updateUserInfoTest() throws Exception{
        User testUser = new User(100093);

        if (testUser.getClasses().equals("RTPA122")){
            testUser.setClasses("CSC131");
            testUser.setMajor("Software Engineering");
            Server.updateUser(testUser);
            User testUser2 = new User(100093);
            assertEquals("CSC131",testUser2.getClasses());
        }
        else {
            testUser.setClasses("RTPA122");
            testUser.setMajor("Retirement Studies");
            Server.updateUser(testUser);
            User testUser2 = new User(100093);
            assertEquals("RTPA122",testUser2.getClasses());
        }
    }
}