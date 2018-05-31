package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.Objects;

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
        return String.format("[%d] Traded [%d] x [%s] for 1 x [%s]", playerIndex, ratio, tradeIn.name(), tradeOut.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerIndex, tradeIn, tradeOut, ratio);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TradeResources) {
            TradeResources that = (TradeResources) obj;
            return tradeIn == that.tradeIn && playerIndex == that.playerIndex && tradeOut == that.tradeOut && ratio == that.ratio;
        }
        return false;
    }
}
