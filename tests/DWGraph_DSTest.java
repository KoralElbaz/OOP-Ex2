package ex2.tests;

import ex2.api.DWGraph_DS;
import ex2.api.directed_weighted_graph;
import ex2.api.edge_data;
import ex2.api.node_data;
import org.junit.jupiter.api.Test;
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
    public void testConnect1() {
        directed_weighted_graph g = new DWGraph_DS();
        createGraph(g,10);
        g.connect(1,3,1.0);
        g.connect(1,5,2.0);
        g.connect(4,1,2.0);
        g.connect(1,5,2.0);
        g.connect(5,1,2.0);

    }
}
