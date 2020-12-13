
import api.DWGraph_DS;
import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DWGraph_DSTest {

    public static void createGraph(directed_weighted_graph g, int graphSize) {

        for (int i = 0; i < graphSize; i++) {
            node_data newNode = new DWGraph_DS.Node(i);
            g.addNode(newNode);
        }

    }

    public static void createEdgeForNode(directed_weighted_graph g, int nodeKey, int edgeSize) {
        for (int i = 0; i <= edgeSize; i++) {
            if (i == nodeKey)
                g.connect(nodeKey, ++i, 0);
            g.connect(nodeKey, i, 0);
        }
    }

    @Test
    public void testGetNode() {
        directed_weighted_graph g = new DWGraph_DS();
        // test getNode doesn't exist
        assertNull( g.getNode(1));

        //get exist Node
        createGraph(g, 10);
        node_data newNode = new DWGraph_DS.Node(2);
        assertEquals(newNode, g.getNode(2));

        //get deleted node
        g.removeNode(2);
        assertNull(g.getNode(2));

    }

    @Test
    public void testGetEdge() {
        directed_weighted_graph g = new DWGraph_DS();
        createGraph(g,20);

        //edge doesnt exist---> should return null
        assertNull(g.getEdge(0,5));

        g.connect(1,2,0.5);
        edge_data newEdge= new DWGraph_DS.Edge(1,2,0.5);

        assertEquals(g.getEdge(1,2),newEdge);
        assertNotEquals((g.getEdge(2,1)),newEdge);

        assertNull(g.getEdge(2,1));
    }

    @Test
    public void testAddNode(){
        directed_weighted_graph g0 = new DWGraph_DS();
        g0.addNode(new DWGraph_DS.Node(9));

        Assertions.assertTrue(g0.getV().contains(g0.getNode(9)));

        //node already exist --> mc should be the same
        int beforeMc=g0.getMC();
        g0.addNode(new DWGraph_DS.Node(9));
        int afterMc=g0.getMC();

        Assertions.assertEquals(beforeMc,afterMc);
    }

    @Test
    public void testConnect(){
        directed_weighted_graph g0 = new DWGraph_DS();

        createGraph(g0,30);
        createEdgeForNode(g0,0,8);

        assertNotNull(g0.getEdge(0,1));
        assertNull(g0.getEdge(1,1));

        //doesn't exist edge
        assertNull(g0.getEdge(4,5));


        g0.connect(4,5,0);
        assertNotNull(g0.getEdge(4,5));
        assertNull(g0.getEdge(5,4));

        //edge already exist--> mc should be the same.
        int beforeMc=g0.getMC();
        g0.connect(4,5,0);
        int afterMc=g0.getMC();
        assertEquals(beforeMc,afterMc);
    }

    @Test
    public void testGetV(){
        //Null graph
        directed_weighted_graph g0 = new DWGraph_DS();
        Assertions.assertEquals(g0.getV().size(),0);

        Collection<node_data> v = g0.getV();
        Iterator<node_data> iter = v.iterator();
        Assertions.assertFalse(iter.hasNext());

        //the graph isn't null.
        createGraph(g0,10);

        Collection<node_data> v1 = g0.getV();
        Iterator<node_data> iter1 = v1.iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            Assertions.assertNotNull(n);
        }

        Assertions.assertEquals(10, g0.getV().size());
    }

    @Test
    public void getETest(){

        //Graph with not edges
        directed_weighted_graph g0 = new DWGraph_DS();
        createGraph(g0,10);
        Assertions.assertEquals(g0.getE(1).size(),0);

        Collection<edge_data> v = g0.getE(1);
        Iterator<edge_data> iter = v.iterator();
        Assertions.assertFalse(iter.hasNext());

        //connect edges
        createEdgeForNode(g0, 1, 5);


        Collection<edge_data> v1 = g0.getE(1);
        Iterator<edge_data> iter1 = v1.iterator();
        while (iter.hasNext()) {
            edge_data e = iter.next();
            Assertions.assertNotNull(e);
        }

        Assertions.assertEquals(5, g0.getE(1).size());
    }

    @Test
    public void  removeNodeTest(){
        directed_weighted_graph g= new DWGraph_DS();
        createGraph(g,10);

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
        assertNull(g.getNode(0));

        assertNull(g.getEdge(1,0));
        assertNull(g.getEdge(0,1));
        assertNull(g.getEdge(2,0));
        assertNull(g.getEdge(0,4));
        assertNull(g.getEdge(0,5));

        //try to remove deleted Node
        int beforeMc= g.getMC();
        g.removeNode(0);
        int afterMc=g.getMC();
        assertEquals(beforeMc,afterMc);
    }

    @Test

    public void testRemoveEdge(){
        directed_weighted_graph g0 = new DWGraph_DS();
        createGraph(g0,10);
        g0.connect(1,2,0);
        g0.connect(2,1,0);

        g0.removeEdge(1,2);
        assertNull(g0.getEdge(1,2));
        assertNotNull(g0.getEdge(2,1));

        //remove edge doesnt exist--> mc should be the same.
        int beforeMc=g0.getMC();
        g0.removeEdge(0,1);
        int afterMc=g0.getMC();
        assertEquals(beforeMc,afterMc);
    }

    @Test
    public void testNodeSize(){
        directed_weighted_graph g0 = new DWGraph_DS();
        createGraph(g0,10);

        assertEquals(10,g0.nodeSize());

        g0.removeNode(0);
        Assertions.assertEquals(9,g0.nodeSize());

        //remove node doesnt exist--> nodeSize should be the same.
        g0.removeNode(0);
        Assertions.assertEquals(9,g0.nodeSize());

        for (int i=0; i<10; i++)
            g0.removeNode(i);

        Assertions.assertEquals(0,g0.nodeSize());
    }

    @Test
    public void testEdgeSize(){
        directed_weighted_graph g0 = new DWGraph_DS();
        //null graph
        assertEquals(0,g0.edgeSize());

        createGraph(g0,20);
        createEdgeForNode(g0,1,5);

        assertEquals(5,g0.edgeSize());

        g0.removeEdge(1,2);
        Assertions.assertEquals(4,g0.edgeSize());

        //delete the same edge--> edgeSize should be the same.
        g0.removeEdge(1,2);
        assertEquals(4,g0.edgeSize());

        //connect exist edge
        g0.connect(1,3,0);
        assertEquals(4,g0.edgeSize());

        //edgeSize after remove node
        //create 10 edges
        createEdgeForNode(g0,2,10);
        assertEquals(14,g0.edgeSize());

        //delete 5 edges
        g0.removeNode(1);

        Assertions.assertEquals(9,g0.edgeSize());

    }

    @Test
    public void testGetMcAfterBuildG(){
        directed_weighted_graph g0 = new DWGraph_DS();
        createGraph(g0,20);
        assertEquals(20,g0.getMC());

        //add exist Node--> mc should be the same
        g0.addNode(new DWGraph_DS.Node(3));
        assertEquals(20, g0.getMC());

        createEdgeForNode(g0,1,5);
        Assertions.assertEquals(25,g0.getMC());

        createEdgeForNode(g0,2,10);
        Assertions.assertEquals(35,g0.getMC());

    }

    @Test
    //should run in O(100)
    public void testTimeRunOfGetE(){
        directed_weighted_graph g0 = new DWGraph_DS();
        createGraph(g0,200);
        createEdgeForNode(g0,1,100);
        long start= new Date().getTime();
        Collection <edge_data> l=  g0.getE(1);
        long end= new Date().getTime();
        double dt= (end-start)/1000.0;
        System.out.println(dt+" seconds");

    }
    @Test
    //10^6 nodes, 10^7 edges.
    public void testTimeRun(){
        long start= new Date().getTime();
        directed_weighted_graph g0 = new DWGraph_DS();
        for (Integer i=0 ; i<=Math.pow(10,6) ; i++){
            g0.addNode(new DWGraph_DS.Node(i));
        }
        for (Integer i=0; i<=10; i++){
            for(Integer j=i; j<Math.pow(10,6); j++) {
                g0.connect(i, j, 0.5);
            }
        }
        long end= new Date().getTime();
        double dt= (end-start)/1000.0;
        System.out.println(dt+" seconds");

    }

}
