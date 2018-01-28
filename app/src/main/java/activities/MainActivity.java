package activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.concurrent.ExecutionException;

import data.GrooveBerryCommands;
import highdevs.grooveberryandroid.R;
import network.SendCommandTask;
import network.SocketCommands;

public class MainActivity extends AppCompatActivity {

    ImageButton BT_Shuffle, BT_Play, BT_Pause, BT_Prev, BT_Next;

    SocketCommands socketCommands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initXmlElements();
        Intent intent = getIntent();
        socketCommands = (SocketCommands) intent.getExtras().getSerializable("socket");
        Log.d("c nul", ( socketCommands == null ? "PAS LEGAL" : " TRES LEGAL"));
    }

    private void initXmlElements()
    {
        BT_Next = (ImageButton) findViewById(R.id.BTN_Command_Next);
        BT_Play = (ImageButton) findViewById(R.id.BTN_Command_Play);
        BT_Prev = (ImageButton) findViewById(R.id.BTN_Command_Previous);
        BT_Pause = (ImageButton) findViewById(R.id.BTN_Command_Pause);
        BT_Shuffle = (ImageButton) findViewById(R.id.BTN_Command_Shuffle);
        ImageButton[] Buttons = new ImageButton[] { BT_Pause, BT_Next, BT_Play, BT_Shuffle, BT_Prev };
        for (ImageButton button : Buttons) {
            button.setOnClickListener(sendCommandListener);
        }
    }

    private View.OnClickListener sendCommandListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendCommandToRemote(view.getTooltipText().toString());
            } else
                sendCommandToRemote(view.getTag().toString());
        }
    };

    private void sendCommandToRemote(String commandToSend)
    {
        GrooveBerryCommands command = null;
        switch (commandToSend)
        {
            case "Next" : command = GrooveBerryCommands.NEXT; break;
            case "Play" : command = GrooveBerryCommands.PLAY; break;
            case "Shuffle" : command = GrooveBerryCommands.GET_READING_QUEUE; break; //TODO
            case "Previous" : command = GrooveBerryCommands.PREV; break;
            case "Pause" : command = GrooveBerryCommands.PAUSE; break;
            default:
        }
        SendCommandTask sendTask = new SendCommandTask(socketCommands);
        try {
            Object taskReturn = sendTask.execute(command).get();
            Log.d("TaskReturn", taskReturn.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
