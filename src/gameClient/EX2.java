package gameClient;

import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.game_service;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EX2 implements  Runnable
{
    private static Arena _ar;
    private static MyFrame _win;
    private directed_weighted_graph g;
    private HashMap<Integer,HashMap<Integer,CL_Pokemon>> data;

    public static void main(String[] args)
    {
        Thread client = new Thread(new EX2());
        client.start();
    }

    @Override
    public void run()
    {
        int scenario_num = 11;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        init(game);
        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());

        int ind=0;long dt=100;
        while (game.isRunning()){
            moveAgants(game);
            try {
                if(ind%1==0) {_win.repaint();}
                //_ar.setTime("Time Left: " + (double) game.timeToEnd() / 1000);
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    public void init(game_service game)
    {
        String gG=game.getGraph();
        dw_graph_algorithms saveG=new DWGraph_Algo();
        saveG.load(save(gG));
        g= saveG.copy();
        _ar=new Arena();
        _ar.setGraph(g);

        String gP=game.getPokemons();
        _ar.setPokemons(Arena.json2Pokemons(gP));

        _win=new MyFrame("test Ex2");
        _win.setSize(1000,700);
        _win.update(_ar);
        _win.show();

        data=new HashMap<>();
        HashMap<Integer , CL_Pokemon> newPok=new HashMap<>();
        String info =game.toString();
        JSONObject line;
        try {
            line=new JSONObject(info);
            JSONObject  gameInfo=line.getJSONObject("GameServer");
            int agentsNum=gameInfo.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());

            ArrayList<CL_Pokemon> cl_pok = Arena.json2Pokemons(game.getPokemons());
            for(int a = 0;a<cl_pok.size();a++)
            {
                Arena.updateEdge(cl_pok.get(a),g);//מיקום פוקימונים
            }

            for(int a = 0;a<agentsNum;a++)
            {//מיקום של הסוכנים
                int ind = a%cl_pok.size();
                CL_Pokemon c = cl_pok.get(ind);

                int dest = c.get_edge().getDest();
                int src = c.get_edge().getSrc();
                startPos(src,dest,c,game ,newPok);
                data.put(a,newPok);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startPos(int src, int dest, CL_Pokemon c,game_service game , HashMap<Integer,CL_Pokemon> h)
    {
        System.out.println("Src: "+src+" dest:"+dest);
        if(c.getType()==-1)
        {
            if(dest>src) {
                game.addAgent(dest);
                h.put(dest,c);
                System.out.println("------>"+dest);
            }
            else
            {
                game.addAgent(src);
                h.put(src,c);
                System.out.println("src---->"+src);
            }
        }
        else if(c.getType()==1){
            if(dest>src){
                game.addAgent(src);
                h.put(src,c);
                System.out.println("------>"+src);
            }
            else{
                game.addAgent(dest);
                h.put(dest,c);
                System.out.println("src---->"+dest);

            }
        }
    }

    private String save(String gG) {
        try {
            FileWriter fw= new FileWriter("Jgraph.json");
            fw.write(gG);
            fw.flush();
            fw.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return "Jgraph.json";
    }

    private void moveAgants(game_service game) {

    }
}
