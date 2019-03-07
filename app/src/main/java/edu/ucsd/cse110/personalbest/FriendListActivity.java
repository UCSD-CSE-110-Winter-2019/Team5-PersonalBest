package edu.ucsd.cse110.personalbest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FriendListActivity extends AppCompatActivity {

    String COLLECTION_KEY = "kk";
    String REQ_KEY = "Requested friends";
    String LIST_KEY = "Friend list";
    String REQ_EMAIL_KEY = "Requested Email";
    String FRIEND_EMAIL_KEY = "Friend's Email";

    CollectionReference users;
    DocumentReference doc;
    CollectionReference request;
    CollectionReference friendlist;

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
        doc = users.document(MainActivity.user.getEmailAddress());
        request = doc.collection(REQ_KEY);
        friendlist = doc.collection(LIST_KEY);

        Map<String, String> reqEmail = new HashMap<>();
        reqEmail.put(REQ_EMAIL_KEY, "ziw330@ucsd.edu");
        request.add(reqEmail);

        Map<String, String> friEmail = new HashMap<>();
        friEmail.put(FRIEND_EMAIL_KEY, "xig113@ucsd.edu");
        friendlist.add(friEmail);
    }
}
