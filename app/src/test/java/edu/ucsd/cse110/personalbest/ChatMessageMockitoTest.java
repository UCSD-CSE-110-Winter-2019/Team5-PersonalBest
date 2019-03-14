package edu.ucsd.cse110.personalbest;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import edu.ucsd.cse110.personalbest.chatmessage.ChatMessageService;
import edu.ucsd.cse110.personalbest.chatmessage.FirebaseFirestoreAdapter;
import edu.ucsd.cse110.personalbest.notification.NotificationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class ChatMessageMockitoTest {
    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
    }

    @Test
    public void messagesDisplayedInOrder() {
//        set up mocks
        CollectionReference chat = mock(CollectionReference.class);
        Query q = mock(Query.class);
        when(chat.orderBy(anyString(), any())).thenReturn(q);
        ChatMessageService messageService = new FirebaseFirestoreAdapter(chat);

        Intent intent = TestUtils.getMessageActivityIntent(messageService, mock(NotificationService.class));
        MessageActivity activity = Robolectric.buildActivity(MessageActivity.class, intent).create().get();

//        do verification on CollectionReference mock: https://stackoverflow.com/questions/9841623/mockito-how-to-verify-method-was-called-on-an-object-created-within-a-method
        verify(chat).orderBy("timestamp", Query.Direction.ASCENDING);
    }
}
