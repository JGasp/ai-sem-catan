package si.fri.ai.catan.map;

import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.map.parts.enums.TerrainType;

import java.util.ArrayList;

public class Map {

    private ArrayList<Terrain> terrains = new ArrayList<>();
    private ArrayList<Land> lands = new ArrayList<>();
    private ArrayList<Road> roads = new ArrayList<>();

    public Map() {
        initTerrain();
        initLand();
        initRoad();
    }


    private void initTerrain() {
        ArrayList<Terrain> t = terrains;

        t.add(new Terrain(1, 6, TerrainType.QUERY));
        t.add(new Terrain(2, 3, TerrainType.FORREST));
        t.add(new Terrain(3, 8, TerrainType.FORREST));

        t.add(new Terrain(4, 2, TerrainType.MOUNTAIN));
        t.add(new Terrain(5, 9, TerrainType.MOUNTAIN));
        t.add(new Terrain(6, 4, TerrainType.FIELD));
        t.add(new Terrain(7, 10, TerrainType.PASTURE));

        t.add(new Terrain(8, 5, TerrainType.QUERY));
        t.add(new Terrain(9, -1, TerrainType.DESERT));
        t.add(new Terrain(10, 11, TerrainType.QUERY));
        t.add(new Terrain(11, 5, TerrainType.FIELD));
        t.add(new Terrain(12, 9, TerrainType.FIELD));

        t.add(new Terrain(13, 10, TerrainType.FORREST));
        t.add(new Terrain(14, 3, TerrainType.PASTURE));
        t.add(new Terrain(15, 6, TerrainType.MOUNTAIN));
        t.add(new Terrain(16, 12, TerrainType.PASTURE));

        t.add(new Terrain(17, 8, TerrainType.FIELD));
        t.add(new Terrain(18, 4, TerrainType.PASTURE));
        t.add(new Terrain(19, 11, TerrainType.FORREST));
    }

    private Terrain gt(int i) {
        return terrains.get(i - 1);
    }

    private void initLand() {

        lands.add(new Land(1, 2, gt(1)));
        lands.add(new Land(2, 2, gt(1)));
        lands.add(new Land(3, 3, gt(1), gt(2)));
        lands.add(new Land(4, 2, gt(2)));
        lands.add(new Land(5, 3, gt(2), gt(3)));
        lands.add(new Land(6, 2, gt(3)));
        lands.add(new Land(7, 2, gt(3)));

        lands.add(new Land(8, 2, gt(4)));
        lands.add(new Land(9, 3, gt(1), gt(4)));
        lands.add(new Land(10, 3, gt(1), gt(4), gt(5)));
        lands.add(new Land(11, 3, gt(1), gt(2), gt(5)));
        lands.add(new Land(11, 3, gt(1), gt(2), gt(5)));
        lands.add(new Land(12, 3, gt(2), gt(5), gt(6)));
        lands.add(new Land(13, 3, gt(2), gt(3), gt(6)));
        lands.add(new Land(14, 3, gt(3), gt(6), gt(7)));
        lands.add(new Land(15, 3, gt(3), gt(7)));
        lands.add(new Land(16, 2, gt(10)));

        lands.add(new Land(17, 2, gt(8)));
        lands.add(new Land(18, 3, gt(4), gt(8)));
        lands.add(new Land(19, 3, gt(4), gt(8), gt(9)));
        lands.add(new Land(20, 3, gt(4), gt(5), gt(9)));
        lands.add(new Land(21, 3, gt(5), gt(9), gt(10)));
        lands.add(new Land(22, 3, gt(5), gt(6), gt(10)));
        lands.add(new Land(23, 3, gt(6), gt(10), gt(11)));
        lands.add(new Land(24, 3, gt(6), gt(7), gt(11)));
        lands.add(new Land(25, 3, gt(7), gt(11), gt(12)));
        lands.add(new Land(26, 3, gt(7), gt(12)));
        lands.add(new Land(27, 2, gt(12)));

        lands.add(new Land(28, 2, gt(8)));
        lands.add(new Land(29, 3, gt(8), gt(13)));
        lands.add(new Land(30, 3, gt(8), gt(9), gt(13)));
        lands.add(new Land(31, 3, gt(9), gt(13), gt(14)));
        lands.add(new Land(32, 3, gt(9), gt(10), gt(14)));
        lands.add(new Land(33, 3, gt(10), gt(14), gt(15)));
        lands.add(new Land(34, 3, gt(10), gt(11), gt(15)));
        lands.add(new Land(35, 3, gt(11), gt(15), gt(16)));
        lands.add(new Land(36, 3, gt(11), gt(12), gt(16)));
        lands.add(new Land(37, 3, gt(12), gt(16)));
        lands.add(new Land(38, 3, gt(12)));

        lands.add(new Land(39, 2, gt(13)));
        lands.add(new Land(40, 3, gt(13), gt(17)));
        lands.add(new Land(41, 3, gt(13), gt(14), gt(17)));
        lands.add(new Land(42, 3, gt(14), gt(17), gt(18)));
        lands.add(new Land(43, 3, gt(14), gt(15), gt(18)));
        lands.add(new Land(44, 3, gt(15), gt(18), gt(19)));
        lands.add(new Land(45, 3, gt(15), gt(16), gt(19)));
        lands.add(new Land(46, 3, gt(16), gt(19)));
        lands.add(new Land(47, 3, gt(16)));

        lands.add(new Land(48, 2, gt(17)));
        lands.add(new Land(49, 2, gt(17)));
        lands.add(new Land(50, 3, gt(17), gt(18)));
        lands.add(new Land(51, 2, gt(18)));
        lands.add(new Land(52, 3, gt(18), gt(19)));
        lands.add(new Land(53, 2, gt(19)));
        lands.add(new Land(54, 2, gt(19)));

    }

