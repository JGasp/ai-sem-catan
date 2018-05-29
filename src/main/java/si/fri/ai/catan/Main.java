package si.fri.ai.catan;

import si.fri.ai.catan.rules.Rule;

public class Main {

    public static void main(String[] args) {

        playGame();
        //playGames();
        //testDiceFairness();

    }

    public static void playGame() {
        Game game = new Game();
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
        long[] playerWins = new long[State.NUMBER_OF_PLAYERS];

        long count = 0;
        while (count < 100000) {
            count++;

            Game game = new Game(false);
            game.start();

            playerWins[game.getWinnerIndex()]++;
        }

        for(int pi=0; pi<State.NUMBER_OF_PLAYERS; pi++) {
            System.out.printf("Player: [%d] \t Wins: [%d]\n", pi, playerWins[pi]);
        }

    }

}
