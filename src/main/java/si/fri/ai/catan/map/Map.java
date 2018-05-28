package si.fri.ai.catan.map;

import si.fri.ai.catan.map.parts.Land;
import si.fri.ai.catan.map.parts.Road;
import si.fri.ai.catan.map.parts.Terrain;
import si.fri.ai.catan.map.parts.enums.TerrainType;
import si.fri.ai.catan.map.parts.positon.Point;
import si.fri.ai.catan.map.parts.positon.Vector;
import si.fri.ai.catan.rules.moves.enums.ResourceType;

import java.util.ArrayList;

public class Map {

    private ArrayList<Terrain> terrains = new ArrayList<>();
    private ArrayList<Land> lands = new ArrayList<>();
    private ArrayList<Road> roads = new ArrayList<>();

    public Map() {

        initTerrain();
        initLand();
        initRoad();
        initTrading();

        initTerrainPoint();
        initLandPoint();
        initRoadPoint();


        //validateLandRoads();
    }


    public Terrain gt(int i) {
        return terrains.get(i);
    }

    public Land gl(int i) {
        return lands.get(i);
    }

    public Road gr(int i) {
        return roads.get(i);
    }


    private void initTerrain() {
        ArrayList<Terrain> t = terrains;

        t.add(new Terrain(0, 6, ResourceType.CLAY));
        t.add(new Terrain(1, 3, ResourceType.WOOD));
        t.add(new Terrain(2, 8, ResourceType.WOOD));

        t.add(new Terrain(3, 2, ResourceType.IRON));
        t.add(new Terrain(4, 9, ResourceType.IRON));
        t.add(new Terrain(5, 4, ResourceType.WHEAT));
        t.add(new Terrain(6, 10, ResourceType.SHEEP));

        t.add(new Terrain(7, 5, ResourceType.CLAY));
        t.add(new Terrain(8, -1, null));
        t.add(new Terrain(9, 11, ResourceType.CLAY));
        t.add(new Terrain(10, 5, ResourceType.WHEAT));
        t.add(new Terrain(11, 9, ResourceType.WHEAT));

        t.add(new Terrain(12, 10, ResourceType.WOOD));
        t.add(new Terrain(13, 3, ResourceType.SHEEP));
        t.add(new Terrain(14, 6, ResourceType.IRON));
        t.add(new Terrain(15, 12, ResourceType.SHEEP));

        t.add(new Terrain(16, 8, ResourceType.WHEAT));
        t.add(new Terrain(17, 4, ResourceType.SHEEP));
        t.add(new Terrain(18, 11, ResourceType.WOOD));
    }

