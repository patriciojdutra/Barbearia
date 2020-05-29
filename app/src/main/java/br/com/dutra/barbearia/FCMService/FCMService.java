package br.com.dutra.barbearia.FCMService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import br.com.dutra.barbearia.Modelo.Usuario.Usuario;
import br.com.dutra.barbearia.R;
import br.com.dutra.barbearia.Telas.BatePapo.ConversaActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        final Map<String, String> data = remoteMessage.getData();

        if (data == null || data.get("sender") == null){
            return;
        }

        final Intent ii = new Intent(this, ConversaActivity.class);

        FirebaseFirestore.getInstance().collection("Usuario")
                .document(data.get("sender"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Usuario usuario = documentSnapshot.toObject(Usuario.class);

                        ii.putExtra("usuario", usuario);
                        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);

                        NotificationManager notificationManager = (NotificationManager)
                                getSystemService(Context.NOTIFICATION_SERVICE);

                        String notificationChannelId = "my_channel_id_01";

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel notificationChannel =
                                    new NotificationChannel(notificationChannelId, "My Notifications",
                                            NotificationManager.IMPORTANCE_DEFAULT);

                            notificationChannel.setDescription("Channel description");
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);

                            notificationManager.createNotificationChannel(notificationChannel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannelId);

                        builder.setAutoCancel(true)
                                .setSmallIcon(R.drawable.logo250)
                                .setContentTitle(data.get("title"))
                                .setContentText(data.get("body"))
                                .setContentIntent(pIntent);

                        notificationManager.notify(1, builder.build());
                    }
                });


    }

    @Override
    public void onNewToken(String token) {

    }
}
