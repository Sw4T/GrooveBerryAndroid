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
        if (commmandToSend == GrooveBerryCommands.GET_READING_QUEUE)
        {
            try {
                return (socket.getReadingQueue());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            socket.sendCommand(grooveBerryCommands[0]);
            return true;
        }
    }

    @Override
    protected void onPostExecute(Object result)
    {

    }


}
