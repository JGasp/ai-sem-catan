package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class TradeResources extends Move {

    private ResourceType tradeIn;
    private ResourceType tradeOut;

    private int ratio;

    public TradeResources(ResourceType tradeIn, ResourceType tradeOut, int ratio) {
        this.tradeIn = tradeIn;
        this.tradeOut = tradeOut;
        this.ratio = ratio;
    }

    @Override
    public void make(State state, int playerIndex) {

    }
}
