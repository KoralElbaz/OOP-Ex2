package api;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.json.JSONArray;

/**
 * This interface represents a Directed (positive) Weighted Graph Theory Algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // JSON file
 * 6. Load(file); // JSON file
 *
 *
 */

public class DWGraph_Algo implements dw_graph_algorithms{
    private directed_weighted_graph myGraph;

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.myGraph=g;
    }

    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return myGraph;
    }

    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph ans=new DWGraph_DS();
        copyNodes(ans);
        copyEdges(ans);
        return ans;
    }
    /**
     * copy the nodes from our graph to the other graph.
     */
    private void copyNodes(directed_weighted_graph ans) {
        for (node_data node:this.myGraph.getV())
        {
            ans.addNode(node);
        }
    }

    /**
     * copy the edges from our graph to the other graph.
     */
    private void copyEdges(directed_weighted_graph ans) {
        for (node_data node:this.myGraph.getV())
        {
            for(edge_data edge: myGraph.getE(node.getKey())){
                int src= edge.getSrc();
                int dest= edge.getDest();
                double weight = edge.getWeight();
                ans.connect(src,dest,weight);
            }

        }

    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node.
     * This method uses a dfs algorithm.
     * @return
     */

    @Override
    public boolean isConnected() {
        if (myGraph.nodeSize()==0 || myGraph.nodeSize()==1)
            return true;

        upDateInfo(); // update the info of this graph's nodes

        List<Integer> order= new ArrayList<>();

        node_data curr=myGraph.getV().iterator().next(); //passing on the graph's nodes.

        dfs(myGraph, curr, order ); ////check if the reverse graph is connected.

        if(order.size()!= myGraph.nodeSize())
            return false;

        directed_weighted_graph reverse= buildReverseGraph(myGraph);

        upDateInfo(); // update the info of this graph's nodes
        order.clear();

        dfs(reverse, curr, order ); //check if the reverse graph is connected.

        if(order.size()!= myGraph.nodeSize())
            return false;

        return true;
    }

    /**
     * Gets a graph and reverses the direction of its edges
     * @param myGraph the graph we work on
     * @return reverse graph.
     */
    private directed_weighted_graph buildReverseGraph(directed_weighted_graph myGraph) {
        directed_weighted_graph newOne= new DWGraph_DS();

        //copy all the nodes of the given graph.
        for( node_data node: myGraph.getV()){
            newOne.addNode(node);
        }

        /*copy all the edges of the given graph and switches between
        src and dest.*/

        for (node_data node: newOne.getV())
            for (edge_data edge: myGraph.getE(node.getKey())){
                newOne.connect(edge.getDest(),edge.getSrc(),edge.getWeight());
            }

        return  newOne;
    }

    /**
     * Adds to the given list all the node are connect
     * to him and indicates it.
     * @param g te graph we work on
     * @param curr current node
     * @param l empty list
     */
    private void dfs(directed_weighted_graph g, node_data curr , List <Integer> l){
        curr.setInfo("Black");

        for(edge_data edge: g.getE(curr.getKey())){
            node_data node= g.getNode(edge.getDest());
            if(node.getInfo().equals("White"))
                dfs(g,node,l);
        }
        l.add(curr.getKey());
    }


    /**
     * this method update the info of this graph's nodes to be "White" ,as in the beginning.
     * @return this graph after update the info
     */
    private void upDateInfo() {
        for(node_data node: this.myGraph.getV()) {
            node.setInfo("White");
        }
    }
    /**
     * this method update the tag of this graph's nodes to be Double.MAX_VALUE ,as in the beginning.
     * @return this graph after update the tag
     */
    private void upDateWeight() {
        for(node_data node: this.myGraph.getV()) {
            node.setWeight(Double.MAX_VALUE);
        }
    }

    /**
     * Dijkstra algorithm- is an algorithm for finding the shortest paths between
     * nodes in a graph
     * @param src -current node
     * @return all the nodes of the graph and their predecessors.
     */
    private HashMap<node_data,node_data> dijkstra(node_data src) {
        this.upDateWeight(); this.upDateInfo(); //up date the visited list and the min dist
        HashMap<node_data, node_data> parentChange = new HashMap<>();//save the pred that do the min dist
        Queue<node_data> q = new LinkedList<>();
        src.setWeight(0);
        q.add(src); //Add to the Queue the first ex1.src.node_info
        parentChange.put(src, null); //Add to the parentChange the first ex1.src.node_info and because is the first he has no previous

        while (!q.isEmpty()) // running on this queue until is empty.
        {
            node_data ans = q.remove();
            if (!q.contains(ans)) //if this queue already contains this value i will not check again.
            {
                for (edge_data neighbour : this.myGraph.getE(ans.getKey())) // running on this list of connected to this ans value.
                {
                    node_data Ni = this.myGraph.getNode(neighbour.getDest());
                    if (Ni.getInfo().equals("White"))
                    //checking if i have already visited this node.
                    //Black==visited , White==no visited
                    {
                        double newDistance = this.myGraph.getEdge(ans.getKey(), Ni.getKey()).getWeight() + ans.getWeight();
                        //Calculates the new distance with the current (pred) distance

                        if (newDistance < Ni.getWeight()) //checking if the new dist smallest from neighbour distance
                        {
                            Ni.setWeight(newDistance);//up date the tag if we found smallest distance
                            parentChange.put(Ni, ans);//Add to the parentChange the neighbour and his previous (ans)
                            q.add(Ni);//Add to the Queue the neighbour ex1.src.node_info
                        }
                    }
                }
                ans.setInfo("Black"); //because i checked on the neighbors i chance the info to Black==visited
            }
        }
        return parentChange;
    }


    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */

    @Override
    public double shortestPathDist(int src, int dest) {

        if(src==dest)
            return 0;

        node_data v1=this.myGraph.getNode(src);
        node_data v2=this.myGraph.getNode(dest);

        if(v1==null || v2==null)
            return -1;

        this.dijkstra(v1); //Activating the algorithm
        if(v2.getWeight()==Double.MAX_VALUE) //there is no path
            return -1;

        else return v2.getWeight();
    }


    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {

        node_data v1=this.myGraph.getNode(src);
        node_data v2=this.myGraph.getNode(dest);
        List<node_data> shortPath = new ArrayList<>();

        //if the nodes are null its mean that they are don't exist.
        if(v1==null || v2==null)
            return null;

        //empty list.
        if(src==dest)
            return shortPath;

        HashMap<node_data,node_data> pred=this.dijkstra(v1);
        node_data current= v2;

        //add the nodes are on part of the shortest path.
        while(current!=v1 && pred.get(current)!=null)
        {
            shortPath.add(current);
            node_data ans=pred.get(current);
            current=ans;
        }

        if(pred.get(current)!=null)
        {
            shortPath.clear();
            return null;
        }

        shortPath.add(v1);
        return reverse(shortPath);
    }

    /**
     * This function reverses the values in the list
     * @param path - Short path
     * @return Short path after reverse
     */
    private List<node_data> reverse(List<node_data> path) {
        List<node_data> shortPath = new ArrayList<>();
        for (int i=path.size()-1 ; i>=0 ; i--)
        {
            shortPath.add(path.get(i));
        }
        return shortPath;
    }


    /**
     * Saves this weighted directed graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try
        {
            JsonWriter r=new CreateJsonGraph().graphToJson(this.myGraph,file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */

    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder= new GsonBuilder();
            builder.registerTypeAdapter(DWGraph_DS.class,new CreateGraph());
            Gson gson= builder.create();

            FileReader reader= new FileReader(file);
            directed_weighted_graph graph= gson.fromJson(reader, DWGraph_DS.class);
            System.out.println(graph);
            init(graph);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DWGraph_Algo{" +
                "myGraph=" + myGraph +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_Algo that = (DWGraph_Algo) o;
        return this.toString().equals(o.toString());
    }

    /**
     * Returns a Jason file that represents the given graph
     * @return
     */

    //Serialize
    private static class CreateJsonGraph {

        public JsonWriter graphToJson(directed_weighted_graph g,String file) throws IOException {
            FileWriter fileWriter= new FileWriter(file);
            JsonWriter writer = new JsonWriter(fileWriter);
            writer.beginObject();
            writer.name("Edges");
            writer.beginArray();

            //passing the graph's edges and save the data.
            for (node_data node : g.getV()) {
                for (edge_data edge: g.getE(node.getKey())) {
                    writer.beginObject();
                    writer.name("src").value(edge.getSrc());
                    writer.name("w").value(edge.getWeight());
                    writer.name("dest").value(edge.getDest());
                    writer.endObject();
                }
            }
            writer.endArray();
            writer.name("Nodes");
            writer.beginArray();

            //passing the graph's nodes and save the data.
            for (node_data node : g.getV()) {
                writer.beginObject();
                writer.name("pos").value(node.getLocation().toString());
                writer.name("id").value(node.getKey());
                writer.endObject();
            }
            writer.endArray();
            writer.endObject();
            writer.close();
            return writer;
        }
    }

    /**
     * Returns a graph by reading a Jason file.
     * @return
     */
    //Deserialize
    private class CreateGraph implements JsonDeserializer<directed_weighted_graph> {
        @Override
        public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            directed_weighted_graph g=new DWGraph_DS();

            JsonObject jasonO= jsonElement.getAsJsonObject();
            JsonArray nodes= jasonO.get("Nodes").getAsJsonArray();
            JsonArray edges= jasonO.get("Edges").getAsJsonArray();

            //add nodes to the graph.
            for (JsonElement node:nodes) {
                int key=((JsonObject)node).get("id").getAsInt();
                String pos=((JsonObject)node).get("pos").getAsString();
                g.addNode(new DWGraph_DS.Node(key));
                g.getNode(key).setLocation(new Location(pos));
            }

            //add edges to the graph.
            for (JsonElement edge:edges) {
                int key=((JsonObject)edge).get("src").getAsInt();
                int dest=((JsonObject)edge).get("dest").getAsInt();
                double w=((JsonObject)edge).get("w").getAsDouble();
                g.connect(key,dest,w);
            }
            return g;
        }
    }
}
