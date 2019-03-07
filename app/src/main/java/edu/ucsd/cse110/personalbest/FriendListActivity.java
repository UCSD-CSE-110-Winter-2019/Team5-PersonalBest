package edu.ucsd.cse110.personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendListActivity extends AppCompatActivity {

    String COLLECTION_KEY = "users";

    CollectionReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        Button goBack = (Button) findViewById(R.id.back);

        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        users = FirebaseFirestore.getInstance().collection(COLLECTION_KEY);
        users.document(MainActivity.user.getEmailAddress());
    }
}
