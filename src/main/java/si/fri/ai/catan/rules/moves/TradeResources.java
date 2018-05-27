package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

public class TradeResources extends Move {

    private ResourceType tradeIn;
    private ResourceType tradeOut;

    private int ratio;

    public TradeResources(Game game, int playerIndex, ResourceType tradeIn, ResourceType tradeOut, int ratio) {
        super(game, playerIndex);

        this.tradeIn = tradeIn;
        this.tradeOut = tradeOut;
        this.ratio = ratio;
    }

    @Override
    public void make(State state) {

        subResource(state, tradeIn, ratio);

        switch (tradeOut) {
            case CLAY:
                state.addClay(playerIndex, 1);
                break;
            case IRON:
                state.addIron(playerIndex, 1);
                break;
            case SHEEP:
                state.addSheep(playerIndex, 1);
                break;
            case WHEAT:
                state.addWheat(playerIndex, 1);
                break;
            case WOOD:
                state.addWood(playerIndex, 1);
                break;
        }

    }
}
