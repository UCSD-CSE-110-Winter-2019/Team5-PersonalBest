package edu.ucsd.cse110.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.ucsd.cse110.personalbest.chatmessage.ChatMessageService;
import edu.ucsd.cse110.personalbest.chatmessage.ChatMessageServiceFactory;
import edu.ucsd.cse110.personalbest.chatmessage.FirebaseFirestoreAdapter;
import edu.ucsd.cse110.personalbest.notification.FirebaseCloudMessagingAdapter;
import edu.ucsd.cse110.personalbest.notification.NotificationService;
import edu.ucsd.cse110.personalbest.notification.NotificationServiceFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    public static final String SHARED_PREFERENCES_NAME = "FirebaseLabApp";
    public static final String CHAT_MESSAGE_SERVICE_EXTRA = "CHAT_MESSAGE_SERVICE";
    public static final String NOTIFICATION_SERVICE_EXTRA = "NOTIFICATION_SERVICE";

    public static final String COLLECTION_KEY = "chats";
    public static final String MESSAGES_KEY = "messages";
    public static final String FROM_KEY = "from";
    public static final String TEXT_KEY = "text";
    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String TAG = "MessageActivity";

    private Button send_button;
    private Button back_button;

    String user_email;
    String friend_email;
    String document_key = "chat1";

    ChatMessageService chat;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        user_email = getIntent().getStringExtra("user_email");
        friend_email = getIntent().getStringExtra("friend_email");

        from = sharedPreferences.getString(FROM_KEY, user_email);

        // Construct document key for the private chat room
        if (user_email != null && friend_email != null) {
            this.document_key = buildDocumentKey(user_email, friend_email);
        }

        setContentView(R.layout.activity_message);


        back_button = (Button) findViewById(R.id.message_back_button);
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        send_button = (Button) findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        String stringExtra = getIntent().getStringExtra(CHAT_MESSAGE_SERVICE_EXTRA);
        chat = ChatMessageServiceFactory.getInstance().getOrDefault(stringExtra, FirebaseFirestoreAdapter::getInstance);
        if (user_email != null && friend_email != null) {
            chat = new FirebaseFirestoreAdapter(document_key);
        }

        initMessageUpdateListener();

        subscribeToNotificationsTopic();

        EditText nameView = findViewById((R.id.user_name));
        nameView.setText(from);
        nameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                from = s.toString();
                sharedPreferences.edit().putString(FROM_KEY, from).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private String buildDocumentKey(String user_email, String friend_email) {
        if (user_email.indexOf("@") != -1) {
            user_email = user_email.substring(0, user_email.indexOf("@"));
        }
        if (friend_email.indexOf("@") != -1) {
            friend_email = friend_email.substring(0, friend_email.indexOf("@"));
        }

        if (user_email.compareTo(friend_email) >= 0) {
            return user_email + "~" + friend_email;
        }
        return friend_email + "~" + user_email;
    }

    private void sendMessage(){
        EditText messageView = (EditText) findViewById(R.id.text_message);
        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, from);
        newMessage.put(TEXT_KEY, messageView.getText().toString());
        //chat.add(newMessage);
        chat.addMessage(newMessage).addOnSuccessListener(result -> {
            messageView.setText("");
            //Toast.makeText(this,"lalalalalala", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
        //Toast.makeText(this,"lalalalalala", Toast.LENGTH_SHORT).show();
    }

    private void initMessageUpdateListener() {
        TextView chatView = findViewById(R.id.chat);
        chat.addOrderedMessagesListener(
                chatMessagesList -> {
                    Log.d(TAG, "msg list size:" + chatMessagesList.size());
                    chatMessagesList.forEach(chatMessage -> {
                        chatView.append(chatMessage.toString());
                    });
                });
    }

    private void subscribeToNotificationsTopic() {
        NotificationServiceFactory notificationServiceFactory = NotificationServiceFactory.getInstance();
        String notificationServiceKey = getIntent().getStringExtra(NOTIFICATION_SERVICE_EXTRA);
        NotificationService notificationService = notificationServiceFactory.getOrDefault(notificationServiceKey, FirebaseCloudMessagingAdapter::getInstance);

        notificationService.subscribeToNotificationsTopic(document_key, task -> {
            String msg = "Subscribed to notifications";
            if (!task.isSuccessful()) {
                msg = "Subscribe to notifications failed";
            }
            Log.d(TAG, msg);
            Toast.makeText(MessageActivity.this, msg, Toast.LENGTH_SHORT).show();
        });
    }

}

