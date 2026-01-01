# -_juego_java_interfazOracle19C_crud_array_matriz_Lista_- :. 
🎮 Juego Java con Interfaz + Oracle 19c + CRUD + Array + Matriz + Lista .

<img width="450" height="130" alt="image" src="https://github.com/user-attachments/assets/c02f3fe3-6407-4381-bbaa-865e5f5e4117" />    

<img width="2550" height="1079" alt="image" src="https://github.com/user-attachments/assets/4227853e-dbb9-4f03-9c28-305cfb14b300" />    

<img width="2552" height="1079" alt="image" src="https://github.com/user-attachments/assets/cb4b9463-e13f-42a5-a971-355535f38c46" />    

Solución profesional lista para ejecutar en IntelliJ IDEA :

Implementa:

Juego sencillo con interfaz gráfica (Swing)

Uso de:

Array

Matriz

Lista (ArrayList)

CRUD completo contra Oracle Database 19c

Entrada y salida de datos desde la interfaz

Arquitectura limpia

Código ejecutable

## 🎯 Descripción del Juego

Juego tipo memoria simple :

Se genera una matriz 3x3 (tablero del juego)

En una posición aleatoria hay una estrella

El usuario intenta adivinarla

Estructuras utilizadas
Elemento	Uso
Array	Vidas del jugador
Matriz	Tablero
Lista	Historial de partidas
Base de Datos

Cada partida se registra en Oracle 19c permitiendo CRUD:

Crear partida

Consultar partidas

Actualizar nombre del jugador

Eliminar registros

## ✅ Requerimientos Previos

Oracle 19c instalado

Usuario creado

JDBC Oracle Driver (ojdbc8.jar)

IntelliJ IDEA

## 🗄️ Script Base de Datos Oracle 19c

Ejecutar en SQL Developer:
```
CREATE TABLE GAME_PLAYER (
    ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    PLAYER_NAME VARCHAR2(50),
    ATTEMPTS NUMBER,
    RESULT VARCHAR2(20)
);

INSERT INTO GAME_PLAYER (PLAYER_NAME, ATTEMPTS, RESULT)
VALUES ('Jugador Demo', 2, 'GANÓ');

COMMIT;
```
## 📂 Estructura del Proyecto
* src
* ├─ db
* │    └─ OracleConnection.java
* ├─ dao
* │    └─ GameDAO.java
* ├─ model
* │    └─ Player.java
* └─ ui
      └─ GameUI.java

## 🧩 Código Completo
🔹 Conexión Oracle

db/OracleConnection.java
```
package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class OracleConnection {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "SYSTEM";
    private static final String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Error conectando a Oracle: " + e.getMessage());
            return null;
        }
    }
}
```
🔹 Modelo

model/Player.java
```
package model;

public class Player {
    private int id;
    private String name;
    private int attempts;
    private String result;

    public Player(int id, String name, int attempts, String result) {
        this.id = id;
        this.name = name;
        this.attempts = attempts;
        this.result = result;
    }

    public Player(String name, int attempts, String result) {
        this.name = name;
        this.attempts = attempts;
        this.result = result;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAttempts() { return attempts; }
    public String getResult() { return result; }
}
```
🔹 DAO con CRUD

dao/GameDAO.java
```
package dao;

import db.OracleConnection;
import model.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    public void save(Player p) {
        String sql = "INSERT INTO GAME_PLAYER (PLAYER_NAME, ATTEMPTS, RESULT) VALUES (?, ?, ?)";
        try (Connection con = OracleConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getAttempts());
            ps.setString(3, p.getResult());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error guardando: " + e.getMessage());
        }
    }

    public List<Player> findAll() {
        List<Player> list = new ArrayList<>();
        String sql = "SELECT * FROM GAME_PLAYER";
        try (Connection con = OracleConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Player(
                        rs.getInt("ID"),
                        rs.getString("PLAYER_NAME"),
                        rs.getInt("ATTEMPTS"),
                        rs.getString("RESULT")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error listando: " + e.getMessage());
        }
        return list;
    }

    public void updateName(int id, String newName) {
        String sql = "UPDATE GAME_PLAYER SET PLAYER_NAME=? WHERE ID=?";
        try (Connection con = OracleConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error actualizando: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM GAME_PLAYER WHERE ID=?";
        try (Connection con = OracleConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error eliminando: " + e.getMessage());
        }
    }
}
```
🔹 Interfaz Gráfica + Juego

ui/GameUI.java
```
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

    List<String> history = new ArrayList<>(); // LISTA -> historial

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
```
## ✔️ Validación de Requisitos
* Requisito	Estado
* Juego en Java	✔️
* IntelliJ IDEA	✔️
* Interfaz Gráfica	✔️
* Oracle 19c	✔️
* CRUD Completo	✔️
* Entrada / Salida desde interfaz	✔️
* Uso Array	✔️
* Uso Matriz	✔️
* Uso Lista	✔️
* Arquitectura por paquetes	✔️

## ▶️ Ejecución

* 1️⃣ Crear tabla en Oracle
* 2️⃣ Descargar ojdbc8 y agregarlo al proyecto (Libraries)
* 3️⃣ Ajustar en OracleConnection:

URL

USER

PASSWORD

+ 4️⃣ Ejecutar:
```
GameUI.main()
```
## © Copyright

© 2026 – Proyecto Juego CRUD Oracle 19c – Java Swing
Todos los derechos reservados .

Este software y su documentación asociada son propiedad intelectual del autor.
Queda prohibida su copia, distribución, modificación o uso no autorizado total o parcial sin permiso expreso por escrito.

El uso académico está permitido siempre que se mantenga la autoría y no se utilice con fines comerciales . 
