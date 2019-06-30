package unused;

import utility.coordinates;

public class FireCalibration {
	
	/*******************************************
	 this class is deserted and only left for backup purposes
	********************************************/
    /*                                                
	public static boolean IsHit(float degrees,float shooterx,float shootery,float hita,float hitb)
	{   if(degrees == 0)
	{
		if(shooterx==hita&&hitb>=shootery)
		{
			return true;
		}
	}
	else if (degrees ==90)
	{
		if(shootery==hitb&&hita>=shooterx)
		{
			return true;
		}
	}
	else if(degrees == 180)
	{
		if(shooterx==hita&&hitb<=shootery)
		{
			return true;
		}
	}
	else if (degrees==270)
		if(shootery==hitb&&hita<=shooterx)
		{
			return true;
		}
	float A = 0;
	if(degrees>=0&&degrees<=90)
	{
		A = 1/(float) Math.tan(Math.toRadians(degrees));	

	}
	else if(degrees>90&&degrees<=180)
	{
		A = -1/(float) Math.tan(Math.toRadians(180-degrees));	
	}
	else if(degrees>180&&degrees<=270)
	{
		A = (float) Math.tan(Math.toRadians(270-degrees));	
	}
	else if(degrees>270&&degrees<=360)
	{
		A = -1/(float) Math.tan(Math.toRadians(360-degrees));	
	}
	float B = -1;
	float C = (float) (shootery-Math.tan(Math.toRadians(90-degrees))*shooterx);
	float d = (float) Math.abs((A*hita+B*hitb+C)/Math.sqrt(Math.pow(A, 2)+1));
	if(d<10)
	{

		if(hita>=shooterx&&hitb>=shootery&&degrees>=0&&degrees<=90)
		{
			return true;
		}
		else if(hita>=shooterx&&hitb<=shootery&&degrees>=90&&degrees<=180)
		{
			return true;
		}
		else if(hita<=shooterx&&hitb<=shootery&&degrees>=180&&degrees<=270)
		{
			return true;
		}
		else if(hita<=shooterx&&hitb>=shootery&&degrees>=270&&degrees<=360)
		{
			return true;
		}


	}
	return false;
	}
	
	public static coordinates getlocation(float[][]s)//S consists of s[0] = a,s[1] = b,s[2] = c and s[3] = r
	{
		float[][]f = new float[3][];
		float[]A = new float[4];
		float[]B = new float[4];
		float[]C = new float[4];	
		f[0] = A;
		f[1] = B;
		f[2] = C;
		for(int i=0;i<3;i++)
		{
			f[i][0] = s[1][i]-s[0][i];//x2-x1
			f[i][1] = s[2][i]-s[0][i];//x3-x1
			f[i][2] = s[3][i]-s[0][i];//x4-x1
			f[i][3] = s[2][i]-s[1][i];//x3-x2
		}
		
		float k1,k2,k3,k4;
		//(r1^2-r2^2+a2^2-a1^2+b2^2-b1^2+c2^2-c1^2)/2
		k1 = (float) (Math.pow(s[0][3], 2)-Math.pow(s[1][3], 2)+Math.pow(s[1][0], 2)-Math.pow(s[0][0], 2)+Math.pow(s[1][1], 2)-Math.pow(s[0][1], 2)+Math.pow(s[1][2], 2)-Math.pow(s[0][2], 2))/2;
		//(r1^2-r3^2+a3^2-a1^2+b3^2-b1^2+c3^2-c1^2)/2
		k2 = (float) (Math.pow(s[0][3], 2)-Math.pow(s[2][3], 2)+Math.pow(s[2][0], 2)-Math.pow(s[0][0], 2)+Math.pow(s[2][1], 2)-Math.pow(s[0][1], 2)+Math.pow(s[2][2], 2)-Math.pow(s[0][2], 2))/2;
		//(r1^2-r4^2+a4^2-a1^2+b4^2-b1^2+c4^2-c1^2)/2
		k3 = (float) (Math.pow(s[0][3], 2)-Math.pow(s[3][3], 2)+Math.pow(s[3][0], 2)-Math.pow(s[0][0], 2)+Math.pow(s[3][1], 2)-Math.pow(s[0][1], 2)+Math.pow(s[3][2], 2)-Math.pow(s[0][2], 2))/2;
		//(r2^2-r3^2+a3^2-a2^2+b3^2-b2^2+c3^2-c2^2)/2
		k4 = (float) (Math.pow(s[1][3], 2)-Math.pow(s[2][3], 2)+Math.pow(s[2][0], 2)-Math.pow(s[1][0], 2)+Math.pow(s[2][1], 2)-Math.pow(s[1][1], 2)+Math.pow(s[2][2], 2)-Math.pow(s[1][2], 2))/2;
		float[]ks = {k1,k2,k3,k4};
		
		//under normal circumstances
		
		//(A3C4-A4C3)*(C2K1-C1K2)-(A1C2-A2C1)*(K3C4-K4C3)
		float y1 = (A[2]*C[3]-A[3]*C[2])*(C[1]*k1-C[0]*k2)-(A[0]*C[1]-A[1]*C[0])*(k3*C[3]-k4*C[2]);
		//(B1C2-B2C1)(A3C4-A4C3)-(A1C2-A2C1)(B3C4-B4C3)
		float y2 = (B[0]*C[1]-B[1]*C[0])*(A[2]*C[3]-A[3]*C[2])-(A[0]*C[1]-A[1]*C[0])*(B[2]*C[3]-B[3]*C[2]);
		float Y = y1/y2;
		//(C2K1-C1K2-(B1C2-B2C1)Y)/(A1C2-A2C1)
		float X = ((C[1]*k1-C[0]*k2)-(B[0]*C[1]-B[1]*C[0])*Y)/(A[0]*C[1]-A[1]*C[0]);	
		
		//if there are special circumstances involved
		for(int i=0;i<3;i++)
		{
			
			int rzeros = 0;
			int temp = -1;
			for(int j=0;j<3;j++)
			{
				if(f[i][j]==0)
				{
					rzeros++;
				}
				if(f[i][j]!=0)
				{
					temp = j;
				}
			}
			if(rzeros>=2)
			{
				
					switch(temp)
					{
					case(0):
						X = ks[i]/A[i];
						break;
					case(1):
						Y = ks[i]/B[i];
						break;
					default:
						break;
					}
			}
		}
		
		return new coordinates(X,Y);
		
	}
	public static coordinates getcoordinates_2d(float a,float r1,float r2) 
	{
	float temp = a*a-r2*r2+r1*r1;
	temp = temp/(2*a);
	float x = temp;
	float y = (float) Math.pow((r1*r1-x*x), 0.5);
	return new coordinates(x,y);
	
	}
	
	public static float RssiToDistance(float avgrssi)
	{
		float distance = -1;
		//first formula attempt 
		float temp = (float) (avgrssi-42.27);
		distance = (float) (temp/0.0291);
		//second formula attempt
		//distance = (float) (Math.pow(avgrssi, 3)*(-0.046)+Math.pow(avgrssi, 2)*8.0297-avgrssi*433.78 +7442.7);
		return distance;
	}
	
	
	
	
	public static void main(String[] args) {
		
		for(int i=0;i<=360;i++)
		{
			if(IsHit(i,45,45,45,65))
			{
				System.out.println(i);
			}
		}
	//	float s[][] = {{0,0,0,5},{5,0,0,(float)Math.pow(20, 0.5)},{0,5,0,(float)Math.pow(10, 0.5)},{0,0,5,(float)Math.pow(50, 0.5)}};
	//	getlocation(s);

        double degrees = 45.0;
        double radians = Math.toRadians(degrees);

        System.out.format("pi 的值为 %.4f%n", Math.PI);
        System.out.format("%.4f 的反正切值 %.4f 度 %n", Math.cos(radians), Math.toDegrees(Math.atan(Math.sin(radians))));
		System.out.println(Math.toDegrees(Math.atan(1)));

	}
*/
}
