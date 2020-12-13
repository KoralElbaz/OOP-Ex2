package api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, node_data> nodes;
    private HashMap<Integer, HashMap<Integer, edge_data>> neighbors;
    private HashMap<Integer, HashMap<Integer, edge_data>> connectToMe;////
    private int mc;
    private int countEdge;

    public DWGraph_DS(){
        this.nodes= new HashMap<>();
        this.neighbors = new HashMap<>();
        this.connectToMe=new HashMap<>();
        this.countEdge=0;
        this.mc=0;
    }

    //this function return node_data by the node key.
    @Override
    public node_data getNode(int key) {
        return nodes.containsKey(key)?nodes.get(key):null;
    }

    //this function return edge_data by the src and dest.
    @Override
    public edge_data getEdge(int src, int dest) {
        if(this.edgeSize()==0)
            return null;
        if(nodes.containsKey(src)&&nodes.containsKey(dest)) {
            if(this.neighbors.get(src).get(dest)!=null)
                return this.neighbors.get(src).get(dest);
        }
        return null;
    }

    //this function add a new node to the graph with the given key.
    //if there is already a node with such a key there is no action.
    @Override
    public void addNode(node_data n) {
        if(n!=null && !nodes.containsKey(n.getKey())){
            mc++;
            nodes.put(n.getKey(),n);
            HashMap<Integer, edge_data> newMapForNi = new HashMap<>();
            HashMap<Integer, edge_data> newMapForCon = new HashMap<>();
            neighbors.put(n.getKey(),newMapForNi);
            connectToMe.put(n.getKey(),newMapForCon);
        }
    }

    //this function connect an edge between node1 and node2,
    // with an edge with weight >=0.
    //if the edge node1-node2 already exists - the method simply updates the weight of the edge.
    @Override
    public void connect(int src, int dest, double w) {
        if(src==dest) return;
        if (nodes.containsKey(src) && nodes.containsKey(dest)) {
            if (neighbors.get(src).containsKey(dest) && neighbors.get(src).get(dest).getWeight() == w)  return;
            if (!neighbors.get(src).containsKey(dest))countEdge++;
            mc++;
            edge_data newEdge = new Edge(src, dest, w);

            //we're save in hashMap the node who connect to the src and the
            // edge between them.
            neighbors.get(src).put(dest, newEdge);

            //we're save in other hashMap the node who connect to the dest and the
            //edge between them.
            connectToMe.get(dest).put(src,newEdge);
        }

    }

    // this function return a pointer (shallow copy) for a
    // collection representing all the nodes in the graph.
    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    //This function returns a pointer (shallow copy) for the
	// collection representing all the edges starting at the given node

    @Override
    public Collection<edge_data> getE(int node_id) {
        if(nodes.containsKey(node_id)) return neighbors.get(node_id).values();
        return new ArrayList<>();
    }

    // this function delete the node (with the given ID) from the graph -
    // and removes all edges which starts or ends at this node.
    @Override
    public node_data removeNode(int key) {

        //save the deleted node
        node_data toRemove=this.getNode(key);

        //check the node exist
        if(toRemove!=null && this.neighbors.containsKey(key))
        {
            mc++;
            //delete the edges getting out of the given node
            while(this.neighbors.get(key).size()!=0)
            {
                Integer ans=this.neighbors.get(key).keySet().stream().findFirst().get();
                this.removeEdge(key, ans);
            }

            //delete the edges getting out of the neighbor's node
            while(this.connectToMe.get(key).size()!=0)
            {
                Integer ans=this.connectToMe.get(key).keySet().stream().findFirst().get();
                this.removeEdge(ans, key);
            }

            this.connectToMe.remove(key);
            this.neighbors.remove(key);
            this.nodes.remove(key);
        }
        return toRemove;
    }

    //this function delete the edge from the graph,
    //if the edge doesnt exist there is no action.
    @Override
    public edge_data removeEdge(int src, int dest) {
        edge_data toRemove=neighbors.get(src).get(dest);
        if(toRemove!=null){
            this.neighbors.get(src).remove(dest);
            this.connectToMe.get(dest).remove(src);
            mc++;
            countEdge--;
        }
        return toRemove;
    }

    @Override
    public int nodeSize() {
        return this.getV().size();
    }

    @Override
    public int edgeSize() {
        return countEdge;
    }

    @Override
    public int getMC() {
        return mc;
    }

    @Override
    public String toString() {
        return "DWGraph_DS{" +
                "nodes=" + nodes +
                ", neighbors=" + neighbors +
                ", connectToMe=" + connectToMe +
                ", mc=" + mc +
                ", countEdge=" + countEdge +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        return this.toString().equals(o.toString());
    }

    //================================================================================//
    // =====================================Node======================================//
    //================================================================================//
    public static class Node implements node_data, Serializable {

        private int _key;
        private int _tag;
        private geo_location _location;
        private String _info;
        private double _weight;


        public Node(int key){
            _key=key;
            _tag=0;
            _location=new Location(0, 0, 0);;
            this.setInfo("White");
            this.setWeight(Double.MAX_VALUE);

        }
        @Override
        public int getKey() {
            return _key;
        }

        @Override
        public geo_location getLocation() {
            return _location;
        }

        @Override
        public void setLocation(geo_location p) {
            _location=new Location(p);
        }

        @Override
        public double getWeight() {
            return _weight;
        }

        @Override
        public void setWeight(double w) {
            _weight=w;
        }

        @Override
        public String getInfo() {
            return _info;
        }

        @Override
        public void setInfo(String s) {
            _info=s;
        }

        @Override
        public int getTag() {
            return _tag;
        }

        @Override
        public void setTag(int t) {
            _tag=t;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "_key=" + _key +
                    ", _tag=" + _tag +
                    ", _location=" + _location +
                    ", _info='" + _info + '\'' +
                    ", _weight=" + _weight +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return this.toString().equals(o.toString());
        }

    }

    //================================================================================//
    //======================================Edge======================================//
    //================================================================================//
    public static class Edge implements edge_data, Serializable {
        private int _src;
        private int _dest;
        private double _weight;
        private String _info;
        private int _tag;


        public Edge(int src, int dest, double w){
            _src=src;
            _dest=dest;
            _weight=w;
            _info="";
            _tag=0;
        }


        @Override
        public int getSrc() {
            return _src;
        }

        @Override
        public int getDest() {
            return _dest;
        }

        @Override
        public double getWeight() {
            return _weight;
        }

        @Override
        public String getInfo() {
            return _info;
        }

        @Override
        public void setInfo(String s) {
            _info=s;
        }

        @Override
        public int getTag() {
            return _tag;
        }

        @Override
        public void setTag(int t) {
            _tag=t;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "_src=" + _src +
                    ", _dest=" + _dest +
                    ", _weight=" + _weight +
                    ", _info='" + _info + '\'' +
                    ", _tag=" + _tag +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return this.toString().equals(o.toString());
        }


    }


}