    private void initLand() {

        lands.add(new Land(0, 2, gt(0)));
        lands.add(new Land(1, 2, gt(0)));
        lands.add(new Land(2, 3, gt(0), gt(1)));
        lands.add(new Land(3, 2, gt(1)));
        lands.add(new Land(4, 3, gt(1), gt(2)));
        lands.add(new Land(5, 2, gt(2)));
        lands.add(new Land(6, 2, gt(2)));

        lands.add(new Land(7, 2, gt(3)));
        lands.add(new Land(8, 3, gt(0), gt(3)));
        lands.add(new Land(9, 3, gt(0), gt(3), gt(4)));
        lands.add(new Land(10, 3, gt(0), gt(1), gt(4)));
        lands.add(new Land(11, 3, gt(1), gt(4), gt(5)));
        lands.add(new Land(12, 3, gt(1), gt(2), gt(5)));
        lands.add(new Land(13, 3, gt(2), gt(5), gt(6)));
        lands.add(new Land(14, 3, gt(2), gt(6)));
        lands.add(new Land(15, 2, gt(6)));

        lands.add(new Land(16, 2, gt(7)));
        lands.add(new Land(17, 3, gt(3), gt(7)));
        lands.add(new Land(18, 3, gt(3), gt(7), gt(8)));
        lands.add(new Land(19, 3, gt(3), gt(4), gt(8)));
        lands.add(new Land(20, 3, gt(4), gt(8), gt(9)));
        lands.add(new Land(21, 3, gt(4), gt(5), gt(9)));
        lands.add(new Land(22, 3, gt(5), gt(9), gt(10)));
        lands.add(new Land(23, 3, gt(5), gt(6), gt(10)));
        lands.add(new Land(24, 3, gt(6), gt(10), gt(11)));
        lands.add(new Land(25, 3, gt(6), gt(11)));
        lands.add(new Land(26, 2, gt(11)));

        lands.add(new Land(27, 2, gt(7)));
        lands.add(new Land(28, 3, gt(7), gt(12)));
        lands.add(new Land(29, 3, gt(7), gt(8), gt(12)));
        lands.add(new Land(30, 3, gt(8), gt(12), gt(13)));
        lands.add(new Land(31, 3, gt(8), gt(9), gt(13)));
        lands.add(new Land(32, 3, gt(9), gt(13), gt(14)));
        lands.add(new Land(33, 3, gt(9), gt(10), gt(14)));
        lands.add(new Land(34, 3, gt(10), gt(14), gt(15)));
        lands.add(new Land(35, 3, gt(10), gt(11), gt(15)));
        lands.add(new Land(36, 3, gt(11), gt(15)));
        lands.add(new Land(37, 2, gt(11)));

        lands.add(new Land(38, 2, gt(12)));
        lands.add(new Land(39, 3, gt(12), gt(16)));
        lands.add(new Land(40, 3, gt(12), gt(13), gt(16)));
        lands.add(new Land(41, 3, gt(13), gt(16), gt(17)));
        lands.add(new Land(42, 3, gt(13), gt(14), gt(17)));
        lands.add(new Land(43, 3, gt(14), gt(17), gt(18)));
        lands.add(new Land(44, 3, gt(14), gt(15), gt(18)));
        lands.add(new Land(45, 3, gt(15), gt(18)));
        lands.add(new Land(46, 2, gt(15)));

        lands.add(new Land(47, 2, gt(16)));
        lands.add(new Land(48, 2, gt(16)));
        lands.add(new Land(49, 3, gt(16), gt(17)));
        lands.add(new Land(50, 2, gt(17)));
        lands.add(new Land(51, 3, gt(17), gt(18)));
        lands.add(new Land(52, 2, gt(18)));
        lands.add(new Land(53, 2, gt(18)));

    }

