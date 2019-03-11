package edu.ucsd.cse110.personalbest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FriendListActivity extends AppCompatActivity {

    final String COLLECTION_KEY     = "users";
    final String REQ_KEY            = "Requested friends";
    final String LIST_KEY           = "Friend list";
    final String REQ_EMAIL_KEY      = "Requested Email";
    final String FRIEND_EMAIL_KEY   = "Friend's Email";

    CollectionReference users;
    DocumentReference   selfDoc;
    CollectionReference selfRequestList;
    CollectionReference selfFriendList;

    DocumentReference   otherDoc;
    CollectionReference otherRequestList;
    CollectionReference otherFriendList;

    String tmpStringFriend  = "xig113@ucsd.edu";//For test
    String tmpStringRequest = "ziw330@ucsd.edu";//For test
    String inputEmail;
    String s;
    String userEmail;

    private boolean isList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        userEmail = getIntent().getSerializableExtra("email").toString();

        users = FirebaseFirestore.getInstance().collection(COLLECTION_KEY);
        selfDoc = users.document( userEmail );
        selfRequestList = selfDoc.collection(REQ_KEY);
        selfFriendList = selfDoc.collection(LIST_KEY);

        Map<String, String> reqEmail = new HashMap<>();
        reqEmail.put(REQ_EMAIL_KEY, "ziw330@ucsd.edu");
        selfRequestList.add(reqEmail);

        Map<String, String> friEmail = new HashMap<>();
        friEmail.put(FRIEND_EMAIL_KEY, "xig113@ucsd.edu");
        selfFriendList.add(friEmail);

        Button addFriend = (Button) findViewById(R.id.add_friend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog("Add new friend", "Enter your new friend's email:");
            }
        });

        Button goBack = (Button) findViewById(R.id.back);
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        Button friend1 = (Button) findViewById(R.id.friend1);
        friend1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switchToFriend();
            }
        });

    }

    private void switchToFriend(){
        Intent intent = new Intent( this, ProfileActivity.class);
        intent.putExtra("email", userEmail);
        startActivity(intent);
    }

    private void promptDialog ( String title, String message ){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input as integer;
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        // Set up the positive button ("Yes" button)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                inputEmail = input.getText().toString();

                otherDoc = users.document( inputEmail );
                otherRequestList = otherDoc.collection(REQ_KEY);
                otherFriendList = otherDoc.collection(LIST_KEY);

                if( checkList( selfFriendList, inputEmail ) ) {
                    Toast.makeText(FriendListActivity.this, "You have added this friend!", Toast.LENGTH_LONG).show();

                } else if( checkList( selfRequestList, inputEmail ) ) {
                    Toast.makeText(FriendListActivity.this, "You have requested!", Toast.LENGTH_LONG).show();

                } else {
                    requestFriend( inputEmail);
                }

            }
        });

        // set the negative button ("No" button)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // show the dialog
        builder.show();
    }

    private boolean checkList ( CollectionReference list, String checkEmail ) {

        if( list.getId() == REQ_KEY ) {
            s = REQ_EMAIL_KEY;
        } else if ( list.getId() == LIST_KEY ) {
            s = FRIEND_EMAIL_KEY;
        }

        //Get the email in friend list
        list.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String TAG = "";
                if (task.isSuccessful()){ //Find email in selfRequestList list
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        if(checkEmail.equals(document.getString( s ))) {
                            isList = true;
                            break;
                        }
                    }
                }
            }
        });
        return isList;
    }

    private void requestFriend ( String email ) {

        if ( checkList( otherRequestList, userEmail ) ) {
            // remove request + add friend

        } else { //meiyou
            // add self request
        }


        Map<String, String> reqEmail = new HashMap<>();
        reqEmail.put(REQ_EMAIL_KEY, email);
        selfRequestList.add(reqEmail);
    }
}