    private Land gl(int i) {
        return lands.get(i - 1);
    }

    private void initRoad() {

        roads.add(gl(1).initRoad(1, gl(2)));
        roads.add(gl(2).initRoad(2, gl(3)));
        roads.add(gl(3).initRoad(3, gl(4)));
        roads.add(gl(4).initRoad(4, gl(5)));
        roads.add(gl(5).initRoad(5, gl(6)));
        roads.add(gl(6).initRoad(6, gl(7)));

        roads.add(gl(1).initRoad(7, gl(9)));
        roads.add(gl(3).initRoad(8, gl(11)));
        roads.add(gl(5).initRoad(9, gl(13)));
        roads.add(gl(7).initRoad(10, gl(15)));

        roads.add(gl(8).initRoad(11, gl(9)));
        roads.add(gl(9).initRoad(12, gl(10)));
        roads.add(gl(10).initRoad(13, gl(11)));
        roads.add(gl(11).initRoad(14, gl(12)));
        roads.add(gl(12).initRoad(15, gl(13)));
        roads.add(gl(13).initRoad(16, gl(14)));
        roads.add(gl(14).initRoad(17, gl(15)));
        roads.add(gl(15).initRoad(18, gl(16)));

        roads.add(gl(8).initRoad(19, gl(18)));
        roads.add(gl(10).initRoad(20, gl(20)));
        roads.add(gl(12).initRoad(21, gl(22)));
        roads.add(gl(14).initRoad(22, gl(24)));
        roads.add(gl(16).initRoad(23, gl(26)));

        roads.add(gl(17).initRoad(24, gl(18)));
        roads.add(gl(18).initRoad(25, gl(19)));
        roads.add(gl(19).initRoad(26, gl(20)));
        roads.add(gl(20).initRoad(27, gl(21)));
        roads.add(gl(21).initRoad(28, gl(22)));
        roads.add(gl(22).initRoad(29, gl(23)));
        roads.add(gl(23).initRoad(30, gl(24)));
        roads.add(gl(24).initRoad(31, gl(25)));
        roads.add(gl(25).initRoad(32, gl(26)));
        roads.add(gl(26).initRoad(33, gl(27)));

        roads.add(gl(17).initRoad(34, gl(28)));
        roads.add(gl(19).initRoad(35, gl(30)));
        roads.add(gl(21).initRoad(36, gl(32)));
        roads.add(gl(23).initRoad(37, gl(34)));
        roads.add(gl(25).initRoad(38, gl(36)));
        roads.add(gl(27).initRoad(39, gl(38)));

        roads.add(gl(28).initRoad(40, gl(29)));
        roads.add(gl(29).initRoad(41, gl(30)));
        roads.add(gl(30).initRoad(42, gl(31)));
        roads.add(gl(31).initRoad(43, gl(32)));
        roads.add(gl(32).initRoad(44, gl(33)));
        roads.add(gl(33).initRoad(45, gl(34)));
        roads.add(gl(34).initRoad(46, gl(35)));
        roads.add(gl(35).initRoad(47, gl(36)));
        roads.add(gl(36).initRoad(48, gl(37)));
        roads.add(gl(37).initRoad(49, gl(38)));

        roads.add(gl(29).initRoad(50, gl(39)));
        roads.add(gl(31).initRoad(51, gl(41)));
        roads.add(gl(33).initRoad(52, gl(43)));
        roads.add(gl(35).initRoad(53, gl(45)));
        roads.add(gl(37).initRoad(54, gl(47)));

        roads.add(gl(39).initRoad(55, gl(40)));
        roads.add(gl(40).initRoad(56, gl(41)));
        roads.add(gl(41).initRoad(57, gl(42)));
        roads.add(gl(42).initRoad(58, gl(43)));
        roads.add(gl(43).initRoad(59, gl(44)));
        roads.add(gl(44).initRoad(60, gl(45)));
        roads.add(gl(45).initRoad(61, gl(46)));
        roads.add(gl(46).initRoad(62, gl(47)));

        roads.add(gl(40).initRoad(63, gl(48)));
        roads.add(gl(42).initRoad(64, gl(50)));
        roads.add(gl(44).initRoad(65, gl(52)));
        roads.add(gl(46).initRoad(66, gl(54)));

        roads.add(gl(48).initRoad(67, gl(49)));
        roads.add(gl(49).initRoad(68, gl(50)));
        roads.add(gl(50).initRoad(69, gl(51)));
        roads.add(gl(51).initRoad(70, gl(52)));
        roads.add(gl(52).initRoad(71, gl(53)));
        roads.add(gl(53).initRoad(72, gl(54)));

    }
}
