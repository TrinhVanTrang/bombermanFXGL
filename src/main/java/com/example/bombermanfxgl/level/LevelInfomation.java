package com.example.bombermanfxgl.level;

import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Vector;


public class LevelInfomation {
    private int level;
    private int block_width;
    private int block_height;
    private int enemyCode;
    private int enemy_counts;
    private List<Pair<Integer, Integer> >listItem=new Vector<>();

    public LevelInfomation(int level) {
        this.level=level;
        String s="/assets/lv_info/"+String.valueOf(this.level)+"_info.txt";
        URL url=getClass().getResource(s);
        URLConnection urlConnection= null;
        try {
            urlConnection = url.openConnection();
            BufferedReader buffer=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String s1=buffer.readLine();
            String[] data=s1.split(" ");
            this.block_width=Integer.parseInt(data[0]);
            this.block_height=Integer.parseInt(data[1]);
            String s2=buffer.readLine();
            this.enemyCode=Integer.parseInt(s2);
            String s3=buffer.readLine();
            this.enemy_counts=Integer.parseInt(s3);
            String s4=buffer.readLine();
            while(s4!=null) {
                String[] items=s4.split(" ");
                Pair<Integer,Integer>pair=new Pair(items[0],items[1]);
                listItem.add(pair);
                s4=buffer.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error connect!");
            e.printStackTrace();
        }
    }


    public int getBlock_width() {
        return block_width;
    }

    public void setBlock_width(int block_width) {
        this.block_width = block_width;
    }

    public int getBlock_height() {
        return block_height;
    }

    public void setBlock_height(int block_height) {
        this.block_height = block_height;
    }

    public int getEnemyCode() {
        return enemyCode;
    }

    public void setEnemyCode(int enemyCode) {
        this.enemyCode = enemyCode;
    }

    public int getEnemy_counts() {
        return enemy_counts;
    }

    public void setEnemy_counts(int enemy_counts) {
        this.enemy_counts = enemy_counts;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Pair<Integer, Integer>> getListItem() {
        return listItem;
    }

    public void setListItem(List<Pair<Integer, Integer>> listItem) {
        this.listItem = listItem;
    }
}

/**=======================================================================
 *---------------------------------- Mã item -----------------------------
 * 4: bomb
 * 2: speed
 * 3: flame
 * 10: prime
 * 1: pierce
 * 7: kick
 * 5: not_obstacle
 * 6: bonus_life
 * 8:
 * 9:
 * 0: gate
 */

/**=======================================================================
 * --------------------------------- Mã enemy ----------------------------
 * 1: balloon
 * 2: slime
 * 3: barrel
 * 4: cloud
 * 5: bacterium
 * 6: beast
 * 7: octopus
 * 8: coin
 *
 */
