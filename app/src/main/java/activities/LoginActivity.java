package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import network.ServerConnectTask;
import network.SocketCommands;
import highdevs.grooveberryandroid.R;

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
            if (!url.equals("") && url.contains((".")))
            {
                try {
                    SocketCommands socket = new ServerConnectTask(context).execute(url).get();
                    Intent startTheGrooveIntent = new Intent(context, MainActivity.class);
                    startTheGrooveIntent.putExtra("socket", socket);
                    startActivity(startTheGrooveIntent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, "Failed to connect to GrooveBerry server...", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };
}
