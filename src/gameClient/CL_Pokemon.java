package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.Objects;

/**
 * This class build single pokemon
 */
public class CL_Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	private  boolean isVisit=false;

	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
		//	_speed = s;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
	}

	/**
	 * Returns if Is the Pokemon marked by an agent.
	 * @return
	 */
	public boolean getIsVisit() {
		return this.isVisit;
	}

	/**
	 * Signifies that Pokemon is a target.
	 * @return
	 */

	public void setIsVisit(boolean b) {
		this.isVisit = b;
	}

	/**
	 * Build a pokemon.
	 * @param json
	 * @return new pokemon.
	 */
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	public edge_data get_edge() {
		return _edge;
	}

	/**
	 * Allows changing the pokemon edge.
	 * @param _edge
	 */
	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	/**
	 * Returns the pokemon position.
	 * @return
	 */
	public Point3D getLocation() {
		return _pos;
	}

	/**
	 * Returns the pokemon type,
	 * 1 if he is on up edge, -1 if on down edge.
	 * @return
	 */

	public int getType() {return _type;}

	/**
	 * Returns the pokemon value.
	 * @return
	 */
	public double getValue() {
		return _value;
	}


	/**
	 * Returns the key of the src_Node of the Pokemon edge.
	 * @return
	 */

	public int getSrc(){return _edge.getSrc();}

	/**
	 * Returns the key of the dest_Node of the Pokemon edge.
	 * @return
	 */
	public int getDest(){return _edge.getDest();}


	@Override
	public int hashCode() {
		return Objects.hash(_edge, _value, _type, _pos, min_dist, min_ro, isVisit);
	}
}