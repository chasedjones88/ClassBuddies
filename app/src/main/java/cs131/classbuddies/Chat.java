package cs131.classbuddies;

public class Chat {
    private int chatID;
    private int ownerID;
    private String chatDesc;
    private String chatWinURL;

    public Chat(){
        //  Basic constructor
    }

    //  Populating Constructor
    public Chat(int chatID, int owner, String chatDesc, String chatWinURL){
        this.chatID = chatID;
        this.ownerID = owner;
        this.chatDesc = chatDesc;
        this.chatWinURL = chatWinURL;
    }

    int getID(){ return this.chatID; }
    int getOwnerID(){ return this.ownerID; }
    String getDesc(){ return this.chatDesc; }
}
