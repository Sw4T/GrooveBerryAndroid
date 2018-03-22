package network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import data.GrooveBerryCommands;

/**
 * Created by Pierre on 15/01/2018.
 */

public class GrooveBerrySocket extends Socket  {

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    //private ObjectInputStream objectInput;

    /**
     * Creates a new socket and connect it to the remote <a href="https://github.com/Seikomi/GrooveBerry">GrooveBerry server</a>.<br/>
     * Buffers are initialized within this constructor.
     * @param serverURL IP or address of the remote server
     * @param port Port used by the server application
     * @throws IOException
     */
    public GrooveBerrySocket(String serverURL, int port) throws IOException {
        super(serverURL, port);
        this.inputStream = new DataInputStream(this.getInputStream());
        this.outputStream = new DataOutputStream(this.getOutputStream());
        //this.objectInput = new ObjectInputStream(this.getInputStream());
    }

    /**
     * Writes a command to the output buffer.
     * @param command One of {@link GrooveBerryCommands} to send to the server
     */
    public void sendCommand(GrooveBerryCommands command)
    {
        try {
            this.outputStream.writeUTF(command.getCommand());
            this.outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read()
    {
        try {
            return this.inputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getReadingQueue() throws IOException
    {
        ArrayList<String> readingQueue = new ArrayList<>();
        this.outputStream.writeUTF(GrooveBerryCommands.GET_READING_QUEUE.getCommand());
        String readingQueueUnparsed = this.inputStream.readUTF();

        String[] listReadingQueueFiles = readingQueueUnparsed.split("\n");
        for (String audioFile : listReadingQueueFiles)
        {
            if (!audioFile.contains(";"))
                continue;

            String[] audioFileInformations = audioFile.split(";");
            String fileName = audioFileInformations[0];
            if (audioFileInformations[1].equals("1")) {
                readingQueue.add("=> " + fileName);
            } else {
                readingQueue.add(fileName);
            }
        }
        return readingQueue;
    }

    public void close()
    {
        try {
            this.outputStream.writeUTF(GrooveBerryCommands.EXIT.getCommand());
            this.outputStream.close();
            this.inputStream.close();
            super.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
