

import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DWGraph_Test {
    public static int _count = 0;

    //creat graph
    public static directed_weighted_graph graph_creator(int v_size, int e_size) {
        directed_weighted_graph g = new DWGraph_DS();
        Random _rnd = new Random(1);
        for(int i=0;i<v_size;i++) {
            g.addNode(new DWGraph_DS.Node(i));
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i,j, w);
        }
        return g;
    }
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        Random _rnd = null;
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(directed_weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        node_data[] nodes = new node_data[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
//    @Before
//    public void BeforeEach() {
//
//    }

    /**
     * Checks whether the Default constructor is working properly
     */

    @Test
    public void testShortestDist() {

        //distance between nodes in null graph--->path doesnt exist.
        directed_weighted_graph g0 = new DWGraph_DS();
        dw_graph_algorithms g=new DWGraph_Algo();
        g.init(g0);
        assertEquals(-1,g.shortestPathDist(1,2));

        //check distance between exist nodes without edges.--->path doesnt exist.
        for (int i = 0; i < 5; i++) {
            g.getGraph().addNode(new DWGraph_DS.Node(i));
        }
        assertEquals(-1,g.shortestPathDist(1,2));

        //check distance between src==dest-->should be 0
        assertEquals(0,g.shortestPathDist(1,1));

        //shortest dist should be zero
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                g.getGraph().connect(i, j, 0);
        }

        assertEquals(0,g.shortestPathDist(0,3));

        //check what should happen when there is unconnected Node--->path doesnt exist.
        g.getGraph().addNode(new DWGraph_DS.Node(4));
        Assertions.assertEquals(-1,g.shortestPathDist(0,4));

        //check what happen when dest doesnt exist--->path doesnt exist
        Assertions.assertEquals(-1,g.shortestPathDist(1,6));

    }

    @Test
    public void TEST12() {
        directed_weighted_graph g1 = graph_creator(11, 0);
        dw_graph_algorithms ag1 = new DWGraph_Algo();
        ag1.init(g1);
        for(int i=0; i<10; i++)
            g1.connect(i, i+1, i+1);

        for(int i=0; i<g1.nodeSize()-1; i++) {
            assertNotNull(ag1.shortestPath(i, i+1));
            assertEquals(ag1.shortestPathDist(i, i+1), i+1);
            assertEquals(ag1.shortestPathDist(0, i), _count+=i);
        }
        g1.addNode(new DWGraph_DS.Node(11));
        for(int i=0; i<10; i++) {
            assertNull(ag1.shortestPath(i, 11)); // Null Pointer Exception -> FIXED! Added Condition (if)
            assertEquals(ag1.shortestPathDist(i,  11), -1);
        }

        assertEquals(ag1.shortestPathDist(0, 6), 21);
        assertEquals(ag1.shortestPathDist(0, 8), 36);

        List<Integer> myList = new ArrayList<Integer>();
        for(int i=0; i<9; i++)
            myList.add(i);
        assertTrue(checkPath(ag1.shortestPath(0, 8), myList));

        g1.connect(0, 11, 1);
        g1.connect(10, 11, 1);

        assertEquals(ag1.shortestPathDist(0, 8), ag1.shortestPathDist(0, 6), 21); // Distance: 21

        List<Integer> myList2 = new ArrayList<Integer>();
        myList2.add(0);
        for(int i=11; i>7; i--)
            myList2.add(i);
        assertTrue(checkPath(ag1.shortestPath(0, 8), myList2));
    }
    private static boolean checkPath(List<node_data> list, List<Integer> index) {
        if(list.size() == index.size())
            for(int i=0; i<list.size(); i++)
                if(list.get(i).getKey() != index.get(i))
                    return false;
        return true;
    }



    @Test
    public void DGraph() {
        directed_weighted_graph d=new DWGraph_DS();
        assertEquals(0, d.nodeSize());
        assertEquals(0, d.edgeSize());

    }

    /**
     * Checks whether the getNode function is working properly, which means that it returns the corresponding node
     * according to the key
     */
    @Test
    public void getNode1() {
        directed_weighted_graph g0 = new DWGraph_DS();
        node_data helper=g0.getNode(9);
        assertNull(helper);
    }

    @Test
    public void getNode2() {
        directed_weighted_graph g0 = graph_creator(10,0);
        assertNotNull(g0.getNode(1));
    }
    @Test
    public void getNode3() {

    }
    @Test
    public void getNode4() {

    }

    /**
     * Checks if the getEdge function is working properly, ie returns the appropriate edge based on the source key and
     * the target key
     */
    @Test
    public void getEdge1() {
        directed_weighted_graph g2 = graph_creator(10,0);
        g2.connect(1,8,3);
        assertTrue(g2.getEdge(1,8)!=null);
    }
    @Test
    public void getEdge2() {
        directed_weighted_graph g2 = graph_creator(10,0);
        g2.connect(1,3,3.2);
        g2.connect(3,8,7);
        assertTrue(g2.getEdge(3,1)==null);
    }


    /**
     * Checks whether the addNode function is working properly,That is, adds a new node and if it exists the node is
     * replaced with the new node
     */
    @Test
    public void addNode() {

    }

    /**
     * hecks whether the connect function is working properly, That is, adds a new edge and if it exists the edge is
     * replaced with the new edge.If the weight is negative the function throws an exception
     */
    @Test
    public void connect() {
        directed_weighted_graph g = graph_creator(4,0);
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(1,0,8);
        g.removeEdge(0,1);
        assertTrue(g.getEdge(0,1)==null);
        assertTrue(g.getEdge(1,0)!=null);
        g.removeEdge(2,1);
        assertEquals(g.edgeSize(),3);
        g.removeEdge(0,2);
        assertEquals(g.edgeSize(),2);
        g.connect(0,1,1);
        //double w = g.getEdge(2,0).getWeight();
        edge_data e= g.getEdge(2,3);
        assertNull(e);
    }

    /**
     * Checks whether the removeNode function is working properly
     */
    @Test
    public void removeNode() {
        directed_weighted_graph g = graph_creator(6,0);
        g.connect(0,4,1);
        g.connect(0,5,2);
        g.connect(0,1,3);
        g.connect(2,0,8);
        g.connect(3,2,3);
        g.connect(1,0,8);
        g.connect(5,4,8);
        g.connect(4,3,8);
        g.connect(1,2,8);
        g.removeNode(0);
        assertTrue(g.getEdge(1,0)==null);
        assertTrue(g.getEdge(0,1)==null);
        assertTrue(g.getEdge(2,0)==null);
        assertTrue(g.getEdge(0,4)==null);
        assertTrue(g.getEdge(0,5)==null);

    }

    /**
     * Checks whether the removeEdge function is working properly
     */
    @Test
    public void removeEdge() {
        directed_weighted_graph g = graph_creator(6,0);
        g.connect(0,4,1);
        g.connect(0,5,2);
        g.connect(0,1,3);
        g.connect(2,0,8);
        g.connect(3,2,3);
        g.connect(1,0,8);
        g.connect(5,4,8);
        g.connect(4,3,8);
        g.connect(1,2,8);
        g.removeNode(0);
        assertTrue(g.getEdge(1,0)==null);
        assertTrue(g.getEdge(3,2)!=null);
  }

    /**
     * Checks whether the nodeSize function is working properly
     */
    @Test
    public void nodeSize() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new DWGraph_DS.Node(0));
        g.addNode(new DWGraph_DS.Node(1));
        g.addNode(new DWGraph_DS.Node(1));

        g.removeNode(2);
        g.removeNode(1);
        g.removeNode(1);
        int s = g.nodeSize();
        assertEquals(1,s);
    }

    /**
     * Checks whether the edgeSize function is working properly
     */
    @Test
    public void edgeSize() {
        directed_weighted_graph g = new DWGraph_DS();
        g.addNode(new DWGraph_DS.Node(0));
        g.addNode(new DWGraph_DS.Node(1));
        g.addNode(new DWGraph_DS.Node(2));
        g.addNode(new DWGraph_DS.Node(3));
        g.connect(0,1,1);
        g.connect(0,2,2);
        g.connect(0,3,3);
        g.connect(0,1,1);
        int e_size =  g.edgeSize();
        assertEquals(3, e_size);
//        double w03 = g.getEdge(0,3).getWeight();
//        double w30 = g.getEdge(3,0).getWeight();
//        assertEquals(w03, w30);
//        assertEquals(w03, 3);

    }

    /**
     * Checks whether the getMC function is working correctly, which means counting the amount of graph changes properly
     * the changes that can be adding a node, adding a edge, removing a node, and removing a edge
     */
    @Test
    public void getMC() {
        DWGraph_DS.Node toAdd1=new DWGraph_DS.Node(6);
        DWGraph_DS.Node toAdd2=new DWGraph_DS.Node(7);

        directed_weighted_graph g0= new DWGraph_DS();
        int beforeChange0=g0.getMC();
        g0.addNode(toAdd1);
        g0.addNode(toAdd2);
        g0.connect(toAdd1.getKey(),toAdd2.getKey(),100);
        g0.removeEdge(toAdd1.getKey(),toAdd2.getKey());
        g0.removeNode(toAdd1.getKey());
        g0.removeNode(toAdd2.getKey());

        directed_weighted_graph g1= new DWGraph_DS();
        int beforeChange1=g1.getMC();
        g1.addNode(toAdd1);
        g1.addNode(toAdd2);
        g1.connect(toAdd1.getKey(),toAdd2.getKey(),100);
        g1.removeEdge(toAdd1.getKey(),toAdd2.getKey());
        g1.removeNode(toAdd1.getKey());
        g1.removeNode(toAdd2.getKey());

        directed_weighted_graph g2= new DWGraph_DS();
        int beforeChange2=g2.getMC();
        g2.addNode(toAdd1);
        g2.addNode(toAdd2);
        g2.connect(toAdd1.getKey(),toAdd2.getKey(),100);
        g2.removeEdge(toAdd1.getKey(),toAdd2.getKey());
        g2.removeNode(toAdd1.getKey());
        g2.removeNode(toAdd2.getKey());

        directed_weighted_graph g3= new DWGraph_DS();
        int beforeChange3=g3.getMC();
        g3.addNode(toAdd1);
        g3.addNode(toAdd2);
        g3.connect(toAdd1.getKey(),toAdd2.getKey(),100);
        g3.removeEdge(toAdd1.getKey(),toAdd2.getKey());
        g3.removeNode(toAdd1.getKey());
        g3.removeNode(toAdd2.getKey());


        assertEquals(beforeChange0+beforeChange1+beforeChange2+beforeChange3+24,g0.getMC()+g1.getMC()+g2.getMC()+g3.getMC());

    }

}