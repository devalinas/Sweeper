package sweeper;

public class Play {
    private Bomb bomb;
    private Flag flag;

    private GameState state;
    public GameState getState() {
        return state;
    }

    public Play (int cols, int rows, int bombs){
        Ranges.setSize(new Coord(cols, rows));
        bomb=new Bomb(bombs);
        flag=new Flag();
    }

    public void start(){
        bomb.start();
        flag.start();
        state=GameState.PLAYED;
    }

    // метод для розміщення об'єктів на екрані (верхній шар+нижній шар)
    public Box getBox(Coord coord){
        if(flag.get(coord)==Box.OPENED)
            return bomb.get(coord);
        else
            return flag.get(coord);
    }

    // відкриття комірки
    public void pressLeftButton(Coord coord){
        if(gameOver()) return;
        openBox(coord);
        checkWinner();
    }
    // перевірка на перемогу
    private void checkWinner(){
        if(state==GameState.PLAYED)
            if(flag.getCountOfClosedBoxes()==bomb.getTotalBombs())
                state=GameState.WINNER;
    }

    // відкриття комірки
    private void openBox(Coord coord){
        switch (flag.get(coord))
        {
            case OPENED:
                    setOpenedToClosedBoxesAroundNumber(coord);
                return;
            case FLAGED:
                return;
            case CLOSED:
                switch (bomb.get(coord))
                {
                    case ZERO:
                        openBoxesAround(coord);
                        return;
                    case BOMB:
                        openBombs(coord);
                        return;
                    default:
                        flag.setOpenedToBox(coord);
                        return;
                }
        }
    }

    // відкриття закритих комірок навколо числа
    // (рахує к-сть прапорців навколо, якщо їхня к-сть = к-сті бомб, то встановлюються значення)
    void setOpenedToClosedBoxesAroundNumber(Coord coord){
        if(bomb.get(coord)!=Box.BOMB)
            if(flag.getCountOfFlagedBoxesAround(coord)==bomb.get(coord).getNumber())
                for(Coord around:Ranges.getCoordsAround(coord))
                    if(flag.get(around)==Box.CLOSED)
                        openBox(around);
    }

    // відкриття бомби
    private void openBombs(Coord bombed) {
        state=GameState.BOMBED;
        flag.setBombedToBox(bombed);
        // намалювати всі бомби
        for(Coord coord:Ranges.getAllCoords())
            // якщо у вказаній координаті є бомба, то встановлюється відкрита клітинка на закриту бомбу
            if(bomb.get(coord)==Box.BOMB)
                flag.setOpenedToCloseBombBox(coord);
            // якщо нема бомби, ставиться значок без бомби
            else
                flag.setNobombToFlagedSafeBox(coord);
    }

    // метод, що містить рекурсію для відкриття клітинок навколо вибраної координати
    private void openBoxesAround(Coord coord){
        flag.setOpenedToBox(coord);
        for(Coord around:Ranges.getCoordsAround(coord))
            openBox(around);
    }

    // нажата права клавіша
    public void pressRightButton(Coord coord){
        if(gameOver()) return;
        flag.toggleFlagedToBox(coord);
    }

    //завершення гри
    private boolean gameOver() {
        if(state==GameState.PLAYED)
            return false;
        start();
        return true;
    }
}
