package si.fri.ai.catan.dto;

import java.awt.*;

public class InfoMessage {

    private String message;
    private int playerIndex = -1;

    public InfoMessage(String message, int playerIndex) {
        this.message = message;
        this.playerIndex = playerIndex;
    }

    public InfoMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
