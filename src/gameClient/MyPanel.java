package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class MyPanel extends JPanel {
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private Graphics2D g2D;
    private Image agent;
    private Image pokemon;
    private Image background;

    public MyPanel(Arena ar)
    {
        this._ar=ar;
        this.agent=new ImageIcon("C:\\Users\\Koral Elbaz\\Desktop\\Koral Document\\PicGame\\bad.png").getImage();
        this.pokemon=new ImageIcon("C:\\Users\\Koral Elbaz\\Desktop\\Koral Document\\PicGame\\pikachu.png").getImage();
        this.background=new ImageIcon("C:\\Users\\Koral Elbaz\\Desktop\\Koral Document\\PicGame\\backG2.jpg").getImage();

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paint(g);

    }
    public void update()
    {
        updatePanel();
    }

    public void updatePanel() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }
    public void paint(Graphics g) {
        g2D=(Graphics2D)g;
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        g2D.drawImage(background, 0, 0, w, h, null);
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);
    }

    private void drawPokemons(Graphics g) {
        g2D=(Graphics2D)g;
        List<CL_Pokemon> fs = _ar.getPokemons();
        if(fs!=null)
        {
            Iterator<CL_Pokemon> itr = fs.iterator();
            while(itr.hasNext())
            {
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r=10;
              //  if(f.getType()<0) {g.setColor(Color.orange);}
                if(c!=null)
                {
                    geo_location fp = this._w2f.world2frame(c);
                    g2D.drawImage(pokemon,(int)fp.x()-r, (int)fp.y()-r, 3*r, 3*r,null);
                }
            }
        }
    }

    private void drawGraph(Graphics g) {
        g2D=(Graphics2D)g;
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.black);
            drawNode(n,5,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.black);
                drawEdge(e, g);
            }
        }
    }

    private void drawEdge(edge_data e, Graphics g) {
        g2D=(Graphics2D)g;
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
    }

    private void drawNode(node_data n, int r, Graphics g) {
        g2D=(Graphics2D)g;
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }

    private void drawAgants(Graphics g)
    {
        g2D=(Graphics2D)g;
        List<CL_Agent> rs = _ar.getAgents();
        int i=0;
        while(rs!=null && i<rs.size())
        {
            geo_location c = rs.get(i).getLocation();
            int r=8;
            i++;
            if(c!=null)
            {
                geo_location fp = this._w2f.world2frame(c);
                g2D.drawImage(agent,(int)fp.x()-r, (int)fp.y()-r, 3*r, 3*r,null);
            }
        }
    }

    private void drawInfo(Graphics g) {
        g2D=(Graphics2D)g;
        List<String> str = _ar.get_info();
        String dt = "none";
        for(int i=0;i<str.size();i++)
        {
            g2D.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
        }
    }
}
