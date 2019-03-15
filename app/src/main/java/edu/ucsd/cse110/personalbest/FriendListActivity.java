package edu.ucsd.cse110.personalbest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class FriendListActivity extends AppCompatActivity implements IcheckList{
    public static final String TAG = "FriendListActivity";

    public static final String COLLECTION_KEY     = "Users";
    public static final String REQ_KEY            = "Request list";
    public static final String LIST_KEY           = "Friend list";
    public static final String REQ_EMAIL_KEY      = "Requested Email";
    public static final String FRIEND_EMAIL_KEY   = "Friend's Email";

    private int current_button_location = 150;

    CollectionReference users;
    DocumentReference   selfDoc;
    CollectionReference selfRequestList;
    CollectionReference selfFriendList;

    DocumentReference   otherDoc;
    CollectionReference otherRequestList;
    CollectionReference otherFriendList;

    String inputEmail;
    String s;
    String userEmail;
    String docID;

    private boolean isList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        userEmail = getIntent().getSerializableExtra("user_email").toString();

        FirebaseApp.initializeApp(this);

        users = FirebaseFirestore.getInstance().collection(COLLECTION_KEY);
        if (userEmail.equals("")) {
            selfDoc = users.document( "default" );
        } else {
            selfDoc = users.document( userEmail );
        }
        selfRequestList = selfDoc.collection(REQ_KEY);
        selfFriendList = selfDoc.collection(LIST_KEY);

        initFriendUpdateListener();

        Button addFriend = (Button) findViewById(R.id.add_friend_button);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog("Add new friend", "Enter your new friend's email:");
            }
        });

        Button goBack = (Button) findViewById(R.id.friend_list_back_button);
        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        Log.d( TAG, "@@@@@@@@@@@ finished oncreate method @@@@@@@@@@");
    }

    private void initFriendUpdateListener() {
        selfFriendList.addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();

                        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.friend_list_layout);
                        documentChanges.forEach(change -> {
                            QueryDocumentSnapshot document = change.getDocument();

                            String friend_email = document.get(FRIEND_EMAIL_KEY).toString();
                            Button friend_button = new Button(this);
                            relativeLayout.addView(friend_button);
                            friend_button.setText(friend_email);
                            friend_button.setWidth(800);
                            setMargins(friend_button, 0, 0, 0);
                            friend_button.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    switchToFriend(friend_email);
                                }
                            });
                        });
                    }
                });
    }

    private void setMargins (View view, int left, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, current_button_location, right, bottom);
            current_button_location += 100;
            view.requestLayout();
        }
    }

    private void switchToFriend(String friend_email){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user_email", this.userEmail);
        intent.putExtra("friend_email", friend_email);
        startActivity(intent);
    }

    private void promptDialog ( String title, String message ){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                inputEmail = input.getText().toString();

                otherDoc = users.document( inputEmail );
                otherRequestList = otherDoc.collection(REQ_KEY);
                otherFriendList = otherDoc.collection(LIST_KEY);

                if( inputEmail == userEmail ) {
                    toast("You couldn't add yourself!");
                }
                checkList( selfFriendList, inputEmail, FriendListActivity.this );

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void checkList ( CollectionReference list, String checkEmail, IcheckList check ) {
        isList = false;

        if( list.getId() == REQ_KEY ) {
            s = REQ_EMAIL_KEY;
            // Log.d(TAG,"@@@@@@@@@@@@@@@@@ check request list @@@@@@@@@@@@@@" + checkEmail);
        } else if ( list.getId() == LIST_KEY ) {
            s = FRIEND_EMAIL_KEY;
            // Log.d(TAG,"@@@@@@@@@@@@@@@@@@@@ check friend list @@@@@@@@@@@@@" + checkEmail);
        }

        list.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d(TAG, "checkEmail:" + checkEmail + "@@@@@@@@@@@@");
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Log.d(TAG, document.getString(s)+"@@@@@@@@@@@@");

                        if(checkEmail.equals(document.getString( s ))) {
                            isList = true;
                            if( list.getId() == REQ_KEY ) {
                                docID = document.getId();
                            }
                            // Log.d(TAG,s+"@@@@@@@ found  true @@@@@@@" +isList+"@@@@@" + list.getParent().getId() + s + checkEmail );

                            if(list.getParent().getId() == inputEmail ) {
                                check.addFriend();
                            } else if( list.getParent().getId() == userEmail ) {
                                if( list.getId() == LIST_KEY ) {
                                    check.toast("You have added this friend!");
                                } else if( list.getId() == REQ_KEY ) {
                                    check.toast("You have requested!");
                                }
                            }
                            break;
                        }
                    }
                }
                if(!isList && list.getParent().getId() == userEmail ) {
                    if(list.getId() == LIST_KEY) {
                        checkList( selfRequestList, inputEmail, FriendListActivity.this );
                    } else if(list.getId() == REQ_KEY) {
                        checkList(otherRequestList, userEmail, FriendListActivity.this);
                    }
                }
                if(!isList && list.getParent().getId() == inputEmail) {
                    check.addRequest();
                }
            }
        });
        Log.d(TAG, "finish check list method" +isList+"@@@@@" + list.getParent().getId() + s + checkEmail +"@@@@@@@@@@");
    }

    public void addFriend ( ) {
        DocumentReference reqDoc;

        // Log.d(TAG,"@@@@@@@@@@@@ successfully added @@@@@@@@@@");

        reqDoc = otherRequestList.document(docID);
        reqDoc.delete();

        Map<String, String> friend1 = new HashMap<>();
        friend1.put( FRIEND_EMAIL_KEY, userEmail );
        otherFriendList.add(friend1);

        Map<String, String> friend2 = new HashMap<>();
        friend2.put( FRIEND_EMAIL_KEY, inputEmail );
        selfFriendList.add(friend2);

        Toast.makeText(FriendListActivity.this, "Successfully added!", Toast.LENGTH_LONG).show();

    }
    public void addRequest(){
        // Log.d(TAG, "@@@@@@@@@ Didn't add @@@@@@@@@@@");
        Map<String, String> req = new HashMap<>();
        req.put( REQ_EMAIL_KEY, inputEmail );
        selfRequestList.add(req);

        Toast.makeText(FriendListActivity.this, "Wait for approval", Toast.LENGTH_LONG).show();
    }

    public void toast ( String message ) {
        Toast.makeText(FriendListActivity.this, message, Toast.LENGTH_LONG).show();
    }

}
