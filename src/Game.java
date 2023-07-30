import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import sweeper.Box;
import sweeper.Coord;
import sweeper.Ranges;
import sweeper.Play;

public class Game extends JFrame {
    private Play play;
    private JPanel panel;
    // мітка
    private JLabel label;
    private final int COLS=9;
    private final int ROWS=9;
    private final int BOMBS=10;
    private final int IMAGE_SIZE=50;

    public static void main(String[] args) {
        new Game();

    }

    // закритий конструктор
    private Game(){
        play=new Play(COLS, ROWS, BOMBS);
        play.start();
        setImages();
        initLabel();
        initPanel();
        initFrame();
    }

    // стан гри (вивід внизу)
    private void initLabel(){
        label=new JLabel("Welcome!");
        add(label, BorderLayout.SOUTH);
    }

    //задання властивостей
    private void initFrame(){
        // виклик функції для авторозміру, щоб всі елементи вмістились у форму
        pack();
        // при закритті вікна програми завершення за замовчуванням
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //заголовок
        setTitle("Java Sweeper");
        // вікно по центру
        setLocationRelativeTo(null);
        // фіксований розмір вікна без змін
        setResizable(false);
        //видимість (візуальне відображення)
        setVisible(true);
        //іконка
        setIconImage(getImage("icon"));
    }

    // метод для ініціалізації змінної panel
    private void initPanel(){
        panel=new JPanel(){
            @Override
            // метод, який виводить зображення
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for(Coord coord:Ranges.getAllCoords()) {
                    g.drawImage((Image) play.getBox(coord).image, coord.x*IMAGE_SIZE, coord.y*IMAGE_SIZE, this);
                }
            }
        };
        // адаптер для використання мишки
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x=e.getX()/IMAGE_SIZE;
                int y=e.getY()/IMAGE_SIZE;
                Coord coord=new Coord(x,y);
                // нажата ліва кнопка мишки
                if(e.getButton()==MouseEvent.BUTTON1)
                    play.pressLeftButton(coord);
                // нажата права кнопка мишки
                if(e.getButton()==MouseEvent.BUTTON3)
                    play.pressRightButton(coord);
                // нажата середня кнопка мишки (перезапуск)
                if(e.getButton()==MouseEvent.BUTTON2)
                    play.start();
                label.setText(getMessage());
                // перемалювання форми
                panel.repaint();
            }
        });
        // задання розміру поля
        panel.setPreferredSize(new Dimension(Ranges.getSize().x*IMAGE_SIZE,Ranges.getSize().y*IMAGE_SIZE));
        add (panel);
    }

    // вивід повідомлення залежно від стану гри
    private String getMessage() {
        switch (play.getState()){
            case PLAYED:
                return "Think twice!";
            case BOMBED:
                return "YOU LOSE!";
            case WINNER:
                return "CONGRATULATIONS!";
            default:
                return "Welcome!";
        }
    }

    // метод для установки зображень
    private Image getImage (String name) {
        // ім'я файлу для кожного зображення
        String filename = "img/"+name.toLowerCase()+".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(filename));
        return icon.getImage();
    }

    // метод для загрузки та отримання кожного зображення
    private void setImages(){
        for (Box box : Box.values())
            box.image=getImage(box.name());
    }
}

