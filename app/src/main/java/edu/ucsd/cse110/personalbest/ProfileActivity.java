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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "ProfileActivity";
    public static final String COLLECTION_KEY = "Users";


    private Button back_button;
    private Button message_button;

    TextView friend_name;

    String user_email;
    String friend_email;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user_email = getIntent().getSerializableExtra("user_email").toString();
        friend_email = getIntent().getSerializableExtra("friend_email").toString();
        this.documentReference = FirebaseFirestore.getInstance().collection(COLLECTION_KEY).document(user_email);

        this.getData();

        back_button = (Button) findViewById(R.id.profile_back_button);
        back_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        friend_name = findViewById(R.id.name_label);
        friend_name.setText(friend_email);

        message_button = (Button) findViewById(R.id.friend_message_button);
        message_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switchToMessage();
            }
        });
    }

    private void switchToMessage(){
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("user_email", this.user_email);
        intent.putExtra("friend_email", this.friend_email);
        startActivity(intent);
    }

    public void accessData(View view){
        Intent intent=new Intent(this,BarActivity.class);
        intent.putExtra("source",this.friend_email);
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
