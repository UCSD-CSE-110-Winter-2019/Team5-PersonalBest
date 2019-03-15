package edu.ucsd.cse110.personalbest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.cse110.personalbest.chatmessage.ChatMessageService;
import edu.ucsd.cse110.personalbest.notification.NotificationService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class MessageHistoryTest {
    private String from;
    private EditText userNameView;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
        sharedPreferences = context.getSharedPreferences(MessageActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("from", "test").apply();
        from = sharedPreferences.getString("from", null);

        Intent intent = TestUtils.getMessageActivityIntent(mock(ChatMessageService.class), mock(NotificationService.class));
        MessageActivity activity = Robolectric.buildActivity(MessageActivity.class, intent).create().get();
        userNameView = activity.findViewById(R.id.user_name);
    }

    @Test
    public void readUserNameOnStart() {
        assertEquals(from, userNameView.getText().toString());
    }

    @Test
    public void saveUserNameOnEdit() {
        String userName = userNameView.getText().toString();
        assertEquals(from, userName);

        userNameView.setText(userName + " successful");

        String newFrom = sharedPreferences.getString("from", null);
        assertEquals(userName + " successful", newFrom);
    }
}