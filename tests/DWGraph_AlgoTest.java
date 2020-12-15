
import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DWGraph_AlgoTest {

    @Test
    public void testInit(){
        directed_weighted_graph g0 = new DWGraph_DS();
        for(int i=0; i<5; i++) {
            g0.addNode(new DWGraph_DS.Node(i));
        }
        directed_weighted_graph newOne= new DWGraph_DS();
        for(int i=0; i<7; i++) {
            g0.addNode(new DWGraph_DS.Node(i));
        }

        dw_graph_algorithms g=new DWGraph_Algo();
        g.init(g0);
        assertEquals(g0,g.getGraph());

        g.init(newOne);
        assertEquals(newOne,g.getGraph());
        assertNotEquals(g0,g.getGraph());

    }

    @Test
    public void testGetG(){
        directed_weighted_graph g0 = new DWGraph_DS();
        dw_graph_algorithms g=new DWGraph_Algo();

        //check on null graph
        g.init(g0);

        for(int i=0; i<5; i++) {
            g0.addNode(new DWGraph_DS.Node(i));
        }

        assertEquals(g0,g.getGraph());
    }

    @Test
    public void testCopy(){
        directed_weighted_graph g0 = new DWGraph_DS();
        dw_graph_algorithms g=new DWGraph_Algo();
        g.init(g0);

        directed_weighted_graph copiedG= g.copy();

        //check on null graph
        assertEquals(g.getGraph(),copiedG);


        for(int i=0; i<5; i++) {
            g0.addNode(new DWGraph_DS.Node(i));
        }
        g0.connect(1,2,0.5);
        g0.connect(3,4,8);
        g0.connect(0,1,3);


        directed_weighted_graph anotherCopiedG= g.copy();
        assertNotEquals(copiedG, anotherCopiedG);

        assertEquals(g.getGraph().nodeSize(),anotherCopiedG.nodeSize());
        assertEquals(g.getGraph().edgeSize(),anotherCopiedG.edgeSize());


        assertEquals(g.getGraph(),anotherCopiedG);

        anotherCopiedG.removeNode(3);
        Assertions.assertNotEquals(g.getGraph(),anotherCopiedG);

    }

    @Test
    public void testIsConnected(){

        //null graph
        directed_weighted_graph g0 = new DWGraph_DS();

        dw_graph_algorithms g=new DWGraph_Algo();
        g.init(g0);

        assertTrue(g.isConnected());

        //graph with one node
        g.getGraph().addNode(new DWGraph_DS.Node(1));
        assertTrue(g.isConnected());

        //check on small graph
        for (int i=0; i<3; i++){
            node_data n = new DWGraph_DS.Node(i);
            g.getGraph().addNode(n);
        }
        g.getGraph().connect(0,1,2);
        g.getGraph().connect(1,2,0.5);

        assertFalse(g.isConnected());

        g.getGraph().connect(2,1,0);
        g.getGraph().connect(1,0,8);
        assertTrue(g.isConnected());

        //bigger graph

        for(int i=0; i<5;i++){
            g.getGraph().addNode(new DWGraph_DS.Node(i));
        }
        for (int i=0; i<5; i++){
            for(int j=0; j<5;j++){
                g.getGraph().connect(i,j,0);
            }
        }

        assertTrue(g.isConnected());

        for (int i=0; i<5; i++){
            g.getGraph().removeEdge(0,i);
        }
        assertFalse(g.isConnected());

        g.getGraph().connect(0,1,0.3);
        assertTrue(g.isConnected());
    }




    @Test
    public void readAndSave() throws IOException {
        directed_weighted_graph g0 = new DWGraph_DS();
        DWGraph_DSTest.createGraph(g0,15);
        DWGraph_DSTest.createEdgeForNode(g0,2,3);

        dw_graph_algorithms g=new DWGraph_Algo();
        g.init(g0);
        Assertions.assertEquals(g.getGraph(),g0);

        g.save("graph.json");

        directed_weighted_graph g1 = new DWGraph_DS();
        DWGraph_DSTest.createGraph(g1,15);
        DWGraph_DSTest.createEdgeForNode(g1,2,3);

        g.load("graph.json");

        Assertions.assertEquals(g0,g1);
        g1.connect(1,2,0.3);
        Assertions.assertNotEquals(g0,g1);
    }
    @Test
    public void readAndSave2() throws IOException {
        directed_weighted_graph g0 = new DWGraph_DS();
        for(int i=0; i<5; i++) {
            g0.addNode(new DWGraph_DS.Node(i));
        }
        g0.connect(0,2,2);
        g0.connect(1,3,0.5);

        dw_graph_algorithms g=new DWGraph_Algo();
        g.init(g0);
        g.save("graph.json");
        g.load("graph.json");
        System.out.println("\n graph size "+ g.getGraph().nodeSize());
        System.out.println("\n Edge size "+ g.getGraph().edgeSize());
        directed_weighted_graph newG= g.getGraph();
        Assertions.assertEquals(g0,newG);
    }
    @Test
    public void testSave() throws IOException {
        directed_weighted_graph g= new DWGraph_DS();
        g.addNode(new DWGraph_DS.Node(1));
        g.addNode(new DWGraph_DS.Node(2));
        g.addNode(new DWGraph_DS.Node(3));
        g.connect(1,2,0);
        g.connect(2,3,0.1);
        dw_graph_algorithms g0= new DWGraph_Algo();
        g0.init(g);

        g0.save("myFirstJason.txt");
    }
}
