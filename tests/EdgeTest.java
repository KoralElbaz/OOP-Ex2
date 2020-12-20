import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.edge_data;
import api.node_data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * EdgeTest tests the Edge class.
 * @see api.DWGraph_DS.Edge
 */
public class EdgeTest {

    @Test
    public void getNodesAndWeightTest(){

        edge_data e=new DWGraph_DS.Edge(1,2,0.5);
        assertEquals(1,e.getSrc());
        assertEquals(2,e.getDest());
        assertEquals(0.5,e.getWeight());
    }

    @Test
    public void infoTest(){
        edge_data e=new DWGraph_DS.Edge(1,1,1);
        e.setInfo("Black");

        assertEquals("Black",e.getInfo());

        e.setInfo("White");
        assertEquals("White",e.getInfo());

    }

    @Test
    public void tagTest(){
        edge_data e=new DWGraph_DS.Edge(1,1,1);
        e.setTag(5);

        assertEquals(5,e.getTag());

        e.setTag(12);
        assertEquals(12,e.getTag());
    }
}
