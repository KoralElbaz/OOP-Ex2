//package gameClient;
//
//import api.*;
//import Server.Game_Server_Ex2;
//import com.google.gson.JsonObject;
//import org.json.JSONException;
//import org.json.JSONObject;
//import java.awt.*;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class EX2 implements  Runnable
//{
//    private static Arena _ar;
//    private static MyFrame _win;
//    private static directed_weighted_graph g;
//    private static HashMap<Integer,CL_Pokemon> data;
//
//
//
//    public static void main(String[] args)
//    {
//        Thread client = new Thread(new EX2());
//        client.start();
//    }
//
//    @Override
//    public void run()
//    {
//        int scenario_num =4;
//        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
//        init(game);
//        game.startGame();
//        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
//
//        int ind=0;long dt=100;
//        while (game.isRunning()){
//            moveAgants(game);
//            try {
//                if(ind%1==0) {_win.repaint();}
//                //_ar.setTime("Time Left: " + (double) game.timeToEnd() / 1000);
//                Thread.sleep(dt);
//                ind++;
//            }
//            catch(Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        String res = game.toString();
//        System.out.println(res);
//        System.exit(0);
//    }
//
//    public void init(game_service game)
//    {
//        String gG=game.getGraph();
//        dw_graph_algorithms saveG=new DWGraph_Algo();
//        saveG.load(save(gG));
//        g= saveG.copy();
//        _ar=new Arena();
//        _ar.setGraph(g);
//
//        String gP=game.getPokemons();
//        _ar.setPokemons(Arena.json2Pokemons(gP));
//
//        _win=new MyFrame("test Ex2");
//        _win.setSize(1000,700);
//        _win.update(_ar);
//        _win.show();
//
//        data=new HashMap<>();
//        String info =game.toString();
//        JSONObject line;
//        try {
//            line=new JSONObject(info);
//            JSONObject  gameInfo=line.getJSONObject("GameServer");
//            int agentsNum=gameInfo.getInt("agents");
//            System.out.println(info);
//            System.out.println(game.getPokemons());
//
//            ArrayList<CL_Pokemon> cl_pok = Arena.json2Pokemons(game.getPokemons());
//            for(int a = 0;a<cl_pok.size();a++)
//            {
//                Arena.updateEdge(cl_pok.get(a),g);//׳׳™׳§׳•׳ ׳₪׳•׳§׳™׳׳•׳ ׳™׳
//            }
//
//            for(int a = 0;a<agentsNum;a++)
//            {//׳׳™׳§׳•׳ ׳©׳ ׳”׳¡׳•׳›׳ ׳™׳
//                int ind = a%cl_pok.size();
//                CL_Pokemon c = cl_pok.get(ind);
//
//                int dest = c.get_edge().getDest();
//                int src = c.get_edge().getSrc();
//                startPos(a,src,dest,c,game);
//
//            }
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private void startPos(int id, int src, int dest, CL_Pokemon c,game_service game)
//    {
//        System.out.println("Src: "+src+" dest:"+dest);
//        if(c.getType()==-1)
//        {
//            game.addAgent(c.bigNode());
//            data.put(id,c);
//            c.setIsVisit(true);
//            System.out.println("------>"+c.bigNode());
//
//        }
//        else if(c.getType()==1){
//
//            game.addAgent(c.smallNode());
//            data.put(id,c);
//            c.setIsVisit(true);
//            System.out.println("------>"+c.smallNode());
//
//        }
//    }
//
//    private String save(String gG) {
//        try {
//            FileWriter fw= new FileWriter("Jgraph.json");
//            fw.write(gG);
//            fw.flush();
//            fw.close();
//
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        return "Jgraph.json";
//    }
//
//    private static void moveAgants(game_service game) {
//
//        String lg = game.move();
//        List<CL_Agent> log = Arena.getAgents(lg, g);
//        _ar.setAgents(log);
//        String fs =  game.getPokemons();
//        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
//
//       // System.out.println("try "+ffs);
//
//        for(int a = 0;a<ffs.size();a++)
//        {
//            Arena.updateEdge(ffs.get(a),g);//׳׳™׳§׳•׳ ׳₪׳•׳§׳™׳׳•׳ ׳™׳
//
////            System.out.println("pok "+a+" src "+ffs.get(a).getSrc());
////            System.out.println("pok "+a+" dest "+ffs.get(a).getDest());
//
//        }
//
//        _ar.setPokemons(ffs);
//
//        if (log != null) {
//
//            for(int i=0;i<log.size();i++) {
//                CL_Agent ag = log.get(i);
//
//                int id = ag.getID();
//                int dest = ag.getNextNode();
//                double v= ag.getValue();
//
//                System.out.println("path outside if: " + ag.getShortestPath());
//
//                if (dest == -1 && ag.getShortestPath().isEmpty()) {
//
//                    List<node_data> l = chooseNext(ag,game, id);
//                    ag.setShortestPath((ArrayList<node_data>)l);
//
//                    game.chooseNextEdge(ag.getID(), dest);
//                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
//                }
//                else if(dest == -1 && !ag.getShortestPath().isEmpty()){
//
//                    int nextKey= ag.getShortestPath().get(0).getKey();
//                    ag.setNextNode(nextKey);
//                    System.out.println("path inside if: " + ag.getShortestPath());
//                    game.chooseNextEdge(ag.getID(), nextKey);
//
//                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + nextKey);
//                    ag.getShortestPath().remove(nextKey);
//                }
//
//
//
//            }
//        }
//    }
//
//
//    private static int nextNode(game_service game,directed_weighted_graph g, CL_Agent ag,int id,List<CL_Pokemon> ffs)
//    {
//        List<node_data> res = new ArrayList<node_data>();
//
//        int agSrc= ag.getSrcNode();
//        if(data.get(id)!=null) {
//
//            CL_Pokemon p = data.get(id);
//            int pokSrc = p.get_edge().getSrc();
//            int pokDest = p.get_edge().getDest();
//
//            if (pokSrc == agSrc ) {
//                CL_Pokemon chosen = data.get(id);
//                data.remove(id);
//               // res.add(g.getNode(chosen.getDest()));
//                return chosen.getDest();
//            }
//
//            else if (pokDest == agSrc) {
//
//                CL_Pokemon chosen = data.get(id);
//                data.remove(id);
//                return chosen.getSrc();
//            }
//        }
//
//        else if(ag.getShortestPath().isEmpty()){
//            chooseNext(ag, game, agSrc);
//        }
//
//
//        int ans= ag.getNextInPath();
//        return ans;
//    }
//
//    private static List<node_data> chooseNext(CL_Agent ag,game_service game,int agSrc)
//    {
//
//
//        dw_graph_algorithms ga=new DWGraph_Algo();
//        ga.init(g);
//
//        List<CL_Pokemon> l= _ar.getPokemons();
//        System.out.println(" l "+l);
//
//        int curr=0;
//
//
//        double minPath=Double.MAX_VALUE;
//        List<node_data> res = new ArrayList<node_data>();
//        CL_Pokemon chooseMinPok=null;
//
//        for (CL_Pokemon c:l ) {
//            if (!c.getIsVisit()) {
//
//                if (c.getType() == -1) {
//                    double currDist = ga.shortestPathDist(agSrc, c.bigNode());
//                    if (minPath > currDist) {
//                        minPath = currDist;
//                        chooseMinPok = c;
//                        curr = c.bigNode();
//
//                        res.addAll(ga.shortestPath(agSrc, c.bigNode()));
//                        res.add(g.getNode(c.smallNode()));
//                        if(currDist==0) break;
//                    }
//                }
//                else if (c.getType() == 1) {
//                    double currDist = ga.shortestPathDist(agSrc, c.smallNode());
//                    if (minPath > currDist) {
//                        minPath = currDist;
//                        chooseMinPok = c;
//                        curr = c.smallNode();
//
//                        res.addAll(ga.shortestPath(agSrc, c.smallNode()));
//                        res.add(g.getNode(c.bigNode()));
//                        if(currDist==0) break;
//                    }
//                }
//            }
//        }
//
//        if(chooseMinPok!=null)
//            chooseMinPok.setIsVisit(true);
//
//        data.put(ag.getID(),chooseMinPok);
//
//        ag.setShortestPath((ArrayList<node_data>)res);
//
//        System.out.println("chosen pokimon "+chooseMinPok );
//        System.out.println("shortest path "+ res);
//
//        return res;
//    }
//
//}




