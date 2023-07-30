package sweeper;

import java.rmi.MarshalledObject;

class Bomb {
    private Matrix bombMap;
    private int totalBombs;

    Bomb(int totalBombs){
        this.totalBombs=totalBombs;
        fixBombsCount();
    }

    void start(){
        bombMap=new Matrix(Box.ZERO);
        for(int j=0; j<totalBombs;j++)
            placeBomb();
    }

    Box get(Coord coord){
        return bombMap.get(coord);
    }

    // розміщення бомб, щоб вони не накладалися одна на одну
    private void placeBomb(){
        while(true) {
            Coord coord = Ranges.getRandomCoord();
            if (Box.BOMB==bombMap.get(coord))
                continue;
            bombMap.set(coord, Box.BOMB);
            incNumbersAroundBomb(coord);
            break;
        }
    }

    // збільшення цифр навколо кожної бомби
    private void incNumbersAroundBomb(Coord coord){
        // перебір навколо клітинки та встановлення цифри
        for (Coord around : Ranges.getCoordsAround(coord))
            if (Box.BOMB!=bombMap.get(around))
                bombMap.set(around, bombMap.get(around).getNextNumberBox());
    }

    // фіксація кількості бомб
    private void fixBombsCount(){
        // максимальна кількість бомб має дорівнювати половині площі поля
        int maxBombs=Ranges.getSize().x*Ranges.getSize().y/2;
        if(totalBombs>maxBombs)
            totalBombs=maxBombs;
    }

    int getTotalBombs() {
        return totalBombs;
    }
}
