package cs131.classbuddies;

import java.util.List;
import java.util.Map;

class Search {
    private String  searchText;
    private boolean usersSelected = false;
    private boolean groupSelected = false;
    private List<String> searchList;
    private Map<Integer, Integer> resultsMap;

    Search(String searchText, boolean user, boolean group, List<String> searchList, Map<Integer, Integer> resultsMap){
        this.searchText     = searchText;
        this.usersSelected  = user;
        this.groupSelected  = group;
        this.searchList     = searchList;
        this.resultsMap     = resultsMap;
    }

    // Get Methods (Set by constructor)
    String getSearchText() { return searchText; }
    boolean isUsersSelected() { return usersSelected; }
    boolean isGroupSelected() { return groupSelected; }
    List<String> getSearchList() {return searchList; }
    Map<Integer, Integer> getResultsMap() { return resultsMap;}

}
