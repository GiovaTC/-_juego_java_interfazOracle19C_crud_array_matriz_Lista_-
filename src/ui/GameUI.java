package ui;

import dao.GameDAO;
import model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUI extends JFrame {

    private GameDAO dao = new GameDAO();

    int[] lives = {3}; // ARRAY -> vidas

    int[][] board = new int[3][3]; // MATRIZ -> tablero 3x3

    List<String> history = new ArrayList<>(); // lista -> historial

    int attempts = 0;
    String playerName;

    public GameUI() {
        setTitle("Juego CRUD Oracle 19c");
        setSize(500, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        playerName = JOptionPane.showInputDialog("Ingrese su nombre:");

        generateBoard();

        JPanel grid = new JPanel(new GridLayout(3,3));
        add(grid, BorderLayout.CENTER);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                JButton btn = new JButton("?");
                int x=i,y=j;
                btn.addActionListener(e -> play(x,y,btn));
                grid.add(btn);
            }
        }

        JButton crudBtn = new JButton("Ver Registros");
        crudBtn.addActionListener(e -> showDBRecords());
        add(crudBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    void generateBoard(){
        Random r = new Random();
        int x = r.nextInt(3);
        int y = r.nextInt(3);
        board[x][y] = 1;
    }

    void play(int x,int y,JButton btn){
        attempts++;

        if(board[x][y] == 1){
            btn.setBackground(Color.GREEN);
            JOptionPane.showMessageDialog(this,"Ganaste!");

            history.add("Ganó en " + attempts + " intentos");
            dao.save(new Player(playerName, attempts, "GANÓ"));
            return;
        }

        btn.setBackground(Color.RED);
        lives[0]--;

        if(lives[0] == 0){
            JOptionPane.showMessageDialog(this,"Perdiste!");
            history.add("Perdió");
            dao.save(new Player(playerName, attempts, "PERDIÓ"));
        }
    }

    void showDBRecords(){
        List<Player> players = dao.findAll();
        StringBuilder sb = new StringBuilder("REGISTROS:\n\n");

        for(Player p : players){
            sb.append("ID: ").append(p.getId())
                    .append(" | ").append(p.getName())
                    .append(" | Intentos: ").append(p.getAttempts())
                    .append(" | Resultado: ").append(p.getResult())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString());
    }

    public static void main(String[] args) {
        new GameUI();
    }
}