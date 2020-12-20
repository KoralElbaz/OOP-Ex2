package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import api.game_service;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class MyPanel extends JPanel {
    private game_service game;
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private Graphics2D g2D;
    private Image agent, pokemon, background, info, remote;

    public MyPanel(Arena ar) {

        this._ar = ar;
        this.agent = new ImageIcon("./data/ash.png").getImage();
        this.pokemon = new ImageIcon("./data/pika9.png").getImage();
        this.background = new ImageIcon("./data/backG4.jpg").getImage();
        this.info = new ImageIcon("./data/pokemon2.png").getImage();
        this.remote = new ImageIcon("./data/data.png").getImage();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paint(g);
    }

    public void update() {
        updatePanel();
    }

    public void updatePanel() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
    }

    public void paint(Graphics g) {
        g2D = (Graphics2D) g;
        int w = this.getWidth();//
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        g2D.drawImage(background, 0, 0, w, h, null);

        int x = (this.getWidth() - info.getWidth(null)) / 2;
        g2D.drawImage(info, x + 180, 0, 550, 350, null);

        drawGraph(g);
        drawAgants(g);
        drawPokemons(g);
        drawInfo(g);
        // Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
    }

    private void drawPokemons(Graphics g) {
        g2D = (Graphics2D) g;
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();
            while (itr.hasNext()) {
                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                //  if(f.getType()<0) {g.setColor(Color.orange);}
                if (c != null) {
                    geo_location fp = this._w2f.world2frame(c);
                    g2D.drawImage(pokemon, (int) fp.x() - r, (int) fp.y() - r, 4 * r, 4 * r, null);
                }
            }
        }
    }

    private void drawGraph(Graphics g) {
        g2D = (Graphics2D) g;
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.WHITE);
            drawNode(n, 5, g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g2D.setColor(Color.lightGray);

                drawEdge(e, g2D);
            }
        }
    }

    private void drawEdge(edge_data e, Graphics g) {
        g2D = (Graphics2D) g;
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g2D.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2D.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());

    }

    private void drawNode(node_data n, int r, Graphics g) {
        g2D = (Graphics2D) g;
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.setColor(Color.WHITE);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawAgants(Graphics g) {
        g2D = (Graphics2D) g;
        List<CL_Agent> rs = _ar.getAgents();
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {
                geo_location fp = this._w2f.world2frame(c);
                g2D.drawImage(agent, (int) fp.x() - r, (int) fp.y() - r, 3 * 10, 3 * 14, null);
            }
        }
    }

    private void drawInfo(Graphics g) {
        g2D = (Graphics2D) g;
        List<String> str = _ar.get_info();

        int x0 =this.getWidth() - 250;
        int y0 = this.getHeight() / 20;
        g2D.setFont(new Font("Ariel", Font.BOLD, (this.getHeight() + this.getWidth()) / 120));
       g2D.setColor(Color.WHITE);
        //g2D.drawImage(remote, this.getWidth() - 350, 0, 350, 250, null);

        double j = ((this.getHeight() * this.getWidth()) / 40000);
        int k = 1;
        for (int i = 0; i < str.size(); i++) {
            g2D.drawString(str.get(i), x0 , (int) (y0 + k * j) + 15);
            k++;
        }
        g2D.drawString(_ar.getTime(), x0, y0+12);


    }








    public static class PopUpWin {
        public static int getId()
        {
            int ID=0;
            ImageIcon pika = new ImageIcon("./data/pika1.png");
            String id=(String)JOptionPane.showInputDialog(
                    null,
                    "Please, enter your id number: ",
                    "ID",
                    JOptionPane.QUESTION_MESSAGE,
                    pika,
                    null,
                    ""
            );
            if(id==null) System.exit(0);
            try{
                ID=Integer.parseInt(id);
            }
            catch (Exception e) {
                System.out.println("Invalid value");
            }
            return ID;
        }

        public static int getLevel()
        {
            int LEVEL=0;
            ImageIcon game = new ImageIcon("./data/game2.png");
            String[] options ={"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
            String sen= (String) JOptionPane.showInputDialog(
                    null,
                    "Please enter level: ",
                    "level",
                    JOptionPane.QUESTION_MESSAGE,
                    game,
                    options,
                    options[0]
            );
            if(sen==null) System.exit(0);
            try{
                LEVEL=Integer.parseInt(sen);
            }
            catch (Exception e) {
                System.out.println("Invalid value");
            }
            return LEVEL;
        }

    }
}