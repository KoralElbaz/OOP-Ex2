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
    private static directed_weighted_graph g;
    private static HashMap<Integer,CL_Pokemon> data;
    public static void main(String[] args)
    {
        Thread client = new Thread(new EX2());
        client.start();
    }

    @Override
    public void run()
    {
        int scenario_num = 0;
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
                startPos(a,src,dest,c,game);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startPos(int id, int src, int dest, CL_Pokemon c,game_service game)
    {
        System.out.println("Src: "+src+" dest:"+dest);
        if(c.getType()==-1)
        {
            game.addAgent(c.bigNode());
            data.put(id,c);
            System.out.println("------>"+c.bigNode());

        }
        else if(c.getType()==1){

            game.addAgent(c.smallNode());
            data.put(id,c);
            System.out.println("------>"+c.smallNode());

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

    private static void moveAgants(game_service game) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, g);
        _ar.setAgents(log);
        String fs =  game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        for(int a = 0;a<ffs.size();a++)
        {
            Arena.updateEdge(ffs.get(a),g);//מיקום פוקימונים
        }
        _ar.setPokemons(ffs);

        for(int i=0;i<log.size();i++) {
            CL_Agent ag = log.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (dest == -1) {

                dest = nextNode(game,g, ag, id);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);


            }
        }
    }

    private static int nextNode(game_service game,directed_weighted_graph g, CL_Agent ag,int id)
    {

        int agSrc= ag.getSrcNode();

        if(data.get(id)!=null) {

            CL_Pokemon p = data.get(id);
            int pokSrc = p.get_edge().getSrc();
            int pokDest = p.get_edge().getDest();

            if (pokSrc == agSrc ) {
                CL_Pokemon chosen = data.get(id);
                data.remove(id);
                return chosen.getDest();
            }

            else if (pokDest == agSrc) {

                CL_Pokemon chosen = data.get(id);
                data.remove(id);
                return chosen.getSrc();
            }
        }

        if(ag.getShortestPath()==null) {
            chooseNext(ag, game, agSrc);
        }

        return ag.getNextInPath();

    }

    private static void chooseNext(CL_Agent ag,game_service game,int agSrc)
    {

        dw_graph_algorithms ga=new DWGraph_Algo();
        ga.init(g);
        String pok =  game.getPokemons();
        List<CL_Pokemon> cl_pok = Arena.json2Pokemons(pok);
        int curr=0;
        for(int a = 0;a<cl_pok.size();a++)
        {
            Arena.updateEdge(cl_pok.get(a),g);//מיקום פוקימונים
        }

        double minPath=Double.MAX_VALUE;
        CL_Pokemon chooseMinPok=null;
        for (CL_Pokemon c:cl_pok )
        {
            if(c.getType()==-1)
            {
                double currDist=ga.shortestPathDist(agSrc,c.bigNode());
                if(minPath>currDist){
                    minPath=currDist;
                    chooseMinPok=c;
                    curr=c.bigNode();
                }
            }
            else if(c.getType()==1)
            {
                double currDist=ga.shortestPathDist(agSrc,c.smallNode());
                if(minPath>currDist){
                    minPath=currDist;
                    chooseMinPok=c;
                    curr=c.smallNode();
                }
            }
        }
        data.put(ag.getID(),chooseMinPok);
        ag.setShortestPath(ga.shortestPath(agSrc,curr));

    }

}