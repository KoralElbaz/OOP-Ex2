package gameClient;

import api.*;
import Server.Game_Server_Ex2;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EX2 implements  Runnable {
    private static Arena _ar;
    private static MyFrame _win;
    private static int level, id;
    private static directed_weighted_graph g;
    private static HashMap<Integer, List<node_data>> path;
    private static HashMap<String, Boolean> visited;
    private static game_service game;

    private static long dt;

    private static boolean firstRun = true;
    private static boolean change;


    public static void main(String[] args) {
        if (args.length == 2) {
            level = Integer.parseInt(args[1]);
            id = Integer.parseInt(args[0]);
        } else {
            id= MyPanel.PopUpWin.getId();
            level = MyPanel.PopUpWin.getLevel();

        }
        game = Game_Server_Ex2.getServer(level);
        Thread client = new Thread(new EX2());
        client.start();
    }

    @Override
    public void run() {

        init(game);
        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());

        int ind = 0;    dt = 100;
        boolean flag = true;



        while (game.isRunning()) {
            //while(flag){
            moveAgants(game);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                _ar.setTime("Time Left: " + (double) game.timeToEnd() / 1000);
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    public void init(game_service game) {
        String gG = game.getGraph();
        dw_graph_algorithms saveG = new DWGraph_Algo();
        saveG.load(save(gG));
        g = saveG.copy();
        _ar = new Arena();
        _ar.setGraph(g);

        String gP = game.getPokemons();
        _ar.setPokemons(Arena.json2Pokemons(gP));

        _win = new MyFrame("test Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.show();


        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gameInfo = line.getJSONObject("GameServer");
            int agentsNum = gameInfo.getInt("agents");

            System.out.println(info);
            System.out.println(game.getPokemons());

            ArrayList<CL_Pokemon> cl_pok = Arena.json2Pokemons(game.getPokemons());
            visited = new HashMap<>();

            for (int a = 0; a < cl_pok.size(); a++) {
                Arena.updateEdge(cl_pok.get(a), g);//מיקום פוקימונים

                if (!visited.containsKey(cl_pok.get(a).get_edge().toString())) {
                    visited.put(cl_pok.get(a).get_edge().toString(), false);
                }


            }

            path = new HashMap<>();

            for (int a = 0; a < agentsNum; a++) {//מיקום של הסוכנים
                int ind = a % cl_pok.size();
                CL_Pokemon c = cl_pok.get(ind);

                int dest = c.get_edge().getDest();
                int src = c.get_edge().getSrc();
                startPos(a, c, game);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startPos(int id, CL_Pokemon c, game_service game) {

        game.addAgent(c.getSrc());

        List<node_data> newList = new ArrayList<>();
        path.put(id, newList);
        System.out.println("------>" + c.getSrc());


    }


    private String save(String gG) {
        try {
            FileWriter fw = new FileWriter("Jgraph.json");
            fw.write(gG);
            fw.flush();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Jgraph.json";
    }

    private static void moveAgants(game_service game) {

        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, g);
        _ar.setAgents(log);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);


        for (int a = 0; a < ffs.size(); a++) {
            Arena.updateEdge(ffs.get(a), g);//מיקום פוקימונים
            if (!visited.containsKey(ffs.get(a).get_edge().toString()))
                visited.put(ffs.get(a).get_edge().toString(), false);
        }

        change=false;

        for (CL_Agent agents : log) {
            if (agents.getNextNode()== -1 && path.get(agents.getID()).isEmpty()) {
                System.out.println("ChangedAg "+agents.getID());
                change = true;
                break;
            }
        }

        if (change || firstRun) {
            _ar.setPokemons(ffs);
            for (CL_Agent agents : log) {

                path.put(agents.getID(), new ArrayList<>());
                chooseNext(agents, game);

            }
            firstRun=false;
        }


        for (CL_Agent ag : log) {

            if (ag.getNextNode() == -1) {
                int id = ag.getID();
                double v = ag.getValue();

                int nextKey= path.get(id).get(0).getKey();
                ag.setNextNode(nextKey);
                game.chooseNextEdge(ag.getID(), nextKey);
                String infoAg="Agent: " + ag.getID() + ", Grade: " + ag.getValue();
                _ar.set_info(infoAg, id);

                System.out.println("Speed: "+ag.getSpeed());
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + nextKey);

                path.get(id).remove(0);
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

        for (CL_Pokemon c : _ar.getPokemons())
        {
            if (!c.getIsVisit()) {

                double currDist = ga.shortestPathDist(agSrc, c.getSrc());
                if (currDist < minDist) {

                    list.clear();
                    minDist = currDist;
                    chosen = c;
                    list.addAll(ga.shortestPath(agSrc, c.getSrc()));
                    list.add(g.getNode(c.getDest()));

                    System.out.println("Start: "+agSrc+" End "+c.getSrc());

                    if (currDist == 0)
                    {
                        break;
                    }
                }
            }
        }

        if(chosen != null) {
            chosen.setIsVisit(true);
        }

        path.put(ag.getID(),list);

        System.out.println("  Agent: "+ag.getID()+" Pos "+ag.get_curr_edge()+" Pok: "+chosen+" on Edge: "+chosen.get_edge());

        for(node_data n: list) {
            System.out.print(" To: " + n.getKey());
        }
        System.out.println(" ");

    }
}
