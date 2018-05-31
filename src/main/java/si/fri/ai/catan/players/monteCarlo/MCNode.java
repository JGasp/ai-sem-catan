package si.fri.ai.catan.players.monteCarlo;

import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.TradeResources;
import si.fri.ai.catan.rules.moves.base.Move;

import java.util.Objects;

public class MCNode {

    private Move move;
    private int hashCode;

    public MCNode(Move move, State state) {
        this.move = move;

        if(move == null) {
            this.hashCode = Objects.hash(11, state.hashCode());
        } else if(move instanceof TradeResources) {
            this.hashCode = Objects.hash(17, state.hashCode());
        } else {
            this.hashCode = Objects.hash(state.hashCode(), move.hashCode());
        }
    }

    public Move getMove() {
        return move;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MCNode) {
            MCNode that = (MCNode) obj;
            if(move != null && that.move != null) {
                return move.equals(that.move);
            }
        }
        return false;
    }
}
