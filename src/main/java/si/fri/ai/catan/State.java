package si.fri.ai.catan;

import java.util.Arrays;

public class State {

    private static int NUMBER_OF_PLAYERS = 2;

    private static final int DIFFERENT_RESOURCES = 5;
    private static final int RESOURCES_INCOME = DIFFERENT_RESOURCES * 11;
    private static final int RESOURCES_AVERAGE_INCOME = DIFFERENT_RESOURCES;
    private static final int DIFFERENT_TRADING_TYPES = DIFFERENT_RESOURCES + 1;

    private static final int FIGURES_BUILD = 1;
    private static final int ROAD_FIGURES = 15;
    private static final int VILLAGES_FIGURES = 5;
    private static final int CITY_FIGURES = 4;

    private static final int PLAYER_ALLOCATION =
                    DIFFERENT_RESOURCES +
                    DIFFERENT_TRADING_TYPES +
                    RESOURCES_INCOME +
                    RESOURCES_AVERAGE_INCOME +
                    FIGURES_BUILD + ROAD_FIGURES +
                    FIGURES_BUILD + VILLAGES_FIGURES +
                    FIGURES_BUILD + CITY_FIGURES;


    private static final int ROADS = 70;
    private static final int LANDS = 54;

    private static final int THIEF = 1;
    private static final int ROUND = 1;

    private static final int MAP_ALLOCATION = LANDS + ROADS + THIEF + ROUND;

    private static final byte FIGURE_TAG_OFFSET = 10;
    private static final byte ROAD_FIGURES_TAG = 1;
    private static final byte VILLAGE_FIGURES_TAG = 2;
    private static final byte CITY_FIGURES_TAG = 3;

    private float[] avgIncomePerRound;
    private byte[] gameState;

    public State() {
        int finalGameStateSize = MAP_ALLOCATION + NUMBER_OF_PLAYERS * PLAYER_ALLOCATION;
        gameState = new byte[finalGameStateSize];
        avgIncomePerRound = new float[DIFFERENT_RESOURCES];
    }

    public State(State state) {
        this.gameState = Arrays.copyOf(state.gameState, state.gameState.length);
        this.avgIncomePerRound = Arrays.copyOf(state.avgIncomePerRound, state.avgIncomePerRound.length);
    }

    public static void initNumberOfPlayers(int numberOfPlayers){
        NUMBER_OF_PLAYERS = numberOfPlayers;
    }

    public State copy() {
        return new State(this);
    }

    /**
     * Player game state structuring
     * [4] Resources
     *  - wood
     *  - clay
     *  - sheep
     *  - iron
     *  - wheat
     * [44] Resource income by dice number
     *  - dice[2..12] wood
     *  - dice[2..12] clay
     *  - dice[2..12] sheep
     *  - dice[2..12] iron
     *  - dice[2..12] wheat
     * [5] Trading types
     *  - wood 2:1
     *  - clay 2:1
     *  - sheep 2:1
     *  - iron 2:1
     *  - wheat 2:1
     *  - any 3:1
     *  [1 + ROAD_FIGURES] Road figures
     *  - number of roads build
     *  - road [1..15] road index
     *  [1 + VILLAGE_FIGURES] Village figures
     *  - number of villages build
     *  - villages [1..5] land index
     *  [1 + CITY_FIGURES] City figures
     *  - number of cities build
     *  - cities [1..4] land index;
     */


    /**
     * Resources getters and setters
     */