    private void initRoad() {

        roads.add(gl(0).initRoad(0, gl(1)));
        roads.add(gl(1).initRoad(1, gl(2)));
        roads.add(gl(2).initRoad(2, gl(3)));
        roads.add(gl(3).initRoad(3, gl(4)));
        roads.add(gl(4).initRoad(4, gl(5)));
        roads.add(gl(5).initRoad(5, gl(6)));

        roads.add(gl(0).initRoad(6, gl(8)));
        roads.add(gl(2).initRoad(7, gl(10)));
        roads.add(gl(4).initRoad(8, gl(12)));
        roads.add(gl(6).initRoad(9, gl(14)));

        roads.add(gl(7).initRoad(10, gl(8)));
        roads.add(gl(8).initRoad(11, gl(9)));
        roads.add(gl(9).initRoad(12, gl(10)));
        roads.add(gl(10).initRoad(13, gl(11)));
        roads.add(gl(11).initRoad(14, gl(12)));
        roads.add(gl(12).initRoad(15, gl(13)));
        roads.add(gl(13).initRoad(16, gl(14)));
        roads.add(gl(14).initRoad(17, gl(15)));

        roads.add(gl(7).initRoad(18, gl(17)));
        roads.add(gl(9).initRoad(19, gl(19)));
        roads.add(gl(11).initRoad(20, gl(21)));
        roads.add(gl(13).initRoad(21, gl(23)));
        roads.add(gl(15).initRoad(22, gl(25)));

        roads.add(gl(16).initRoad(23, gl(17)));
        roads.add(gl(17).initRoad(24, gl(18)));
        roads.add(gl(18).initRoad(25, gl(19)));
        roads.add(gl(19).initRoad(26, gl(20)));
        roads.add(gl(20).initRoad(27, gl(21)));
        roads.add(gl(21).initRoad(28, gl(22)));
        roads.add(gl(22).initRoad(29, gl(23)));
        roads.add(gl(23).initRoad(30, gl(24)));
        roads.add(gl(24).initRoad(31, gl(25)));
        roads.add(gl(25).initRoad(32, gl(26)));

        roads.add(gl(16).initRoad(33, gl(27)));
        roads.add(gl(18).initRoad(34, gl(29)));
        roads.add(gl(20).initRoad(35, gl(31)));
        roads.add(gl(22).initRoad(36, gl(33)));
        roads.add(gl(24).initRoad(37, gl(35)));
        roads.add(gl(26).initRoad(38, gl(37)));

        roads.add(gl(27).initRoad(39, gl(28)));
        roads.add(gl(28).initRoad(40, gl(29)));
        roads.add(gl(29).initRoad(41, gl(30)));
        roads.add(gl(30).initRoad(42, gl(31)));
        roads.add(gl(31).initRoad(43, gl(32)));
        roads.add(gl(32).initRoad(44, gl(33)));
        roads.add(gl(33).initRoad(45, gl(34)));
        roads.add(gl(34).initRoad(46, gl(35)));
        roads.add(gl(35).initRoad(47, gl(36)));
        roads.add(gl(36).initRoad(48, gl(37)));

        roads.add(gl(28).initRoad(49, gl(38)));
        roads.add(gl(30).initRoad(50, gl(40)));
        roads.add(gl(32).initRoad(51, gl(42)));
        roads.add(gl(34).initRoad(52, gl(44)));
        roads.add(gl(36).initRoad(53, gl(46)));

        roads.add(gl(38).initRoad(54, gl(39)));
        roads.add(gl(39).initRoad(55, gl(40)));
        roads.add(gl(40).initRoad(56, gl(41)));
        roads.add(gl(41).initRoad(57, gl(42)));
        roads.add(gl(42).initRoad(58, gl(43)));
        roads.add(gl(43).initRoad(59, gl(44)));
        roads.add(gl(44).initRoad(60, gl(45)));
        roads.add(gl(45).initRoad(61, gl(46)));

        roads.add(gl(39).initRoad(62, gl(47)));
        roads.add(gl(41).initRoad(63, gl(49)));
        roads.add(gl(43).initRoad(64, gl(51)));
        roads.add(gl(45).initRoad(65, gl(53)));

        roads.add(gl(47).initRoad(66, gl(48)));
        roads.add(gl(48).initRoad(67, gl(49)));
        roads.add(gl(49).initRoad(68, gl(50)));
        roads.add(gl(50).initRoad(69, gl(51)));
        roads.add(gl(51).initRoad(70, gl(52)));
        roads.add(gl(52).initRoad(71, gl(53)));

    }

    private void initTrading() {
        gl(0).setTrading(ResourceType.CLAY);
        gl(1).setTrading(ResourceType.CLAY);

        gl(4).setAnyTrading(true);
        gl(5).setAnyTrading(true);

        gl(14).setTrading(ResourceType.SHEEP);
        gl(15).setTrading(ResourceType.SHEEP);

        gl(26).setAnyTrading(true);
        gl(37).setAnyTrading(true);

        gl(45).setAnyTrading(true);
        gl(53).setAnyTrading(true);

        gl(50).setAnyTrading(true);
        gl(51).setAnyTrading(true);

        gl(47).setTrading(ResourceType.WHEAT);
        gl(48).setTrading(ResourceType.WHEAT);

        gl(28).setTrading(ResourceType.WOOD);
        gl(38).setTrading(ResourceType.WOOD);

        gl(7).setAnyTrading(true);
        gl(17).setAnyTrading(true);

    }



