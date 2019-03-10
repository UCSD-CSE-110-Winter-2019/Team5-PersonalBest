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
    final String TAG = "FriendListActivity";
    final String COLLECTION_KEY     = "Users";
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
    String docID;

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
            Log.d(TAG,"@@@@@@@@@@@@@@@@@ check request list @@@@@@@@@@@@@@@@@@@@@@");
        } else if ( list.getId() == LIST_KEY ) {
            s = FRIEND_EMAIL_KEY;
            Log.d(TAG,"@@@@@@@@@@@@@@@@@@@@ check friend list @@@@@@@@@@@@@@@@@");
        }

        System.out.println(list.getId());
        System.out.println(s);

        //Get the email in friend list
        list.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){ //Find email in selfRequestList list
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, document.getId() + " => " + document.getData());

                        System.out.println(document.getString(s));

                        if(checkEmail.equals(document.getString( s ))) {
                            Log.d(TAG,"@@@@@@@@@@@@ found same email return true @@@@@@@");
                            isList = true;
                            if( list.getId() == REQ_KEY ) {
                                docID = document.getId();
                            }
                            break;
                        }
                    }
                }
            }
        });
        return isList;
    }

    private void requestFriend ( String email ) {
        DocumentReference reqDoc;
        if ( checkList( otherRequestList, userEmail ) ) {
            Log.d(TAG,"@@@@@@@@@@@@ successfully added @@@@@@@@@@");

            reqDoc = otherRequestList.document(docID);
            reqDoc.delete();

            Map<String, String> friend1 = new HashMap<>();
            friend1.put( FRIEND_EMAIL_KEY, userEmail );
            otherFriendList.add(friend1);

            Map<String, String> friend2 = new HashMap<>();
            friend2.put( FRIEND_EMAIL_KEY, inputEmail );
            selfFriendList.add(friend2);

            Toast.makeText(FriendListActivity.this, "Successfully added!", Toast.LENGTH_LONG).show();

        } else {
            Map<String, String> req = new HashMap<>();
            req.put( FRIEND_EMAIL_KEY, inputEmail );
            selfRequestList.add(req);

            Toast.makeText(FriendListActivity.this, "Wait for approval", Toast.LENGTH_LONG).show();

        }
    }
}