    public byte getWood(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index];
    }

    public void setWood(int playerIndex, byte wood) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index] = wood;
    }


    public byte getClay(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index + 1];
    }

    public void setClay(int playerIndex, byte clay) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + 1] = clay;
    }


    public byte getSheep(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index + 2];
    }

    public void setSheep(int playerIndex, byte sheep) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + 2] = sheep;
    }


    public byte getIron(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index + 3];
    }

    public void setIron(int playerIndex, byte iron) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + 3] = iron;
    }


    public byte getWheat(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index + 4];
    }

    public void setWheat(int playerIndex, byte iron) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + 4] = iron;
    }


    /**
     * Resource income by dice number
     */
    public static int RESOURCE_INCOME_OFFSET = DIFFERENT_RESOURCES;

    public byte getWoodIncome(int playerIndex, int dice) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        return gameState[index + dice];
    }

    public void setWoodIncome(int playerIndex, int dice, byte wood) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        gameState[index + dice] = wood;
    }


    public byte getClayIncome(int playerIndex, int dice) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        return gameState[index + 11 + dice];
    }

    public void setClayIncome(int playerIndex, int dice, byte clay) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        gameState[index + 11 + dice] = clay;
    }


    public byte getSheepIncome(int playerIndex, int dice) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        return gameState[index + 22 + dice];
    }

    public void setSheepIncome(int playerIndex, int dice, byte sheep) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        gameState[index + 22 + dice] = sheep;
    }


    public byte getIronIncome(int playerIndex, int dice) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        return gameState[index + 33 + dice];
    }

    public void setIronIncome(int playerIndex, int dice, byte iron) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        gameState[index + 33 + dice] = iron;
    }


    public byte getWheatIncome(int playerIndex, int dice) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        return gameState[index + 44 + dice];
    }

    public void setWheatnIncome(int playerIndex, int dice, byte iron) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        gameState[index + 44 + dice] = iron;
    }


    /**
     * Average resource gain each turn, based on fair dice
     */

    /*public static int RESOURCE_AVERAGE_INCOME_OFFSET = RESOURCE_INCOME_OFFSET + RESOURCES_INCOME;


    public byte getWoodAvgIncome(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index];
    }

    public void setWoodAvgIncome(int playerIndex, byte wood) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index] = wood;
    }


    public byte getClayAvgIncome(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index + 1];
    }

    public void setClayAvgIncome(int playerIndex, byte clay) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + 1] = clay;
    }


    public byte getSheepAvgIncome(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index + 2];
    }

    public void setSheepAvgIncome(int playerIndex, byte sheep) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + 2] = sheep;
    }


    public byte getIronAvgIncome(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index + 3];
    }

    public void setIronAvgIncome(int playerIndex, byte iron) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + 3] = iron;
    }


    public byte getWheatAvgIncome(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index + 4];
    }

    public void setWheatAvgIncome(int playerIndex, byte iron) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + 4] = iron;
    }*/


    /**
     * Trading policies
     */

    public static int RESOURCE_TRADING_OFFSET = RESOURCE_INCOME_OFFSET + RESOURCES_INCOME; //RESOURCE_AVERAGE_INCOME_OFFSET + RESOURCES_AVERAGE_INCOME;


    public boolean isWoodTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        return gameState[index] == 1;
    }

    public void activateWoodTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        gameState[index] = 1;
    }


    public boolean isClayTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        return gameState[index + 1] == 1;
    }

    public void activateClayTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        gameState[index + 1] = 1;
    }


    public boolean isSheepTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        return gameState[index + 2] == 1;
    }

    public void activateSheepTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        gameState[index + 2] = 1;
    }


    public boolean isIronTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        return gameState[index + 3] == 1;
    }

    public void activateIronTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        gameState[index + 3] = 1;
    }


    public boolean isWheatTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        return gameState[index + 4] == 1;
    }

    public void activateWheatTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        gameState[index + 4] = 1;
    }


    public boolean isAnyTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        return gameState[index + 5] == 1;
    }

    public void activateAnyTrading(int playerIndex) {
        int index = MAP_ALLOCATION + playerIndex * PLAYER_ALLOCATION + DIFFERENT_RESOURCES;
        gameState[index + 5] = 1;
    }



    /**
     * Figures number and location
     */

    private static final int ROAD_FIGURES_OFFSET = RESOURCE_TRADING_OFFSET + DIFFERENT_TRADING_TYPES;

    public byte getNumberOfRoads(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + ROAD_FIGURES_OFFSET;
        return gameState[index];
    }

    public boolean isAnyRoadAvailable(int playerIndex) {
        return getNumberOfRoads(playerIndex) < ROAD_FIGURES;
    }

    public byte getRoadLocation(int playerIndex, int offset) {
        int index = playerIndex * PLAYER_ALLOCATION + ROAD_FIGURES_OFFSET;
        return gameState[index + 1 + offset];
    }

    public void buildRoad(int playerIndex, byte roadIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + ROAD_FIGURES_OFFSET;
        byte offset = gameState[index];
        gameState[index + 1 + offset] = roadIndex;
        gameState[index]++;

        setRoad(playerIndex, roadIndex);
    }



    private static final int VILLAGES_FIGURES_OFFSET = ROAD_FIGURES_OFFSET + FIGURES_BUILD + ROAD_FIGURES;

    public byte getNumberOfVillages(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + VILLAGES_FIGURES_OFFSET;
        return gameState[index];
    }

    public boolean isAnyVillageAvailable(int playerIndex) {
        return getNumberOfVillages(playerIndex) < VILLAGES_FIGURES;
    }

    public byte getVillagesLocation(int playerIndex, int offset) {
        int index = playerIndex * PLAYER_ALLOCATION + VILLAGES_FIGURES_OFFSET;
        return gameState[index + 1 + offset];
    }

    public void buildVillages(int playerIndex, byte landIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + VILLAGES_FIGURES_OFFSET;
        byte offset = gameState[index];
        gameState[index + 1 + offset] = landIndex;
        gameState[index]++;

        setRoad(playerIndex, landIndex);
    }

    public byte deleteVillage(int playerIndex, int offset) {
        int index = playerIndex * PLAYER_ALLOCATION + VILLAGES_FIGURES_OFFSET;

        byte location = gameState[index + 1 + offset];

        byte numberOfVillages = gameState[index];
        if(numberOfVillages == 1) {
            gameState[index] = 0;
            gameState[index + 1] = 0;
        } else {
            gameState[index]--;
            numberOfVillages = gameState[index];
            gameState[index + 1 + offset] = gameState[index + 1 + numberOfVillages - 1];
        }

        return location;
    }



    private static final int CITIES_FIGURES_OFFSET = VILLAGES_FIGURES_OFFSET + FIGURES_BUILD + VILLAGES_FIGURES;

    public byte getNumberOfCities(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + CITIES_FIGURES_OFFSET;
        return gameState[index];
    }

    public boolean isAnyCityAvailable(int playerIndex) {
        return getNumberOfCities(playerIndex) < CITY_FIGURES;
    }

    public byte getCityLocation(int playerIndex, int offset) {
        int index = playerIndex * PLAYER_ALLOCATION + CITIES_FIGURES_OFFSET;
        return gameState[index + 1 + offset];
    }

    public void buildCity(int playerIndex, byte villageIndex) {
        byte roadIndex = deleteVillage(playerIndex, villageIndex);

        int index = playerIndex * PLAYER_ALLOCATION + CITIES_FIGURES_OFFSET;
        byte offset = gameState[index];
        gameState[index + 1 + offset] = roadIndex;
        gameState[index]++;

        setLand(playerIndex, roadIndex, CITY_FIGURES_TAG);
    }



    /**
     * Map information
     */

    private void setRoad(int playerIndex, byte roadIndex) {
        byte playerFigureTag = (byte) (playerIndex * FIGURE_TAG_OFFSET + ROAD_FIGURES_TAG);
        int index = NUMBER_OF_PLAYERS * PLAYER_ALLOCATION;
        gameState[index + roadIndex] = playerFigureTag;
    }

    public byte getRoad(int roadIndex) {
        int index = NUMBER_OF_PLAYERS * PLAYER_ALLOCATION;
        return gameState[index + roadIndex];
    }



    private void setLand(int playerIndex, byte landIndex, byte figureTag) {
        byte playerFigureTag = (byte) (playerIndex * FIGURE_TAG_OFFSET + figureTag);
        int index = NUMBER_OF_PLAYERS * PLAYER_ALLOCATION;
        gameState[index + landIndex] = playerFigureTag;
    }

    public byte getLand(byte landIndex) {
        int index = NUMBER_OF_PLAYERS * PLAYER_ALLOCATION + ROADS;
        return gameState[index + landIndex];
    }



}
