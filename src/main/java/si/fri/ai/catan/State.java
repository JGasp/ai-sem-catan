package si.fri.ai.catan;

import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.Arrays;

public class State {

    public static final int NUMBER_OF_PLAYERS = 2;

    private static final int DICE_VALUES = 10;

    private static final int DIFFERENT_RESOURCES = 5;
    private static final int RESOURCES_INCOME = DIFFERENT_RESOURCES * DICE_VALUES;
    private static final int DIFFERENT_TRADING_TYPES = DIFFERENT_RESOURCES + 1;

    private static final int FIGURES_BUILD = 1;

    private static final int ROAD_FIGURES = 15;
    private static final int VILLAGES_FIGURES = 5;
    private static final int CITY_FIGURES = 4;

    private static final int PLAYER_ALLOCATION =
                    DIFFERENT_RESOURCES +
                    DIFFERENT_TRADING_TYPES +
                    RESOURCES_INCOME +
                    FIGURES_BUILD + ROAD_FIGURES +
                    FIGURES_BUILD + VILLAGES_FIGURES +
                    FIGURES_BUILD + CITY_FIGURES;


    private static final int ROADS = 72;
    private static final int LANDS = 54;

    private static final int THIEF = 1;

    private static final int MAP_ALLOCATION = LANDS + ROADS + THIEF;

    public static final byte FIGURE_TAG_OFFSET = 10;
    public static final byte ROAD_FIGURES_TAG = 1;
    public static final byte VILLAGE_FIGURES_TAG = 1;
    public static final byte CITY_FIGURES_TAG = 2;

    private float[] avgIncomePerRound;
    private byte[] gameState;

    public State() {
        int finalGameStateSize = MAP_ALLOCATION + NUMBER_OF_PLAYERS * PLAYER_ALLOCATION;
        gameState = new byte[finalGameStateSize];
        avgIncomePerRound = new float[DIFFERENT_RESOURCES * NUMBER_OF_PLAYERS];

        setThiefLand((byte) 8);
    }

    public State(State state) {
        this.gameState = Arrays.copyOf(state.gameState, state.gameState.length);
        this.avgIncomePerRound = Arrays.copyOf(state.avgIncomePerRound, state.avgIncomePerRound.length);
    }

    public State copy() {
        return new State(this);
    }

    /**
     * Player start state structuring
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

    public byte getResource(int playerIndex, ResourceType type) {
        int index = playerIndex * PLAYER_ALLOCATION;
        return gameState[index + type.getIndex()];
    }

    public void setResource(int playerIndex, ResourceType type, byte amount) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + type.getIndex()] = amount;
    }

    public void addResource(int playerIndex, ResourceType type, byte amount) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + type.getIndex()] += amount;
    }

    public void subResource(int playerIndex, ResourceType type, byte amount) {
        int index = playerIndex * PLAYER_ALLOCATION;
        gameState[index + type.getIndex()] -= amount;

        if(gameState[index + type.getIndex()] < 0) {
            System.out.println("Break");
        }
    }

    /**
     * Resource income by dice number
     */
    private static int RESOURCE_INCOME_OFFSET = DIFFERENT_RESOURCES;

    private int diceIndex(int dice) {
        if(dice > 7) {
            dice--;
        }
        return dice - 2;
    }

    public byte getResourceIncome(int playerIndex, ResourceType type, int dice) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        int resourceIndex = type.getIndex() * DICE_VALUES;

