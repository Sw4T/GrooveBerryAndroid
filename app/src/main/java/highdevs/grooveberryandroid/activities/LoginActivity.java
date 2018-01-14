package highdevs.grooveberryandroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import highdevs.grooveberryandroid.R;

public class LoginActivity extends Activity {

    private EditText ET_Login;
    private ImageButton BTN_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ET_Login = findViewById(R.id.ET_GrooveBerryAddress);
        BTN_Login = findViewById(R.id.BTN_LoginGrooveberry);
        BTN_Login.setOnClickListener(loginListener);
    }

    private View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("h1", "ok");
        }
    };
}
