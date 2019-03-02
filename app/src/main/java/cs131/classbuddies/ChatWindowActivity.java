package cs131.classbuddies;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatWindowActivity extends AppCompatActivity {
    private CBApp       classbuddiesApp;
    // UI
    private TextView    chatLabel;
    private Button      sendButton, buttonLeaveChat;
    private ImageView   menuButton;
    private WebView     webview;
    private WebSettings webSettings;
    // Variables
    private User        user;
    private Chat        chat;
    private final int   SCROLLING_PAUSE_DURATION = 2;
    private int         scrolling_pause_count = 0;
    private String      windowURL;



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create page layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);


        // Get Application variables
        classbuddiesApp = (CBApp) getApplicationContext();
        user = classbuddiesApp.getUser();
        chat = classbuddiesApp.getChat();


        // Get UI Handles
        webview         = findViewById(R.id.webViewChatWindow);
        chatLabel       = findViewById (R.id.textViewChatLabel);
        sendButton      = findViewById(R.id.buttonSend);
        menuButton      = findViewById(R.id.menu_bar_icon);
        buttonLeaveChat = findViewById(R.id.buttonLeaveChat);


        // Configure and Load Web view
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);                         // Javascript in chat window keeps scroll position at bottom.
        windowURL = Server.getChatUrl(user.getUserID(), chat.getID());
        webview.loadUrl(windowURL);
        webview.setClickable(false);


        // Fill UI Elements
        String chatDesc = chat.getDesc();
        if (chatDesc.length() > 29) chatDesc = chatDesc.substring(0,29) + "...";
        chatLabel.setText(chatDesc);


        // Create repeating timer to refresh webview every 3 seconds
        new CountDownTimer(300000, 3000) {
            @Override
            public void onTick(long l) {
                // Pause refresh if user is scrolling in chat window
                if (scrolling_pause_count == 0) {
                    webview.reload();
                }
                else {
                    scrolling_pause_count--;
                }
            }

            @Override
            public void onFinish() {
                // Restart timer when finished
                this.start();
            }
        }.start();



    // IGNORE WARNING --- Web view doesn't need click actions handled.
        // LISTENER: user is scrolling in chat window
        webview.setOnTouchListener(new View.OnTouchListener (){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrolling_pause_count = SCROLLING_PAUSE_DURATION;
                return false;
            }
        });



        // LISTENER: Send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editTextMessage = findViewById(R.id.editTextMessage);
                String message = editTextMessage.getText().toString();
                if(!message.equals("")) {
                    // Send message and empty the editText
                    Server.sendChatMessage(user.getUserID(), chat.getID(), message);
                    editTextMessage.setText("");
                    // Force reload immediately to display added message.
                    webview.reload();
                }

            }
        });



        // LISTENER: Menu button
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                classbuddiesApp.setLastActivity(ChatWindowActivity.class);
                startActivity(new Intent(ChatWindowActivity.this, OptionsPanelActivity.class));
                finish();
            }
        });



        // LISTENER: Leave chat button
        buttonLeaveChat.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper (ChatWindowActivity.this, R.style.myDialog));

                builder.setTitle("This will remove you from this chat.");
                builder.setMessage("If this is a group chat, you will need to leave and rejoin the group to rejoin this chat. Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Remove user from this chat group, and return to chatList
                        Server.removeUserFromChat(user.getUserID(), chat.getID());
                        dialog.dismiss();
                        onBackPressed();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChatWindowActivity.this, classbuddiesApp.getLastActivity()));
        finish();
    }

}