    private void initTerrainPoint(){
        Point p9 = new Point(0, 0);
        gt(9).setPoint(p9);

        Point p8 = p9.add(Vector.terrainUp);
        gt(8).setPoint(p8);

        Point p7 = p8.add(Vector.terrainUp);
        gt(7).setPoint(p7);

        Point p10 = p9.add(Vector.terrainDown);
        gt(10).setPoint(p10);

        Point p11 = p10.add(Vector.terrainDown);
        gt(11).setPoint(p11);

        Point p4 = p9.add(Vector.terrainUpLeft);
        gt(4).setPoint(p4);

        Point p3 = p4.add(Vector.terrainUp);
        gt(3).setPoint(p3);

        Point p5 = p4.add(Vector.terrainDown);
        gt(5).setPoint(p5);

        Point p6 = p5.add(Vector.terrainDown);
        gt(6).setPoint(p6);

        Point p0 = p4.add(Vector.terrainUpLeft);
        gt(0).setPoint(p0);

        Point p1 = p0.add(Vector.terrainDown);
        gt(1).setPoint(p1);

        Point p2 = p1.add(Vector.terrainDown);
        gt(2).setPoint(p2);

        Point p13 = p9.add(Vector.terrainUpRight);
        gt(13).setPoint(p13);

        Point p12 = p13.add(Vector.terrainUp);
        gt(12).setPoint(p12);

        Point p14 = p13.add(Vector.terrainDown);
        gt(14).setPoint(p14);

        Point p15 = p14.add(Vector.terrainDown);
        gt(15).setPoint(p15);

        Point p16 = p13.add(Vector.terrainUpRight);
        gt(16).setPoint(p16);

        Point p17 = p16.add(Vector.terrainDown);
        gt(17).setPoint(p17);

        Point p18 = p17.add(Vector.terrainDown);
        gt(18).setPoint(p18);
    }

