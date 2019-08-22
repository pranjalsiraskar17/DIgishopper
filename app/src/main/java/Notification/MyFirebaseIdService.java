package Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseIdService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        String refreshToken=s;
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            updateToken(refreshToken);
        }

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented=remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser !=null && sented.equals(firebaseUser.getUid()))
        {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                sendNewNotification(remoteMessage);
            }else
            {
                sendNotification(remoteMessage);
            }

        }
    }

    private void sendNewNotification(RemoteMessage remoteMessage) {
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int j=Integer.parseInt(user.replaceAll("[\\D]]",""));
        // Intent =new Intent(this,)

        //PendingIntent pendingIntent=PendingIntent.getActivity(this,j,)
        Uri defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NewNotification newNotification=new NewNotification(this);
        Notification.Builder builder=newNotification.getNewNotification(title,body,defaultSound,icon);
        int i=0;
        if(j>0)
        {
            i=j;
        }
        newNotification.getManager().notify(i,builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");

        RemoteMessage.Notification notification=remoteMessage.getNotification();
        int j=Integer.parseInt(user.replaceAll("[\\D]]",""));
       // Intent =new Intent(this,)

        //PendingIntent pendingIntent=PendingIntent.getActivity(this,j,)
        Uri defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound);

        NotificationManager noti=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int i=0;
        if(j>0)
        {
            i=j;
        }
        noti.notify(i,builder.build());
    }

    private void updateToken(String refreshToken){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token=new Token(refreshToken);
        reference.child(firebaseUser.getUid()).setValue(token);
    }
}
