package utility;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class GameUtil {
	
	/******************************************************
	 * Creator: Ted Wong
	 * StudentID: 2014012687
	 * Major: Software Engineering
	 * Class: 143
	 * Description: This class is a utility class containing functions for different purposes.
	 *****************************************************/
	
	// write test data to an excel form
	public static void writeDataContent(String t) throws BiffException, IOException, RowsExceededException, WriteException
	{
		String[] temp = t.split(":");
		WritableWorkbook wwb = null;
		Workbook wb = Workbook.getWorkbook(new File("./requirements/wifi_data.xls"));
		OutputStream os = new FileOutputStream("./requirements/wifi_data.xls");
	        // 根据模板文件创建可写的文件，注意这里是createWorkbook(),创建而不是获取
		wwb = Workbook.createWorkbook(os, wb);
		WritableSheet[] sheets = wwb.getSheets();
		Label label;
		int row = sheets[0].getRows();
		Cell[] cells = sheets[0].getRow(row);
		for (int j = 0; j < 9; j++)
		{
			// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
			label = new Label(j, row, temp[j]);
			sheets[0].addCell(label);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
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
	
	/* useless code for now
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  String t = "32:25:22:16:12:20:12:21:30";
		  try {
			writeDataContent(t);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

}
