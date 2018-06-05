package si.fri.ai.catan.rules.moves;

import si.fri.ai.catan.Game;
import si.fri.ai.catan.State;
import si.fri.ai.catan.rules.moves.base.Move;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.Arrays;
import java.util.Objects;

public class DropResources extends Move {

    private byte[] amounts;

    public DropResources(int playerIndex) {
        super(playerIndex);
        this.amounts = new byte[State.DIFFERENT_RESOURCES];
    }

    public DropResources(DropResources dr) {
        super(dr.playerIndex);
        this.amounts = Arrays.copyOf(dr.amounts, dr.amounts.length);
    }

    public DropResources copy() {
        return new DropResources(this);
    }

    public void add(ResourceType rt, byte amount) {
        amounts[rt.getIndex()] += amount;
    }

    public void sub(ResourceType rt, byte amount) {
        amounts[rt.getIndex()] -= amount;
    }

    public byte get(ResourceType rt) {
        return amounts[rt.getIndex()];
    }

    public int getTotalAmount() {
        int totalAmount = 0;

        for(int i=0; i<amounts.length; i++) {
            totalAmount += amounts[i];
        }

        return totalAmount;
    }



    @Override
    public void make(Game game, State state) {
        for(ResourceType rt : ResourceType.values()) {
            state.subResource(playerIndex, rt, amounts[rt.getIndex()]);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + playerIndex + "] Dropped: " );

        for(ResourceType rt : ResourceType.values()) {
            byte amount = amounts[rt.getIndex()];
            if(amount > 0) {
                sb.append(String.format("[%d/%s],", amount, rt.name()));
            }
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(DropResources.class.getSimpleName(), playerIndex, amounts);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DropResources) {
            DropResources that = (DropResources) obj;
            return Arrays.equals(this.amounts, that.amounts) && playerIndex == that.playerIndex && amounts == that.amounts;
        }
        return false;
    }
}