        return gameState[index + resourceIndex + diceIndex(dice)];
    }

    public void setResourceIncome(int playerIndex, ResourceType type, int dice, byte amount) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        int resourceIndex = type.getIndex() * DICE_VALUES;

        gameState[index + resourceIndex + diceIndex(dice)] = amount;
    }

    public void addResourceIncome(int playerIndex, ResourceType type, int dice, byte amount) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        int resourceIndex = type.getIndex() * DICE_VALUES;

        gameState[index + resourceIndex + diceIndex(dice)] += amount;
    }

    public void subResourceIncome(int playerIndex, ResourceType type, int dice, byte amount) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_INCOME_OFFSET;
        int resourceIndex = type.getIndex() * DICE_VALUES;

        gameState[index + resourceIndex + diceIndex(dice)] -= amount;

        if(gameState[index + resourceIndex + diceIndex(dice)] < 0) {
            System.out.println("Break");
        }
    }


    /**
     * Trading policies
     */

    private static int RESOURCE_TRADING_OFFSET = RESOURCE_INCOME_OFFSET + RESOURCES_INCOME;

    public boolean isResourceTrading(int playerIndex, ResourceType type) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        return gameState[index + type.getIndex()] == 1;
    }

    public void activateResourceTrading(int playerIndex, ResourceType type) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        gameState[index + type.getIndex()] = 1;
    }

    public boolean isAnyResourceTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        return gameState[index + DIFFERENT_RESOURCES] == 1;
    }

    public void activateAnyResourceTrading(int playerIndex) {
        int index = playerIndex * PLAYER_ALLOCATION + RESOURCE_TRADING_OFFSET;
        gameState[index + DIFFERENT_RESOURCES] = 1;
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

        setLand(playerIndex, landIndex, VILLAGE_FIGURES_TAG);
    }

    public byte deleteVillage(int playerIndex, int offset) {
        int index = playerIndex * PLAYER_ALLOCATION + VILLAGES_FIGURES_OFFSET;

        byte landIndex = gameState[index + 1 + offset];

        gameState[index + 1 + offset] = 0;
        gameState[index]--;

        byte numberOfVillages = gameState[index];
        if(numberOfVillages != 0) {
            gameState[index + 1 + offset] = gameState[index + 1 + numberOfVillages];
        }

        return landIndex;
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

    public byte buildCity(int playerIndex, byte villageIndex) {
        byte landIndex = deleteVillage(playerIndex, villageIndex);

        int index = playerIndex * PLAYER_ALLOCATION + CITIES_FIGURES_OFFSET;
        byte offset = gameState[index]; // Number of cities
        gameState[index + 1 + offset] = landIndex;
        gameState[index]++;

        setLand(playerIndex, landIndex, CITY_FIGURES_TAG);

        return landIndex;
    }



    /**
     * Map information
     */

    private static final int ROADS_OFFSET = NUMBER_OF_PLAYERS * PLAYER_ALLOCATION;

    private void setRoad(int playerIndex, byte roadIndex) {
        byte playerFigureTag = (byte) (playerIndex * FIGURE_TAG_OFFSET + ROAD_FIGURES_TAG);
        gameState[ROADS_OFFSET + roadIndex] = playerFigureTag;
    }

    public byte getRoad(int roadIndex) {
        return gameState[ROADS_OFFSET + roadIndex];
    }

    private static final int LANDS_OFFSET = ROADS_OFFSET + ROADS;

    private void setLand(int playerIndex, byte landIndex, byte figureTag) {
        byte playerFigureTag = (byte) (playerIndex * FIGURE_TAG_OFFSET + figureTag);
        gameState[LANDS_OFFSET + landIndex] = playerFigureTag;
    }

    public byte getLand(byte landIndex) {
        return gameState[LANDS_OFFSET + landIndex];
    }


    /**
     * Average resource gain each turn, based on fair dice
     */

    private float getDiceRatio(int diceSum) {
        switch (diceSum) {
            case 2:
            case 12:
                return 1/36;
            case 3:
            case 11:
                return 2/36;
            case 4:
            case 10:
                return 3/36;
            case 5:
            case 9:
                return 4/36;
            case 6:
            case 8:
                return 5/36;
            case 7:
                return 6/36;
            default:
                return 0;
        }
    }

    public float getResourceAvgIncome(int playerIndex, ResourceType type) {
        int index = playerIndex * DIFFERENT_RESOURCES;
        return avgIncomePerRound[index + type.getIndex()];
    }

    private void calculateResourceAvgIncome(int playerIndex, ResourceType type) {
        int index = playerIndex * DIFFERENT_RESOURCES;

        float avgIncome = 0;
        for(int i=0; i<5; i++) {
            avgIncome += getDiceRatio(i + 2) * gameState[index + type.getIndex() + i];
        }
        avgIncomePerRound[index + type.getIndex()] = avgIncome;
    }

    public void updateResourceAmount(int dice, int playerIndex) {
        for(ResourceType rt: ResourceType.values()) {
            addResource(playerIndex, rt, getResourceIncome(playerIndex, rt, dice));
        }
    }

    public byte getThiefTerrain() {
        int index = NUMBER_OF_PLAYERS * PLAYER_ALLOCATION + ROADS + LANDS;
        return gameState[index];
    }

    public void setThiefLand(byte landIndex) {
        int index = NUMBER_OF_PLAYERS * PLAYER_ALLOCATION + ROADS + LANDS;
        gameState[index] = landIndex;
    }

}
