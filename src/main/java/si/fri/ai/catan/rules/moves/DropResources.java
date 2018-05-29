package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class DropResources extends Move {

    private ResourceType type;
    private int amount;

    public DropResources(int playerIndex, ResourceType type, int amount) {
        super(playerIndex);
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void make(Game game, State state) {
        state.subResource(playerIndex, type, (byte) amount);
    }


    @Override
    public String toString() {
        return "DropResources{" +
                "amount=" + amount +
                ", playerIndex=" + playerIndex +
                '}';
    }
}
