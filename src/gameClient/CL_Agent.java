package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * This class build single agent
 */
public class CL_Agent {
	public static final double EPS = 0.0001;
	private static int _count = 0;
	private static int _seed = 3331;
	private int _id;
	private geo_location _pos;
	private double _speed;
	private edge_data _curr_edge;
	private node_data _curr_node;
	private directed_weighted_graph _gg;
	private CL_Pokemon _curr_fruit;
	private long _sg_dt;
	private double _value;


	public CL_Agent(directed_weighted_graph g, int start_node) {
		_gg = g;
		setMoney(0);
		this._curr_node = _gg.getNode(start_node);
		_pos = _curr_node.getLocation();
		_id = -1;
		setSpeed(0);
	}
	public void update(String json) {
		JSONObject line;
		try {
			line = new JSONObject(json);
			JSONObject ttt = line.getJSONObject("Agent");
			int id = ttt.getInt("id");
			if(id==this.getID() || this.getID() == -1) {
				if(this.getID() == -1) {_id = id;}
				double speed = ttt.getDouble("speed");
				String p = ttt.getString("pos");
				Point3D pp = new Point3D(p);
				int src = ttt.getInt("src");
				int dest = ttt.getInt("dest");
				double value = ttt.getDouble("value");
				this._pos = pp;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(value);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the key of the src_Node of the agent edge.
	 * @return
	 */

	public int getSrcNode() {return this._curr_node.getKey();}
	public String toJSON() {
		int d = this.getNextNode();
		String ans = "{\"Agent\":{"
				+ "\"id\":"+this._id+","
				+ "\"value\":"+this._value+","
				+ "\"src\":"+this._curr_node.getKey()+","
				+ "\"dest\":"+d+","
				+ "\"speed\":"+this.getSpeed()+","
				+ "\"pos\":\""+_pos.toString()+"\""
				+ "}"
				+ "}";
		return ans;
	}
	private void setMoney(double v) {_value = v;}

	/**
	 * Allows changing the agent next node he is going to.
	 * @param dest the next node.
	 * @return
	 */
	public boolean setNextNode(int dest) {
		boolean ans = false;
		int src = this._curr_node.getKey();
		this._curr_edge = _gg.getEdge(src, dest);
		if(_curr_edge!=null) {
			ans=true;
		}
		else {_curr_edge = null;}
		return ans;
	}


	/**
	 * Allows changing the agent current node
	 * @param src the curr node.
	 * @return
	 */
	public void setCurrNode(int src) {
		this._curr_node = _gg.getNode(src);
	}

	/**
	 * Returns if agent is moving
	 * @return
	 */
	public boolean isMoving() {
		return this._curr_edge!=null;
	}
	public String toString() {
		return toJSON();
	}
	public String toString1() {
		String ans=""+this.getID()+","+_pos+", "+isMoving()+","+this.getValue();
		return ans;
	}

	/**
	 * Returns agent id.
	 * @return
	 */
	public int getID() {
		// TODO Auto-generated method stub
		return this._id;
	}

	/**
	 * Returnes agent position.
	 * @return
	 */
	public geo_location getLocation() {
		// TODO Auto-generated method stub
		return _pos;
	}


	/**
	 * Returnes agent value.
	 * @return
	 */
	public double getValue() {
		// TODO Auto-generated method stub
		return this._value;
	}


	/**
	 * Returns agent next node.
	 * @return
	 */
	public int getNextNode() {
		int ans = -2;
		if(this._curr_edge==null) {
			ans = -1;}
		else {
			ans = this._curr_edge.getDest();
		}
		return ans;
	}

	/**
	 * Returns agent speed.
	 * @return
	 */
	public double getSpeed() {
		return this._speed;
	}

	/**
	 * Allows changing the agent current node
	 * @param v the desired speed.
	 */
	public void setSpeed(double v) {
		this._speed = v;
	}

	/**
	 * Returns the current pokemon that agent go to.
	 * @return
	 */
	public CL_Pokemon get_curr_fruit() {
		return _curr_fruit;
	}

	/**
	 * Allows changing the current pokemon that agent go to.
	 * @param curr_fruit the desired pokemon
	 */
	public void set_curr_fruit(CL_Pokemon curr_fruit) {
		this._curr_fruit = curr_fruit;
	}

	public void set_SDT(long ddtt) {
		long ddt = ddtt;
		if(this._curr_edge!=null) {
			double w = get_curr_edge().getWeight();
			geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
			geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();
			double de = src.distance(dest);
			double dist = _pos.distance(dest);
			if(this.get_curr_fruit().get_edge()==this.get_curr_edge()) {
				dist = _curr_fruit.getLocation().distance(this._pos);
			}
			double norm = dist/de;
			double dt = w*norm / this.getSpeed();
			ddt = (long)(1000.0*dt);
		}
		this.set_sg_dt(ddt);
	}

	/**
	 * Returns the current edge the agent is on.
	 * @return
	 */
	public edge_data get_curr_edge() {
		return this._curr_edge;
	}

	/**
	 * Allows changing the current edge the agent is on.
	 * @param e desired edge
	 */
	public void set_curr_edge(edge_data e) {
		_curr_edge=e;
	}
	public long get_sg_dt() {
		return _sg_dt;
	}
	public void set_sg_dt(long _sg_dt) {
		this._sg_dt = _sg_dt;
	}


}