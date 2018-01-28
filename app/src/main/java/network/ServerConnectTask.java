package network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

/**
 * Task which creates the socket with the remote server
 */
public class ServerConnectTask extends AsyncTask<String, Void, SocketCommands> {

    public static final int PORT = 3008;
    private Context context;

    public ServerConnectTask(Context context) {
        this.context = context;
    }

    @Override
    protected SocketCommands doInBackground(String... url)
    {
        if (haveNetworkConnection())
        {
            try {
                return new SocketCommands(url[0], PORT);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        else {
            Toast.makeText(context, "Your internet seems off, you have to turn it on to use the app", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
