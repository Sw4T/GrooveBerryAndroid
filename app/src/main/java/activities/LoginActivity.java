package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import highdevs.grooveberryandroid.R;
import services.SocketHandlerService;

public class LoginActivity extends Activity {

    private EditText ET_Login;
    private ImageButton BTN_Login;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        ET_Login = findViewById(R.id.ET_GrooveBerryAddress);
        BTN_Login = findViewById(R.id.BTN_LoginGrooveberry);
        BTN_Login.setOnClickListener(loginListener);
    }

    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String url = ET_Login.getText().toString();
            if (!haveNetworkConnection()) {
                Toast.makeText(context, "Your internet seems off, you have to turn it on to use the app", Toast.LENGTH_SHORT).show();
            }
            else if (!url.equals("") && url.contains((".")))
            {
                Intent startTheGrooveIntent = new Intent(context, SocketHandlerService.class);
                startTheGrooveIntent.putExtra("grooveberry-url", url);
                startService(startTheGrooveIntent);
            }
        }
    };

    private boolean haveNetworkConnection()
    {
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
