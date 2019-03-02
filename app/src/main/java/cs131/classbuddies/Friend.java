package cs131.classbuddies;

class Friend extends User {
    private int friendID;
    private String friendNote;

    Friend(){

    }

    Friend(int friendID){
        this.friendID = friendID;
    }

    //  Returns user generated note about friend.
    String getFriendNote(){
        return this.friendNote;
    }

    //  Sets user generated note about friend with String limit of 160 characters.
    void setFriendNote(String friendNote){
        if (friendNote.length() > 159) {
            friendNote = friendNote.substring(0,159);
        }

        this.friendNote = friendNote;
    }
}