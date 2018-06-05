package si.fri.ai.catan;

import si.fri.ai.catan.dto.Counter;
import si.fri.ai.catan.players.HumanPlayer;
import si.fri.ai.catan.players.MonteCarloPlayer;
import si.fri.ai.catan.players.RandomPlayer;
import si.fri.ai.catan.players.base.Player;
import si.fri.ai.catan.rules.Rule;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        //playGame();
        playGames();
        //testDiceFairness();

    }

    public static void playGame() {
        Game game = new Game(getHumanVsMonteCarlo(true));
        game.start();
    }

    public static void testDiceFairness() {
        long[] amount = new long[11];

        long count = 0;
        while (count < 10000000) {
            count++;
            int diceThrow = Rule.throwDice();
            amount[diceThrow - 2]++;
        }

        for(int i=0; i<amount.length; i++) {
            float actual = ((float) amount[i]) / ((float) count);
            float expected = State.getDiceRatio(i + 2);
            float diff = Math.abs(actual - expected);

            float relative = diff / expected * 100;

            System.out.printf("DICE [%2d] A: %f \t E: %f \t D: %f \tR: %f %%\n", i+2, actual, expected, diff, relative);
        }
    }

    public static void playGames() {

        //Player[] players = getRngVsMonteCarlo(false);
        Player[] players = getRngVsMonteCarlo(false);

        HashMap<Player, Counter> counters = new HashMap<>();
        for(Player p : players) {
            counters.put(p, new Counter());
        }

        long count = 0;
        while (count < 100000) {
            count++;

            Game game = new Game(players,false);
            game.start();

            int pi = game.getWinnerIndex();
            if(pi >= 0) {
                Counter c = counters.get(players[pi]);
                c.inc();
            }

            printScore(counters);

            swapPlayerPosition(players);
        }

    }

    private static void printScore(HashMap<Player, Counter> counters) {
        System.out.print("Score: ");
        for(Player p : counters.keySet()) {
            int wins = counters.get(p).getValue();
            System.out.printf("%s: %d \t", p.getClass().getSimpleName(), wins);
        }
        System.out.println();

    }

    public static Player[] getHumanVsMonteCarlo(boolean verbose) {
        Player[] players = new Player[2];
        players[0] = new MonteCarloPlayer(0, verbose);
        players[1] = new HumanPlayer(1);

        return players;
    }

    public static Player[] getRngVsMonteCarlo(boolean verbose) {
        Player[] players = new Player[2];
        players[0] = new MonteCarloPlayer(0, verbose);
        players[1] = new RandomPlayer(1);

        return players;
    }

    public static Player[] getRngVsHuman() {
        Player[] players = new Player[2];
        players[0] = new RandomPlayer(0);
        players[1] = new HumanPlayer(1);

        return players;
    }

    public static void swapPlayerPosition(Player[] players) {
        Player temp = players[0];
        players[0] = players[1];
        players[1] = temp;

        players[0].setPlayerIndex(0);
        players[1].setPlayerIndex(1);
    }

}
