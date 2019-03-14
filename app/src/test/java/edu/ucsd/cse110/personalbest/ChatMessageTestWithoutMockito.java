package edu.ucsd.cse110.personalbest;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.personalbest.chatmessage.ChatMessage;

import static org.junit.Assert.assertEquals;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class ChatMessageTestWithoutMockito {

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
    }

    @Test
    public void messagesDisplayedInOrder() {
        List<ChatMessage> m = new ArrayList<>();
        m.add(new ChatMessage("User1", "Hi there"));
        m.add(new ChatMessage("User1", "How are you doing?"));
        m.add(new ChatMessage("User2", "Good, how are you?"));

        Intent intent = TestUtils.getMessageActivityIntent(TestUtils.getChatMessageService(m), TestUtils.getNotificationService("chat1"));
        MessageActivity activity = Robolectric.buildActivity(MessageActivity.class, intent).create().get();

        TextView chat = activity.findViewById(R.id.chat);

        StringBuilder sb = new StringBuilder();
        m.forEach(message -> sb.append(message.toString()));
        assertEquals(sb.toString(), chat.getText().toString());
    }

}