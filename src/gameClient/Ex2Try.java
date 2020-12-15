package gameClient;

import api.*;
import Server.Game_Server_Ex2;
import com.google.gson.JsonObject;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.MyFrame;
import org.json.JSONException;
import org.json.JSONObject;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ex2Try implements  Runnable
{
    private static Arena _ar;
    private static MyFrame _win;
    private static int level, id;
    private static directed_weighted_graph g;
    private static HashMap<Integer, List<node_data>> path;
    private static HashMap <CL_Pokemon, Boolean> visited;
    private static game_service game;
    private static  long dt;



    public static void main(String[] args)
    {
        if (args.length == 2) {
            level = Integer.parseInt(args[0]);
            id = Integer.parseInt(args[1]);
        }
        else {
            String ID=MyPanel.PopUpWin.getId();
            id=Integer.parseInt(ID);
            String Level=MyPanel.PopUpWin.getLevel();
            level=Integer.parseInt(Level);
        }
        game = Game_Server_Ex2.getServer(level);
        Thread client = new Thread(new Ex2Try());
        client.start();
    }

    @Override
    public void run()
    {

        init(game);
        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());

        int ind=0; dt=100;
        boolean flag=true;
        while (game.isRunning()){
        //while(flag){
            moveAgants(game);
            try {
                if(ind%1==0) {_win.repaint();}
                _ar.setTime("Time Left: " + (double) game.timeToEnd() / 1000);
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


        String info =game.toString();
        JSONObject line;
        try {
            line=new JSONObject(info);
            JSONObject  gameInfo=line.getJSONObject("GameServer");
            int agentsNum=gameInfo.getInt("agents");

            System.out.println(info);
            System.out.println(game.getPokemons());

            ArrayList<CL_Pokemon> cl_pok = Arena.json2Pokemons(game.getPokemons());
            visited=new HashMap<>();

            for(int a = 0;a<cl_pok.size();a++)
            {
                Arena.updateEdge(cl_pok.get(a),g);//מיקום פוקימונים

                if(!visited.containsKey(cl_pok.get(a))) {
                    visited.put(cl_pok.get(a), false);
                }


            }

            path= new HashMap<>();

            for(int a = 0;a<agentsNum;a++)
            {//מיקום של הסוכנים
                int ind = a%cl_pok.size();
                CL_Pokemon c = cl_pok.get(ind);

                int dest = c.get_edge().getDest();
                int src = c.get_edge().getSrc();
                startPos(a,c,game);

            }



        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startPos(int id, CL_Pokemon c,game_service game)
    {

        game.addAgent(c.getSrc());

        List<node_data> newList= new ArrayList<>();
        path.put(id, newList);
        System.out.println("------>"+c.getSrc());


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

        if (lg != null) {

            for(int i=0;i<log.size();i++) {
                CL_Agent ag = log.get(i);

                int id = ag.getID();
                int dest=ag.getNextNode();
                double v = ag.getValue();


                if (dest==-1&&path.get(id).isEmpty()) {

                        chooseNext(ag, game);

                } else if (dest==-1&&!path.get(id).isEmpty()) {

                    int nextKey;
                    nextKey = path.get(id).get(0).getKey();
                    ag.setNextNode(nextKey);
                    game.chooseNextEdge(ag.getID(), nextKey);

                    System.out.println(ag.getSpeed());


                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + nextKey);
                    path.get(id).remove(0);
                }


            }
        }
    }




    private static void chooseNext(CL_Agent ag,game_service game) {
        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(g);

        List<node_data> list = new ArrayList<>();
        double minDist = Double.MAX_VALUE;
        CL_Pokemon chosen = null;
        int agSrc = ag.getSrcNode();

        System.out.println("Visited: "+visited.toString());

        for (CL_Pokemon c : _ar.getPokemons())
        {

            System.out.println("The Pok: "+ c);

            if (c.getIsVisit()==false) {
                double currDist = ga.shortestPathDist(agSrc, c.getSrc());

                if (currDist < minDist) {
                    minDist = currDist;
                    chosen = c;
                    list.addAll(ga.shortestPath(agSrc, c.getSrc()));
                    list.add(g.getNode(c.getDest()));
                    if (currDist == 0) {
                        break;
                    }
                }
            }
        }
        if(chosen != null) {
            chosen.setIsVisit(true);
            visited.put(chosen,true);
        }
        //ag.set_curr_edge(chosen.get_edge());
        //System.out.println("Weight!!!!!!!!!!!"+chosen.get_edge().getWeight());
        path.put(ag.getID(),list);


    }
}
