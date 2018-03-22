package network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import activities.MainActivity;

/**
 * Task which creates the socket with the remote server
 */
public class ServerConnectTask extends AsyncTask<String, Void, GrooveBerrySocket> {

    public static final int PORT = 3008;
    private Context context;

    public ServerConnectTask(Context context) {
        this.context = context;
    }

    @Override
    protected GrooveBerrySocket doInBackground(String... url)
    {
        GrooveBerrySocket grooveBerrySocket = null;
        try {
            grooveBerrySocket = new GrooveBerrySocket(url[0], PORT);
            String str = grooveBerrySocket.read();
            Log.d("Server Welcome Message", str);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return grooveBerrySocket;
    }

    @Override
    protected void onPostExecute(GrooveBerrySocket socket)
    {
        if (socket == null) {
            Toast.makeText(context, "Failed to connect to GrooveBerry server...", Toast.LENGTH_SHORT).show();
        }
    }
}
