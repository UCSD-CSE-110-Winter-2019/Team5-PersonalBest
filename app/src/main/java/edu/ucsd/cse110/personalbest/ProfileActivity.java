package edu.ucsd.cse110.personalbest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "ProfileActivity";
    public static final String COLLECTION_KEY = "Users";

    TextView friend_name;

    String userEmail;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userEmail = getIntent().getSerializableExtra("email").toString();
        this.documentReference = FirebaseFirestore.getInstance().collection(COLLECTION_KEY).document(userEmail);

        this.getData();

        Log.d(TAG, "HAHAHAHAHAHAHAHAHAHA");

        Button goBack = (Button) findViewById(R.id.back_button);
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        friend_name = findViewById(R.id.name_label);

        Button message_button = (Button) findViewById(R.id.friend_message_button);
        message_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switchToMessage();
            }
        });
    }

    private void switchToMessage(){
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("user_email", this.userEmail);
        intent.putExtra("friend_email", friend_name.getText());
        startActivity(intent);
    }

    public void getData() {
        this.documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
