package api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class implement interface that represents a directional weighted graph.
 * The interface has a road-system or communication network in mind -
 * and should support a large number of nodes (over 100,000).
 * The implementation  based on an HashMap representation.
 *
 */

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

    /**
     * returns the node_data by the node_key,
     * @param key - the node_id
     * @return the node_data by the node_key, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return nodes.containsKey(key)?nodes.get(key):null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * @param src
     * @param dest
     * @return edge_data by the src_edge and dest_edge.
     */

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

    /**
     * adds a new node to the graph with the given node_data.
     * if this node  already exist in the graph there is no action.
     * @param n
     */

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

    /**
     * Connects an edge with weight w between node src to node dest.
     * if the edge src-dest already exists - the method simply updates the weight of the edge.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */

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

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * @return Collection<node_data>
     */

    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }


    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges  starting at the given node.
     * @return Collection<edge_data>
     */

    @Override
    public Collection<edge_data> getE(int node_id) {
        if(nodes.containsKey(node_id)) return neighbors.get(node_id).values();
        return new ArrayList<>();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * @return the data of the removed node (null if none).
     * @param key
     */
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

    /**
     * Deletes the edge from the graph,
     * if the edge doesnt exist there is no action.
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */

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

    /** Returns the number of vertices (nodes) in the graph.
     * @return
     */
    @Override
    public int nodeSize() {
        return this.getV().size();
    }
    /**
     * Returns the number of edges .
     * @return
     */

    @Override
    public int edgeSize() {
        return countEdge;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
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


    /**
     * This class implement interface that represents the set of operations applicable on a
     * node (vertex) in a (directional) weighted graph.
     *
     */
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

        /**
         * Returns the key (id) associated with this node.
         * @return
         */

        @Override
        public int getKey() {
            return _key;
        }

        /** Returns the location of this node, if
         * none return null.
         *
         * @return
         */
        @Override
        public geo_location getLocation() {
            return _location;
        }

        /** Allows changing this node's location.
         * @param p - new new location  (position) of this node.
         */
        @Override
        public void setLocation(geo_location p) {
            _location=new Location(p);
        }

        /**
         * Returns the weight associated with this node.
         * @return
         */

        @Override
        public double getWeight() {
            return _weight;
        }

        /**
         * Allows changing this node's weight.
         * @param w - the new weight
         */

        @Override
        public void setWeight(double w) {
            _weight=w;
        }

        /**
         * Returns the remark (meta data) associated with this node.
         * @return
         */

        @Override
        public String getInfo() {
            return _info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         * @param s
         */

        @Override
        public void setInfo(String s) {
            _info=s;
        }

        /**
         * Temporal data (aka color: e,g, white, gray, black)
         * which can be used be algorithms
         * @return
         */

        @Override
        public int getTag() {
            return _tag;
        }

        /**
         * Allows setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */

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

    /**
     * This class implements interface that represents the set of operations applicable on a
     * directional edge(src,dest) in a (directional) weighted graph.
     *
     */

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


        /**
         * Returns the id of the source node of this edge.
         * @return
         */

        @Override
        public int getSrc() {
            return _src;
        }

        /**
         * Returns the id of the destination node of this edge
         * @return
         */

        @Override
        public int getDest() {
            return _dest;
        }

        /**
         * Returns he weight of this edge (positive value).
         * @return
         */

        @Override
        public double getWeight() {
            return _weight;
        }

        /**
         * Returns the remark (meta data) associated with this edge.
         * @return
         */
        @Override
        public String getInfo() {
            return _info;
        }

        /**
         * Allows changing the remark (meta data) associated with this edge.
         * @param s
         */

        @Override
        public void setInfo(String s) {
            _info=s;
        }

        /**
         * Returns temporal data (aka color: e,g, white, gray, black)
         * which can be used be algorithms
         * @return
         */
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
