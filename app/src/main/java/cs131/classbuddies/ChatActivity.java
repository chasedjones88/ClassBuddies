package cs131.classbuddies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    CBApp classbuddiesApp;
    User user;
    ListView lv;
    ImageView menuButton;
    ArrayAdapter<String> adapter;
    @SuppressLint("UseSparseArrays")    // SparseArrays are slower than HashMaps
    Map<Integer, Chat> chatMap = new HashMap<>();
    // LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        // Force background color to slate_grey
        View root = findViewById(R.id.background).getRootView();
        root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.slate_grey));


        // Get Application data
        classbuddiesApp = (CBApp) getApplicationContext();
        user = classbuddiesApp.getUser();


        // Get UI handles
        lv = findViewById(R.id.list_view);
        menuButton = findViewById(R.id.menu_bars);


        // Populate List view
        List<String> chatList = new ArrayList<> ();
        int [] userChatIDs = Server.getUsersChatIDs(user.getUserID());      // Get chat IDs

        if (userChatIDs.length == 0) {
            // List is empty
            chatList.add("Nothing to display");
            lv.setEnabled(false);
        }
        else {
            // Populate list with chat information, add chat to chatMap for onclick recovery
            for (int chatID : userChatIDs){
                Chat thisChat = Server.getChatByID(chatID);
                if (thisChat != null) {
                    chatMap.put(chatMap.size(), thisChat);
                    chatList.add(thisChat.getDesc());
                }
            }
        }
        adapter = new ArrayAdapter<> (ChatActivity.this, android.R.layout.simple_list_item_1, chatList);
        lv.setAdapter(adapter);



        // LISTENER: Listview OnItemClick
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    classbuddiesApp.setChat(chatMap.get(position));
                    classbuddiesApp.setLastActivity(ChatActivity.class);
                    startActivity(new Intent(ChatActivity.this, ChatWindowActivity.class));
                    finish();
            }
        });


        // LISTENER: Menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(ChatActivity.class);
                startActivity(new Intent(ChatActivity.this, OptionsPanelActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChatActivity.this, MapActivity.class));
        finish();
    }


}
