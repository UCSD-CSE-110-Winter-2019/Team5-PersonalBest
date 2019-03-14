package edu.ucsd.cse110.personalbest;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.robolectric.RuntimeEnvironment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import edu.ucsd.cse110.personalbest.chatmessage.ChatMessage;
import edu.ucsd.cse110.personalbest.chatmessage.ChatMessageService;
import edu.ucsd.cse110.personalbest.chatmessage.ChatMessageServiceFactory;
import edu.ucsd.cse110.personalbest.notification.NotificationService;
import edu.ucsd.cse110.personalbest.notification.NotificationServiceFactory;

public class TestUtils {
    public static ChatMessageService getChatMessageService(List<ChatMessage> m) {
        return new ChatMessageService() {
            @Override
            public Task<?> addMessage(Map<String, String> data) {
                return new Task<Object>() {
                    @Override
                    public boolean isComplete() {
                        return true;
                    }

                    @Override
                    public boolean isSuccessful() {
                        return true;
                    }

                    @Override
                    public boolean isCanceled() {
                        return false;
                    }

                    @Nullable
                    @Override
                    public Object getResult() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public <X extends Throwable> Object getResult(@NonNull Class<X> aClass) throws X {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Exception getException() {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Object> addOnSuccessListener(@NonNull OnSuccessListener<? super Object> onSuccessListener) {
                        onSuccessListener.onSuccess(true);
                        return this;
                    }

                    @NonNull
                    @Override
                    public Task<Object> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Object> onSuccessListener) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Object> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super Object> onSuccessListener) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Object> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Object> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Task<Object> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                        return null;
                    }
                };
            }

            @Override
            public void addOrderedMessagesListener(Consumer<List<ChatMessage>> listener) {
                listener.accept(m);
            }
        };
    }

    public static NotificationService getNotificationService(String expectedTopic) {
        return (topic, callback) -> {
            if (!expectedTopic.equals(topic)) {
                throw new IllegalArgumentException("Exptected \"" + expectedTopic + "\", was: \"" + topic + "\"");
            }
        };
    }

    public static Intent getMessageActivityIntent(ChatMessageService chatMessageService, NotificationService notificationService) {
        String testChatMessageServiceKey = "test chat service";
        ChatMessageServiceFactory.getInstance().put(testChatMessageServiceKey, () -> chatMessageService);

        String testNotificationServiceKey = "test notification service";
        NotificationServiceFactory.getInstance().put(testNotificationServiceKey, () -> notificationService);

        Intent intent = new Intent(RuntimeEnvironment.application, MessageActivity.class);
        intent.putExtra(MessageActivity.CHAT_MESSAGE_SERVICE_EXTRA, testChatMessageServiceKey);
        intent.putExtra(MessageActivity.NOTIFICATION_SERVICE_EXTRA, testNotificationServiceKey);

        return intent;
    }

}
