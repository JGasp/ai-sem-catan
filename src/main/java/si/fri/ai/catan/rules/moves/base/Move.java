package si.fri.ai.catan.rules.moves.base;

import si.fri.ai.catan.State;

public abstract class Move {

    public abstract void make(State state, int playerIndex);

}
