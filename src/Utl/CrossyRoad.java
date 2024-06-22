/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Utl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author jimen
 */
public class CrossyRoad extends JFrame{
    private Image buffer; 
    private BufferedImage bufferPixel;
    private Graphics graphicBuffer;
    
    private static final int WIDTH = 400;
    private static final int HEIGHT = 700;
    
    private Color currentColor;
    
    public static void main(String[] args) {
        new CrossyRoad();
    }
    
    public CrossyRoad(){
        bufferPixel = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        
        setTitle("SegundoParcialGraficas");
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        currentColor = Color.BLACK;
    }
    
    @Override
    public void paint(Graphics g){
        update(g); 
    }
    
    public void update(Graphics g)
    {      
        g.setClip(0, 0, WIDTH, HEIGHT);
        
        

        buffer = createImage(WIDTH, HEIGHT);
        graphicBuffer = buffer.getGraphics();
        graphicBuffer.setClip(0, 0, WIDTH, HEIGHT);
        graphicBuffer.drawImage(buffer, 0, 0, this);
        
        g.drawImage(buffer, 0, 0, this);
    }
    
}
