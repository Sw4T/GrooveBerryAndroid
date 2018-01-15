package network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

import data.GrooveBerryCommands;

/**
 * Created by Pierre on 15/01/2018.
 */

public class GrooveBerrySocket extends Socket implements Serializable {

    private BufferedReader bufferReader;
    private PrintWriter printWriter;
    private ObjectInputStream objectInput;

    public GrooveBerrySocket(String serverURL, int port) throws IOException {
        super(serverURL, port);
        this.printWriter = new PrintWriter(this.getOutputStream(), true);
        this.bufferReader = new BufferedReader(new InputStreamReader(this.getInputStream()));
        this.objectInput = new ObjectInputStream(this.getInputStream());
    }

    public void sendCommand(GrooveBerryCommands command)
    {
        this.printWriter.println(command.getCommand());
    }

    public HashMap<Integer, String> getReadingQueue() throws IOException
    {
        this.printWriter.println(GrooveBerryCommands.GET_READING_QUEUE);
        String str;
        while ((str = this.bufferReader.readLine()) != null)
            Log.d("DebugNetworkReceived", str);
        return null;
    }

    public void close()
    {
        this.printWriter.println(GrooveBerryCommands.EXIT);
        try {
            this.objectInput.close();
            this.bufferReader.close();
            this.printWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
