import api.geo_location;
import org.junit.jupiter.api.Test;
import api.geo_location;
import api.Location;
import static org.junit.jupiter.api.Assertions.*;

/**
 * LocationTest tests the Location class.
 * @see Location
 */
public class LocationTest {

    @Test
    public void StringConstractor_Test(){
        geo_location g= new Location("5,2,3.5");

        assertEquals(5.0,g.x());
        assertEquals(2.0,g.y());
        assertEquals(3.5,g.z());
    }
    @Test
    public void CopyConstractor_Test(){

        geo_location g= new Location("5,2,3.5");
        geo_location copy= new Location(g);
        assertEquals(g.x(),copy.x());
        assertEquals(g.y(),copy.y());
        assertEquals(g.z(),copy.z());
    }

    @Test
    public void Constractor_Test(){
        geo_location g= new Location(1,0.5,115);
        assertEquals(1,g.x());
        assertEquals(0.5,g.y());
        assertEquals(115,g.z());
    }

    @Test
    public void distanceTest(){
        geo_location g= new Location(0,0,0);
        geo_location g1= new Location(0,0,0);
        assertEquals(0, g.distance(g1));

        geo_location g2= new Location(1,2,3);
        assertEquals( Math.sqrt(14), g.distance(g2));

    }

}
