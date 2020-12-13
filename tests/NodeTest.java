import api.DWGraph_DS;
import api.Location;
import api.geo_location;
import api.node_data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {

    @Test
    public void getKeyTest(){
        node_data n=new DWGraph_DS.Node(3);
        assertEquals(3,n.getKey());
    }

    @Test
    public void geoLocationTest(){
        node_data n=new DWGraph_DS.Node(3);
        geo_location g= new Location(1,2,3);
        n.setLocation(g);

        assertEquals(g,n.getLocation());

        geo_location other= new Location (3,4,5);
        n.setLocation(other);
        assertEquals(other,n.getLocation());

    }

    @Test
    public void infoTest(){
        node_data n=new DWGraph_DS.Node(3);
        n.setInfo("Black");

        assertEquals("Black",n.getInfo());

        n.setInfo("White");
        assertEquals("White",n.getInfo());

    }

    @Test
    public void tagTest(){
        node_data n=new DWGraph_DS.Node(3);
        n.setTag(5);

        assertEquals(5,n.getTag());

        n.setTag(12);
        assertEquals(12,n.getTag());
    }




}
