package cs131.classbuddies;

public class User{

    public enum ERR { NONE, NO_CONNECTION, DB_FAILURE };

    private int     userID;
    private ERR     error = ERR.NONE;
    private String  userName;
    private String  userPWhash;
    private String  userEmail;
    private String  userMajor;
    private String  classes;
    private int[]   friends;
    private int[]   groups;
    private boolean userEmailConf;


    public User() {
        // Used by Server.java for generic user.
    }

    public User(int userID, boolean server){
        if (server) {
            this.userID = userID;
        }
    }

    public User(int userID) {

        boolean success = false;
        int count = 0;

        while (count < 1000) {
            if (Server.serverConnected()) {
                User temp = Server.getUserByID(userID);
                if (temp != null) {
                    this.userID 	= userID;
                    this.userName 	= temp.getName();
                    this.userEmail 	= temp.getEmail();
                    this.userPWhash = temp.getPWhash();
                    this.userMajor 	= temp.getMajor();
                    this.classes 	= temp.getClasses();
                    this.friends 	= temp.getFriends();
                    this.groups 	= temp.getGroups();
                }
                else {
                    this.error = ERR.DB_FAILURE;
                }
                success = true;
                count = 1000;
            }
            else {
                count++;
            }
        }

        if (!success) { this.error = ERR.NO_CONNECTION; }

    }

    public User(User copy){
        this.userID = copy.getUserID();
        userName = copy.getName();
        userPWhash = copy.getPWhash();
        userEmail = copy.getEmail();
        userMajor = copy.getMajor();
        classes = copy.getClasses();
        friends = copy.getFriends();
        groups = copy.getGroups();
        userEmailConf = copy.getEmailConfirmed();
    }

    // SETS & GETS
    int getUserID() { return this.userID; }

    void setName(String userName) { this.userName = userName; }
    String getName() { return this.userName; }

    void setPWhash(String pwHash) { this.userPWhash = pwHash; }
    String getPWhash() { return this.userPWhash; }

    void setEmail(String email) { this.userEmail = email; }
    String getEmail() { return this.userEmail; }

    void setMajor(String major) { this.userMajor = major; }
    String getMajor() { return this.userMajor; }

    void setClasses(String classes) { this.classes = classes; }
    String getClasses() { return this.classes; }

    void setFriends(int[] intArray) { this.friends = intArray; }
    int[] getFriends() { return this.friends; }

    void setGroups(int[] intArray) { this.groups = intArray; }
    int[] getGroups() { return this.groups; }

    void setEmailConfirmed(boolean emailConf) { this.userEmailConf = emailConf; }
    boolean getEmailConfirmed() { return this.userEmailConf; }

}
