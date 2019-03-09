package edu.ucsd.cse110.personalbest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    String COLLECTION_KEY = "users";
    String REQ_KEY = "Requested friends";
    String LIST_KEY = "Friend list";
    String REQ_EMAIL_KEY = "Requested Email";
    String FRIEND_EMAIL_KEY = "Friend's Email";

    CollectionReference users;
    DocumentReference doc;
    CollectionReference request;
    CollectionReference friendlist;

    String tmpStringFriend = "xig113@ucsd.edu";//For test
    String tmpStringRequest = "ziw330@ucsd.edu";//For test
    String inputEmail;

    private boolean isFriend = false;
    private boolean isRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

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

                if ( !checkFriendList( inputEmail ) && !checkRequestList( inputEmail ) ) {
                    requestFriend(inputEmail);
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

    private boolean checkFriendList ( String email ) {
        //Get the email in friend list
        friendlist.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String TAG = "";
                if (task.isSuccessful()){ //Find email in request list
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        //System.out.println(document.getString("Friend's Email"));
                        //System.out.println(tmpStringFriend);
                        if(email.equals(document.getString("Friend's Email"))) {
                            //TODO toast
                            System.out.println("You have this friend");
                            isFriend = true;
                            break;
                        }
                    }
                }
            }
        });
        return isFriend;
    }

    private boolean checkRequestList ( String email ) {
        //Get email in request list
        request.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String TAG = "";
                if (task.isSuccessful()) { //Find email in request list

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        if(email.equals(document.getString("Requested Email"))){
                            //TODO toast
                            System.out.println("This person is in your request list");
                            isRequested = true;
                            break;
                        }
                    }
                }
            }
        });
        return isRequested;
    }

    private boolean checkAnotherRequestList( String email ) {
        DocumentReference tempDoc = users.document( email );
        return true;
    }

    private void requestFriend ( String email ) {
        Map<String, String> reqEmail = new HashMap<>();
        reqEmail.put(REQ_EMAIL_KEY, email);
        request.add(reqEmail);
    }
}
