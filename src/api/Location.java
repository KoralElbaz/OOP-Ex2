package api;

import java.util.StringTokenizer;

/**
 * This class implement interface that represents a geo location <x,y,z>, aka Point3D
 */

public class Location implements geo_location {
    private double _x;
    private double _y;
    private double _z;

    public Location(double x, double y, double z) {
        _x=x;
        _y=y;
        _z=z;
    }
    public Location(geo_location g){
        _x=g.x();
        _y=g.y();
        _z=g.z();
    }
    public Location(String s){
        StringTokenizer st= new StringTokenizer(s,",",false);

        _x=Double.parseDouble(st.nextToken());
        _y=Double.parseDouble(st.nextToken());
        _z=Double.parseDouble(st.nextToken());
    }

    /**
     * Returns the value of X.
     * @return
     */
    @Override
    public double x() {
        return _x;
    }

    /**
     * Returns the value of Y.
     * @return
     */
    @Override
    public double y() {
        return _y;
    }

    /**
     * Returns the value of Z.
     * @return
     */
    @Override
    public double z() {
        return _z;
    }

    /**
     * Returns the distance between two points.
     * @param  g other point location
     * @return
     */
    @Override
    public double distance(geo_location g) {
        double disX= Math.pow(_x-g.x(),2);
        double disY= Math.pow(_y-g.y(),2);
        double disZ= Math.pow(_z-g.z(),2);
        double ans= Math.sqrt(disX+disY+disZ);

        return ans;
    }

    @Override
    public String toString() {
        return _x +","+ _y +","+ _z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return this.toString().equals(o.toString());
    }
}
