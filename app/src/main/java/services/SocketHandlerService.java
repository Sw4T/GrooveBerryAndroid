package services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import activities.MainActivity;
import network.GrooveBerrySocket;
import network.ServerConnectTask;

/**
 * Created by Pierre on 22/03/2018.
 */

public class SocketHandlerService extends Service {

    public static final int PORT = 3008;
    private final IBinder mBinder = new SocketBinder();
    private GrooveBerrySocket grooveBerrySocket;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        String serverURL = extras.getString("grooveberry-url");
        if (this.grooveBerrySocket == null && serverURL != null) {
            try {
                this.grooveBerrySocket = new ServerConnectTask(getApplicationContext()).execute(serverURL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (this.grooveBerrySocket != null) {
                Intent startMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMainActivity);
            }
        }
        return Service.START_NOT_STICKY;
    }

    public GrooveBerrySocket getGrooveBerrySocket() {
        return this.grooveBerrySocket;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class SocketBinder extends Binder {
        public SocketHandlerService getService() {
            return SocketHandlerService.this;
        }
    }
}