    private void initLandPoint() {

        Point p21 = new Point(0,0).add(Vector.X.neg());
        gl(21).setPoint(p21);

        Point p20 = p21.add(Vector.landUpRight);
        gl(20).setPoint(p20);

        Point p19 = p20.add(Vector.landUpLeft);
        gl(19).setPoint(p19);

        Point p18 = p19.add(Vector.landUpRight);
        gl(18).setPoint(p18);

        Point p17 = p18.add(Vector.landUpLeft);
        gl(17).setPoint(p17);

        Point p16 = p17.add(Vector.landUpRight);
        gl(16).setPoint(p16);

        Point p22 = p21.add(Vector.landDownRight);
        gl(22).setPoint(p22);

        Point p23 = p22.add(Vector.landDownLeft);
        gl(23).setPoint(p23);

        Point p24 = p23.add(Vector.landDownRight);
        gl(24).setPoint(p24);

        Point p25 = p24.add(Vector.landDownLeft);
        gl(25).setPoint(p25);

        Point p26 = p25.add(Vector.landDownRight);
        gl(26).setPoint(p26);

        Point p11 = p21.add(Vector.landLeft);
        gl(11).setPoint(p11);

        Point p10 = p11.add(Vector.landUpLeft);
        gl(10).setPoint(p10);

        Point p9 = p10.add(Vector.landUpRight);
        gl(9).setPoint(p9);

        Point p8 = p9.add(Vector.landUpLeft);
        gl(8).setPoint(p8);

        Point p7 = p8.add(Vector.landUpRight);
        gl(7).setPoint(p7);

        Point p12 = p11.add(Vector.landDownLeft);
        gl(12).setPoint(p12);

        Point p13 = p12.add(Vector.landDownRight);
        gl(13).setPoint(p13);

        Point p14 = p13.add(Vector.landDownLeft);
        gl(14).setPoint(p14);

        Point p15 = p14.add(Vector.landDownRight);
        gl(15).setPoint(p15);

        Point p2 = p10.add(Vector.landLeft);
        gl(2).setPoint(p2);

        Point p1 = p2.add(Vector.landUpLeft);
        gl(1).setPoint(p1);

        Point p0 = p1.add(Vector.landUpRight);
        gl(0).setPoint(p0);

        Point p3 = p2.add(Vector.landDownLeft);
        gl(3).setPoint(p3);

        Point p4 = p3.add(Vector.landDownRight);
        gl(4).setPoint(p4);

        Point p5 = p4.add(Vector.landDownLeft);
        gl(5).setPoint(p5);

        Point p6 = p5.add(Vector.landDownRight);
        gl(6).setPoint(p6);


        Point p32 = new Point(0,0).add(Vector.X);
        gl(32).setPoint(p32);

        Point p31 = p32.add(Vector.landUpLeft);
        gl(31).setPoint(p31);

        Point p30 = p31.add(Vector.landUpRight);
        gl(30).setPoint(p30);

        Point p29 = p30.add(Vector.landUpLeft);
        gl(29).setPoint(p29);

        Point p28 = p29.add(Vector.landUpRight);
        gl(28).setPoint(p28);

        Point p27 = p28.add(Vector.landUpLeft);
        gl(27).setPoint(p27);


        Point p33 = p32.add(Vector.landDownLeft);
        gl(33).setPoint(p33);

        Point p34 = p33.add(Vector.landDownRight);
        gl(34).setPoint(p34);

        Point p35 = p34.add(Vector.landDownLeft);
        gl(35).setPoint(p35);

        Point p36 = p35.add(Vector.landDownRight);
        gl(36).setPoint(p36);

        Point p37 = p36.add(Vector.landDownLeft);
        gl(37).setPoint(p37);


        Point p42 = p32.add(Vector.landRight);
        gl(42).setPoint(p42);

        Point p41 = p42.add(Vector.landUpRight);
        gl(41).setPoint(p41);

        Point p40 = p41.add(Vector.landUpLeft);
        gl(40).setPoint(p40);

        Point p39 = p40.add(Vector.landUpRight);
        gl(39).setPoint(p39);

        Point p38 = p39.add(Vector.landUpLeft);
        gl(38).setPoint(p38);

        Point p43 = p42.add(Vector.landDownRight);
        gl(43).setPoint(p43);

        Point p44 = p43.add(Vector.landDownLeft);
        gl(44).setPoint(p44);

        Point p45 = p44.add(Vector.landDownRight);
        gl(45).setPoint(p45);

        Point p46 = p45.add(Vector.landDownLeft);
        gl(46).setPoint(p46);

        Point p49 = p41.add(Vector.landRight);
        gl(49).setPoint(p49);

        Point p48 = p49.add(Vector.landUpRight);
        gl(48).setPoint(p48);

        Point p47 = p48.add(Vector.landUpLeft);
        gl(47).setPoint(p47);

        Point p50 = p49.add(Vector.landDownRight);
        gl(50).setPoint(p50);

        Point p51 = p50.add(Vector.landDownLeft);
        gl(51).setPoint(p51);

        Point p52 = p51.add(Vector.landDownRight);
        gl(52).setPoint(p52);

        Point p53 = p52.add(Vector.landDownLeft);
        gl(53).setPoint(p53);

    }

    private void initRoadPoint() {
        for(Road r : roads) {
            r.calculatePoint();
        }
    }


    private void validateLandRoads() {
        for(Land l : lands) {
            for(int i=0; i<l.getRoads().length; i++){
                if(l.getRoads()[i] == null) {
                    System.out.println("Break");
                }
            }
        }
    }



    public ArrayList<Terrain> getTerrains() {
        return terrains;
    }

    public ArrayList<Land> getLands() {
        return lands;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }
}
