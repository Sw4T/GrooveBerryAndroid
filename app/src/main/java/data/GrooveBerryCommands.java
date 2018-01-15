package data;

public enum GrooveBerryCommands {

    DOWNLOAD("#DOWNLOAD"),
    UPLOAD("#UPLOAD"),
    NEXT("#NEXT"),
    PREV("#PREV"),
    PLAY("#PLAY"),
    PAUSE("#PAUSE"),
    STOP("#STOP"),
    VOLUME_UP("#VOLUP"),
    VOLUME_DOWN("#VOLDOWN"),
    CURRENT_SONG("#SONG"),
    GET_READING_QUEUE("#LIST"),
    EXIT("#EXIT");

    private String command;

    GrooveBerryCommands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

}