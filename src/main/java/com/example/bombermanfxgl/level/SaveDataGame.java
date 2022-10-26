package com.example.bombermanfxgl.level;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.example.bombermanfxgl.BombermanApp;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;


public class SaveDataGame {
    private int score;
    private int life_counts;
    private int bom_counts;
    private int flame_counts;
    private int speed;

    public SaveDataGame() {
        String path="/assets/gameDB.txt";
        URL url=getClass().getResource(path);
        URLConnection urlConnection= null;

        try {
            urlConnection=url.openConnection();
            BufferedReader buffer=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String s=buffer.readLine();
            this.score=Integer.valueOf(s);
            s=buffer.readLine();
            this.life_counts=Integer.valueOf(s);
            s=buffer.readLine();
            this.bom_counts=Integer.valueOf(s);
            s=buffer.readLine();
            this.flame_counts=Integer.valueOf(s);
            s=buffer.readLine();
            this.speed=Integer.valueOf(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setAll(int score,int life_counts,int bom_counts,int flame_counts,int speed) {
        setScore(score);
        setLife_counts(life_counts);
        setBom_counts(bom_counts);
        setFlame_counts(flame_counts);
        setSpeed(speed);
    }

    public void writeData() {
        String path="/assets/gameDB.txt";
        URL url=getClass().getResource(path);
//        URLConnection urlConnection= null;
//        try {
//            urlConnection=url.openConnection();
//            BufferedWriter buffer=new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
//            buffer.write(score);
//            buffer.newLine();
//            buffer.write(life_counts);
//            buffer.newLine();
//            buffer.write(bom_counts);
//            buffer.newLine();
//            buffer.write(flame_counts);
//            buffer.newLine();
//            buffer.write(speed);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            FileWriter file=new FileWriter("/asset/gameDB.txt");
            BufferedWriter buffer=new BufferedWriter(file);
            buffer.write(score+"\n");
            buffer.write(life_counts+"\n");
            buffer.write(bom_counts+"\n");
            buffer.write(flame_counts+"\n");
            buffer.write(speed);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLife_counts() {
        return life_counts;
    }

    public void setLife_counts(int life_counts) {
        this.life_counts = life_counts;
    }

    public int getBom_counts() {
        return bom_counts;
    }

    public void setBom_counts(int bom_counts) {
        this.bom_counts = bom_counts;
    }

    public int getFlame_counts() {
        return flame_counts;
    }

    public void setFlame_counts(int flame_counts) {
        this.flame_counts = flame_counts;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
