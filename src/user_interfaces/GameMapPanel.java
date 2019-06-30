package user_interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import server_connections.Player_Info_active;
import unused.FireCalibration;

public class GameMapPanel extends JPanel { //1.0 code standard reached. last edit date:2018/3/31
	
	/*******************************************
	this is the game map, it shows the players on the field and gives a visual of the match in progress
	this map is used in the 1.5 update and is an unfinished product
	********************************************/
	
	List<Player_Info_active> plist = new LinkedList<Player_Info_active>();
	public GameMapPanel() {
		// TODO Auto-generated constructor stub
		initlist();
	}
	
	
	public void paint(Graphics g) {
		  // 1.调用父类函数完成初始化
		  // 这句话不能少
		  super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		  BufferedImage src;
		  
		try {
			 
			 for(int i=0;i<plist.size();i++)
			 {
				 src = ImageIO.read(new File("./arrow1.png"));
				 BufferedImage des = Rotate(src, plist.get(i).getDirection());   
				 g2d.drawImage(des, (int)plist.get(i).getLocationx(),(int)plist.get(i).getLocationy(), 24, 24, null, this);
			 }
				  for(int i=0;i<plist.size();i++)
				  {
					  for(int j=i+1;j<plist.size();j++)
					  {
						  /*
						  if(FireCalibration.IsHit(plist.get(i).getDirection(),plist.get(i).getLocationx(),plist.get(i).getLocationy(),plist.get(j).getLocationx(),plist.get(j).getLocationy()))
						  {
							  g2d.setColor(Color.red);
							  g2d.drawLine((int)plist.get(i).getLocationx(), (int)plist.get(i).getLocationy(),(int)plist.get(j).getLocationx(), (int)plist.get(j).getLocationy());
						  
						  }
						  */
					  }
				  }
				 
				
			 // repaint();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  

	}

	  public static BufferedImage Rotate(Image src, float angle) {  
	        int src_width = src.getWidth(null);  
	        int src_height = src.getHeight(null);  
	        // calculate the new image size  
	        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(  
	                src_width, src_height)), angle);  
	  
	        BufferedImage res = null;  
	        res = new BufferedImage(rect_des.width, rect_des.height,  
	                BufferedImage.TYPE_INT_ARGB);  
	        Graphics2D g2 = res.createGraphics();  
	        // transform  
	        g2.translate((rect_des.width - src_width) / 2,  
	                (rect_des.height - src_height) / 2);  
	        g2.rotate(Math.toRadians(angle), src_width / 2, src_height / 2);  
	        g2.drawImage(src, null, null);  
	        return res;  
	    }  
	  
	  public static Rectangle CalcRotatedSize(Rectangle src, float angle) {  
	        // if angel is greater than 90 degree, we need to do some conversion  
	        if (angle >= 90) {  
	            if(angle / 90 % 2 == 1){  
	                int temp = src.height;  
	                src.height = src.width;  
	                src.width = temp;  
	            }  
	            angle = angle % 90;  
	        }  
	  
	        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;  
	        double len = 2 * Math.sin(Math.toRadians(angle) / 2) * r;  
	        double angel_alpha = (Math.PI - Math.toRadians(angle)) / 2;  
	        double angel_dalta_width = Math.atan((double) src.height / src.width);  
	        double angel_dalta_height = Math.atan((double) src.width / src.height);  
	  
	        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha  
	                - angel_dalta_width));  
	        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha  
	                - angel_dalta_height));  
	        int des_width = src.width + len_dalta_width * 2;  
	        int des_height = src.height + len_dalta_height * 2;  
	        return new java.awt.Rectangle(new Dimension(des_width, des_height));  
	    }  
	public void initlist()
	{
		plist.add(new Player_Info_active("test_user",0,5,35,0));
	}


}
