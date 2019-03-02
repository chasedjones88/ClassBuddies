package cs131.classbuddies;

import java.util.Date;

public class Group {
    public enum STATUS { OPEN, CLOSED, EXPIRED, PRIVATE }

    private int     groupID;
    private String  groupName;
    private String  groupClass;
    private String  groupDesc;
    private int     ownerID;
    private int     groupChatID;
    private int[]   memberIDs;
    private double  coordLat;
    private double  coordLong;
    private String  groupLocText;
    private Date    groupStart;
    private Date    groupEnd;
    STATUS          groupStatus;


    //  Constructor given descriptions
    public Group(int groupID, String name, String classes, String groupDesc, int ownerID, int chatID, double coordLat, double coordLong, String groupLocText, Date start, Date end, STATUS groupStatus){
        this.groupID = groupID;
        this.groupName = name;
        this.groupClass = classes;
        this.groupStart = start;
        this.groupEnd = end;
        this.groupDesc=groupDesc;
        this.ownerID=ownerID;
        this.groupChatID=chatID;
        this.coordLat=coordLat;
        this.coordLong=coordLong;
        this.groupLocText=groupLocText;
        this.groupStatus = groupStatus;
    }


    int getID(){ return this.groupID;  }
    void setID(int id) { this.groupID = id; }

    int getOwnerID(){ return this.ownerID;  }
    void setOwnerID(int OwnerID) { this.groupID = ownerID; }

    void setGroupDesc(String desc){ this.groupDesc=desc; }
    String getGroupDesc(){ return this.groupDesc; }

    void setLocationDesc(String location_desc){ this.groupLocText=location_desc;  }
    String getLocationDesc(){ return this.groupLocText; }

    double getCoordLat(){ return this.coordLat; }
    void setCoordLat(double lat) { this.coordLat = lat; }

    double getCoordLong(){ return this.coordLong;  }
    void setCoordLong(double longitude) { this.coordLong = longitude; }

    String getGroupName(){ return this.groupName;  }
    void setGroupName(String name) { this.groupName = groupName; }

    String getGroupClass(){ return this.groupClass;  }
    void setGroupClass(String classes) { this.groupName = groupClass; }

    Date getStartTime(){ return this.groupStart;  }
    void setStartTime(Date start) { this.groupStart = start; }

    Date getEndTime(){ return this.groupEnd;  }
    void setEndTime(Date end) { this.groupEnd = end; }

    int getChatID(){ return this.groupChatID; }
    void setChatID(int chatID) { this.groupChatID = chatID; }

    int[] getMemberList(){ return this.memberIDs; }
    void setMemberList(int[] arrMemberIDs) { this.memberIDs = arrMemberIDs; }

}
