package edu.ucsd.cse110.personalbest;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.cse110.personalbest.chatmessage.ChatMessageService;
import edu.ucsd.cse110.personalbest.notification.NotificationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class NotificationTest {

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
    }

    @Test
    public void subscribeToCorrectTopic() {
        NotificationService notificationService = mock(NotificationService.class);

        Intent intent = TestUtils.getMessageActivityIntent(mock(ChatMessageService.class), notificationService);
        MessageActivity activity = Robolectric.buildActivity(MessageActivity.class, intent).create().get();

        verify(notificationService).subscribeToNotificationsTopic(eq("chat1"), any());
    }
}