/////new/////////
//
//private static void moveAgants(game_service game) {
//
//        String lg = game.move();
//        List<CL_Agent> log = Arena.getAgents(lg, g);
//        _ar.setAgents(log);
//
//        String fs =  game.getPokemons();
//        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
//
//
//        for(int a = 0;a<ffs.size();a++)
//        {
//        Arena.updateEdge(ffs.get(a),g);//׳׳™׳§׳•׳ ׳₪׳•׳§׳™׳׳•׳ ׳™׳
//
//        }
//
//        boolean hasChange=false;
//
//        for (CL_Agent a: log) {
//        if(path.get(a.getID()).isEmpty())
//        hasChange=true;
//        break;
//        }
//
//        if(firstRun || hasChange)
//        {
//        _ar.setPokemons(ffs);
//        for (CL_Agent ag: log )
//        {
//        chooseNext(ag, game);
//        }

//        firstRun=false;
//        }
//
//
//        for(CL_Agent ag: log) {
//
//        int id = ag.getID();
//        int dest = ag.getNextNode();
//        double v = ag.getValue();
//
//        if (dest == -1 && !path.get(id).isEmpty()) {
//
//        int nextKey;
//        nextKey = path.get(id).get(0).getKey();
//        ag.setNextNode(nextKey);
//        game.chooseNextEdge(ag.getID(), nextKey);
//
//        System.out.println(ag.getSpeed());
//
//
//        System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + nextKey);
//        path.get(id).remove(0);
//        }
//
//
//        }
//        }


