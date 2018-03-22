package network;

import android.os.AsyncTask;
import java.io.IOException;
import data.GrooveBerryCommands;

public class SendCommandTask extends AsyncTask<GrooveBerryCommands, Void, Object> {

    private GrooveBerrySocket socket;

    public SendCommandTask(GrooveBerrySocket socket) {
        this.socket = socket;
    }

    @Override
    protected Object doInBackground(GrooveBerryCommands... grooveBerryCommands)
    {
        GrooveBerryCommands commmandToSend = grooveBerryCommands[0];
        try {
            if (commmandToSend == GrooveBerryCommands.GET_READING_QUEUE)
            {
                return (socket.getReadingQueue());
            } else if (commmandToSend == GrooveBerryCommands.NEXT || commmandToSend == GrooveBerryCommands.PREV) {
                socket.sendCommand(commmandToSend);
                socket.read();
                return (socket.getReadingQueue());
            } else {
                socket.sendCommand(commmandToSend);
                return (socket.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
