package edu.ucsd.cse110.personalbest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();

    String COLLECTION_KEY = "chats";
    String DOCUMENT_KEY = "chat1";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    CollectionReference chat;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("PersonalBest", Context.MODE_PRIVATE);
        from = sharedPreferences.getString(FROM_KEY, null);
        setContentView(R.layout.activity_message);
        Button send = (Button) findViewById(R.id.btn_send);
        Button goBack = (Button) findViewById(R.id.go_back);

        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);

        initMessageUpdateListener();

        findViewById(R.id.btn_send).setOnClickListener(view -> sendMessage());
        //call subscribe
        
        subscribeToNotificationsTopic();

    }

    private void sendMessage(){
        EditText messageView = (EditText) findViewById(R.id.text_message);
        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, from);
        newMessage.put(TEXT_KEY, messageView.getText().toString());
        //chat.add(newMessage);
        chat.add(newMessage).addOnSuccessListener(result -> {
            messageView.setText("");
            //Toast.makeText(this,"lalalalalala", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
        //Toast.makeText(this,"lalalalalala", Toast.LENGTH_SHORT).show();
    }

    private void initMessageUpdateListener() {
        chat.addSnapshotListener((newChatSnapShot, error) -> {
            if (error != null) {
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }

            if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                documentChanges.forEach(change -> {
                    QueryDocumentSnapshot document = change.getDocument();
                    sb.append(document.get(FROM_KEY));
                    sb.append(":\n");
                    sb.append(document.get(TEXT_KEY));
                    sb.append("\n");
                    sb.append("---\n");
                });


                TextView chatView = findViewById(R.id.chat);
                chatView.append(sb.toString());
            }
        });
    }

    private void subscribeToNotificationsTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(DOCUMENT_KEY)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed to notifications";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(MessageActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }


}