/////////newwwww/////////

//private static void moveAgants(game_service game) {
//
//        String lg = game.move();
//        List<CL_Agent> log = Arena.getAgents(lg, g);
//        _ar.setAgents(log);
//
//        String fs =  game.getPokemons();
//        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
//
//
//        for(int a = 0;a<ffs.size();a++)
//        {
//        Arena.updateEdge(ffs.get(a),g);//׳׳™׳§׳•׳ ׳₪׳•׳§׳™׳׳•׳ ׳™׳
//
//        }
//
//
//        _ar.setPokemons(ffs);
//
//
//        if (lg != null) {
//
//        for(int i=0;i<log.size();i++) {
//        CL_Agent ag = log.get(i);
//
//        int id = ag.getID();
//        double v = ag.getValue();
//
//
//        if (path.get(id).isEmpty()) {
////                    for(CL_Agent a: log) {
//        hasChange = true;
//        break;
//        // chooseNext(ag, game);
//        }
//        }
////                } else if (!path.get(id).isEmpty()) {
////
////                    int nextKey;
////                    nextKey = path.get(id).get(0).getKey();
////                    ag.setNextNode(nextKey);
////                    game.chooseNextEdge(ag.getID(), nextKey);
////
////                    System.out.println(ag.getSpeed());
////
////
////                    System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + nextKey);
////                    path.get(id).remove(0);
////                }
//
//
//        if(hasChange ==true||firstRun){
//        for(CL_Agent agent: log){
//        chooseNext(agent, game);
//        }
//        hasChange=false;
//        firstRun=false;
//        }
//
//        for(CL_Agent agent: log){
//        int id= agent.getID();
//        double v=agent.getValue();
//
//        int nextKey;
//        nextKey = path.get(id).get(0).getKey();
//        agent.setNextNode(nextKey);
//        game.chooseNextEdge(agent.getID(), nextKey);
//
//        System.out.println(agent.getSpeed());
//
//
//        System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + nextKey);
//        path.get(id).remove(0);
//
//
//        }
//
//        }
//        }

