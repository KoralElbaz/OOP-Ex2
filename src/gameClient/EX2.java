package gameClient;

import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.game_service;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EX2 implements  Runnable
{
    private static MyFrame _win;
    private static Arena _ar;

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
        //	int id = 999;
        //	game.login(id);
        init(game);
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
        int ind=0;
        long dt=5000;
        startPos(game, _ar.getGraph());
        while(game.isRunning())
        {
            //moveAgants(game, gg);
            try
            {
                if(ind%1==0) {_win.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        game.startGame();

        //System.exit(0);
        //startPos(game, _ar.getGraph());
    }
    private void init(game_service game)
    {
        dw_graph_algorithms g = new DWGraph_Algo();
        g.load(save(game.getGraph()));
        _ar = new Arena();
        _ar.setGraph(g.getGraph());
        _ar.setPokemons(Arena.json2Pokemons(game.getPokemons()));
        _win = new MyFrame("teat EX2");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = screenSize.width;
        int h = screenSize.height;
        _win.setSize(w, h);
        _win.update(_ar);
        _win.show();
        String info = game.toString();
        JSONObject line;
        try
        {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());

            for (int a = 0; a < cl_fs.size(); a++)
            {
                Arena.updateEdge(cl_fs.get(a), _ar.getGraph());
            }
            for (int a = 0; a < rs; a++)
            {
                int ind = a % cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0)
                {
                    nn = c.get_edge().getSrc();
                }
                startPos(game, _ar.getGraph());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

        public static String save(String s)
        {
        try {
            FileWriter fw= new FileWriter("Jgraph.json");
            fw.write(s);
            fw.flush();
            fw.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return "Jgraph.json";
    }
    public static void startPos(game_service game,directed_weighted_graph g) {
        String a = game.getAgents();
        List<CL_Agent> listOfAgent = Arena.getAgents(a, g);
        _ar.setAgents(listOfAgent);
        String p = game.getPokemons();
        List<CL_Pokemon> listOfPok = Arena.json2Pokemons(p);
        _ar.setPokemons(listOfPok);
        int i=0,j=0;
        while(i<listOfAgent.size()&&j<listOfPok.size()){
            int src=listOfPok.get(j).get_edge().getSrc();
            game.addAgent(src);
            j++;
            i++;
        }
//        for (int i = 0; i < listOfPok.size(); i++) {
//            CL_Pokemon pok = listOfPok.get(i);
//            int src = pok.get_edge().getSrc();
//            for (int j = 0; j < listOfAgent.size(); j++) {
//                CL_Agent age = listOfAgent.get(j);
//                game.addAgent(src);
//            }
//        }
    }
}
