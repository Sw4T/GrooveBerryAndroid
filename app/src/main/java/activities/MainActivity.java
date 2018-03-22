package activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import data.GrooveBerryCommands;
import highdevs.grooveberryandroid.R;
import network.SendCommandTask;
import network.GrooveBerrySocket;
import services.SocketHandlerService;

public class MainActivity extends AppCompatActivity {

    ImageButton BT_Shuffle, BT_Play, BT_Pause, BT_Prev, BT_Next;
    ListView LV_ReadingQueue;

    GrooveBerrySocket grooveBerrySocket;
    boolean serviceIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initXmlElements();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, SocketHandlerService.class);
        bindService(mIntent, mConnection, BIND_ADJUST_WITH_ACTIVITY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(serviceIsBound) {
            unbindService(mConnection);
            serviceIsBound = false;
        }
    }

    private void initXmlElements()
    {
        LV_ReadingQueue = findViewById(R.id.LV_ReadingQueue);
        BT_Next = findViewById(R.id.BTN_Command_Next);
        BT_Play = findViewById(R.id.BTN_Command_Play);
        BT_Prev = findViewById(R.id.BTN_Command_Previous);
        BT_Pause = findViewById(R.id.BTN_Command_Pause);
        BT_Shuffle = findViewById(R.id.BTN_Command_Get_Queue);
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
            case "ReadingQueue" : command = GrooveBerryCommands.GET_READING_QUEUE; break;
            case "Previous" : command = GrooveBerryCommands.PREV; break;
            case "Pause" : command = GrooveBerryCommands.PAUSE; break;
            default:
        }
        SendCommandTask sendTask = new SendCommandTask(grooveBerrySocket);
        Log.v("SendCommandTask", "Executing command " + commandToSend);
        try {
            Object taskReturn = sendTask.execute(command).get();
            if (taskReturn != null && (command == GrooveBerryCommands.GET_READING_QUEUE ||
                                       command == GrooveBerryCommands.NEXT ||
                                       command == GrooveBerryCommands.PREV)) {
                fillReadingQueue((ArrayList<String>) taskReturn);
            } else {
                Log.d("GrooveBerryServer says", taskReturn.toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceIsBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceIsBound = true;
            SocketHandlerService.SocketBinder binder = (SocketHandlerService.SocketBinder) service;
            SocketHandlerService socketHandlerService = binder.getService();
            grooveBerrySocket = socketHandlerService.getGrooveBerrySocket();
            Log.v("SocketHandlerService", "Service is running...");
        }
    };

    private void fillReadingQueue(ArrayList<String> readingQueue) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                readingQueue);
        LV_ReadingQueue.setAdapter(arrayAdapter);

        int position = 0;
        for (String fileName : readingQueue) {
            if (fileName.contains(("=>"))) {
                LV_ReadingQueue.setSelection(position);
                break;
            }
            position++;
        }
    }
}
