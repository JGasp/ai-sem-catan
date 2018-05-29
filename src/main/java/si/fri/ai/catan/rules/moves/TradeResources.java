package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class TradeResources extends Move {

    private ResourceType tradeIn;
    private ResourceType tradeOut;

    private int ratio;

    public TradeResources(int playerIndex, ResourceType tradeIn, ResourceType tradeOut, int ratio) {
        super(playerIndex);

        this.tradeIn = tradeIn;
        this.tradeOut = tradeOut;
        this.ratio = ratio;
    }

    @Override
    public void make(Game game, State state) {
        state.subResource(playerIndex, tradeIn, (byte) ratio);
        state.addResource(playerIndex, tradeOut, (byte) 1);
    }

    @Override
    public String toString() {
        return "TradeResources{" +
                "tradeIn=" + tradeIn +
                ", tradeOut=" + tradeOut +
                ", ratio=" + ratio +
                ", playerIndex=" + playerIndex +
                '}';
    }
}
