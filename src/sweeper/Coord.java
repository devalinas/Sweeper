package sweeper;

import java.util.Objects;

public class Coord {
    public int x;
    public int y;

    // збереження координат
    public Coord(int x, int y){
        this.x=x;
        this.y=y;
    }

    // анотація - перевизначений метод
    @Override
    public boolean equals(Object o) {
        // якщо координата передана, то перевірка на співпадіння координат
        if (o instanceof Coord) {
            // приведення типів
            Coord to=(Coord) o;
            return to.x==x && to.y==y;
        }
        // повернення базового класу
        return super.equals(o);
    }
}
