package cs131.classbuddies;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

public class CBApp extends Application {
    private User user;
    private Group group;
    private User clickedUser;
    private LatLng latlng;
    private Chat chat;
    private Search search = null;
    private LinkedList<Class<?>> lastActivityStack = new LinkedList<>();


    public void setUser(User user) { this.user = user; }
    public User getUser() { return this.user; }

    public void setGroup(Group group) { this.group = group; }
    public Group getGroup() { return this.group; }

    public User getClickedUser() {
        return clickedUser;
    }
    public void setClickedUser(User user) {
        clickedUser = user;
    }

    public LatLng getLatLng(){ return this.latlng;}
    public void setLatLng(LatLng latLng) {this.latlng = latLng;}

    public Chat getChat(){ return this.chat; }
    public void setChat(Chat chat) { this.chat = chat; }

    public Search getSearch(){ return search;}
    public void setSearch(Search search){this.search = search;}

    public void setLastActivity(Class<?> activity) {
        lastActivityStack.add(activity);
    }
    public Class<?> getLastActivity() {
        // Return last activity from stack, or MapActivity as an empty default value
        return (lastActivityStack.isEmpty()) ? MapActivity.class : lastActivityStack.removeLast();
    }

    // Resets stored Application variables to create a default state
    public void resetVariables() {
        search = null;
        lastActivityStack.clear();
    }

}