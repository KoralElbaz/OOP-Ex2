package gameClient;

import api.*;
import Server.Game_Server_Ex2;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ex2 implements  Runnable {
    private static Arena _ar;
    private static MyFrame _win;
    private static int level, id;
    private static directed_weighted_graph g;
    private static HashMap<Integer, List<node_data>> path;
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
        Thread client = new Thread(new Ex2());
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
            moveAgants();
            try {
                dt = 100;
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


            for (int a = 0; a < cl_pok.size(); a++) {

                Arena.updateEdge(cl_pok.get(a), g);//מיקום פוקימונים

            }

            path = new HashMap<>();

            for (int a = 0; a < agentsNum; a++) {//מיקום של הסוכנים
                int ind = a % cl_pok.size();
                CL_Pokemon c = cl_pok.get(ind);

                startPos(a, c);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Positions the agents for the first time according
     * to the location of the Pokemon
     * @param id
     * @param c
     */
    private void startPos(int id, CL_Pokemon c) {

        game.addAgent(c.getSrc());

        List<node_data> newList = new ArrayList<>();
        path.put(id, newList);


    }

    /**
     * Saves the graph as Json file.
     * @param gG
     * @return
     */
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

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination next edge is chosen .
     * In case the agent reached the destination a new destination was chosen for him
     * @param
     */
    private static void moveAgants() {

        //update the game data.
        String lg = game.move();
        List<CL_Agent> agents = Arena.getAgents(lg, g);
        _ar.setAgents(agents);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);


        for (int a = 0; a < ffs.size(); a++) {
            Arena.updateEdge(ffs.get(a), g);
        }

        change=false;

        /* checking if there was any change on agents.
        change==arrived to the destination
        */
        for (CL_Agent agent : agents) {
            if (agent.getNextNode()== -1 && path.get(agent.getID()).isEmpty()) {
                change = true;
                break;
            }
        }

        //if there was any change--> Choose a new target for all the Pokemon
        if (change || firstRun) {
            _ar.setPokemons(ffs);
            for (CL_Agent agent : agents) {

                path.put(agent.getID(), new ArrayList<>());
                chooseNext(agent);

            }
            firstRun=false;
        }


        for (CL_Agent agent : agents) {

            if (agent.getNextNode() == -1) {
                int id = agent.getID();
                double v = agent.getValue();

                //get the next node in the shortest path.
                int nextKey= path.get(id).get(0).getKey();
                agent.setNextNode(nextKey);
                game.chooseNextEdge(agent.getID(), nextKey);

                String infoAg="Agent: " + agent.getID() + ", Grade: " + agent.getValue();
                _ar.set_info(infoAg, id);

                agent.setNextNode(nextKey);
                game.chooseNextEdge(agent.getID(), nextKey);

                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + nextKey);

                path.get(id).remove(0);

                if(path.size()==0 && shortEdge(agent.get_curr_edge(),agent)){
                    dt=15;
                }
            }


        }
    }



    /**
     * This function checks which pokemon should send the Agent
     * by calculating to which Pokemon the shortest path
     *
     * @param ag     is the AgentPokemon.
     */
    private static void chooseNext(CL_Agent ag) {

        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(g);

        List<node_data> list = new ArrayList<>();

        double minDist = Double.MAX_VALUE;
        CL_Pokemon chosen = null;
        int agSrc = ag.getSrcNode();

        for (CL_Pokemon c : _ar.getPokemons())
        {
            if (!c.getIsVisit()) { //there isn't any agent who choose this pokemon.

                double currDist = ga.shortestPathDist(agSrc, c.getSrc());

                //Save the shortest route
                if (currDist < minDist) {

                    list.clear();
                    minDist = currDist;
                    chosen = c;
                    list.addAll(ga.shortestPath(agSrc, c.getSrc()));
                    list.add(g.getNode(c.getDest()));

                    //if currDist==0 ,this is definitely the shortest way
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

    }

    /**
     * Checking if the edge too short.
     * @param e
     * @param a
     * @return
     */
    private static boolean shortEdge(edge_data e, CL_Agent a){
        geo_location src = g.getNode(e.getSrc()).getLocation();
        geo_location dest = g.getNode(e.getDest()).getLocation();
        double dist = src.distance(dest);
        if (dist < (0.001) / 2 || (e.getWeight()<1.9&&a.getSpeed()==5)) {
            return true;
        }
        return false;
    }
}