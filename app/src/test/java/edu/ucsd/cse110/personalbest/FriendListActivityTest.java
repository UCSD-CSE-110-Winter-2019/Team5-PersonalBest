package edu.ucsd.cse110.personalbest;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class FriendListActivityTest {
    FriendListActivity activity;

    public static final String testUserEmail = "testUser";
    public static final String testFriendEmail = "testFriend";

    CollectionReference users;
    DocumentReference selfDoc;
    CollectionReference selfRequestList;
    CollectionReference selfFriendList;

    DocumentReference   otherDoc;
    CollectionReference otherRequestList;
    CollectionReference otherFriendList;

    @Before
    public void prep(){
        Intent intent = new Intent(RuntimeEnvironment.application, FriendListActivity.class);
        intent.putExtra("user_email", testUserEmail);

        activity = Robolectric.buildActivity(FriendListActivity.class, intent)
                .create()
                .get();

        users = FirebaseFirestore.getInstance().collection(activity.COLLECTION_KEY);

        selfDoc = users.document( testUserEmail );
        selfRequestList = selfDoc.collection(activity.REQ_KEY);
        selfFriendList = selfDoc.collection(activity.LIST_KEY);

        otherDoc = users.document( testFriendEmail );
        otherRequestList = otherDoc.collection(activity.REQ_KEY);
        otherFriendList = otherDoc.collection(activity.LIST_KEY);

    }

    @Test
    public void checkFriendPath(){

        Map<String, String> req1 = new HashMap<>();
        req1.put( activity.REQ_EMAIL_KEY, testFriendEmail );
        selfRequestList.add(req1);

        assertEquals("Users/" + testUserEmail + "/Request list", activity.selfRequestList.getPath());
        //for(QueryDocumentSnapshot document : selfRequestList.get().getResult()){
        //    assertEquals( testFriendEmail, document.getString(activity.REQ_EMAIL_KEY));
        //}
        //Map<String, String> req2 = new HashMap<>();
        //req2.put( activity.REQ_EMAIL_KEY, testUserEmail );
        //otherRequestList.add(req2);

        //assertEquals("Users/" + testFriendEmail + "/Request list", activity.otherRequestList.getPath());
        //assertEquals("/Users/" + testUserEmail + "/Friend list", activity.selfFriendList.getPath());
    }
}