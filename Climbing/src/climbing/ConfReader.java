package climbing;

import java.io.*;
import java.util.*;

public class ConfReader {

	ArrayList<Pnt> pointList;
	Pnt endPoint;
	String title;
	ArrayList<Integer> manPoint;
	boolean isManDeclared = false;
	
	public ConfReader()
	{
		pointList = new ArrayList<Pnt>();
		endPoint = new Pnt(800.0,800.0);
		title = "";
	}
	
	public ArrayList<Pnt> getPointList() { return pointList; }
	public Pnt getBoundRectPnt() { return endPoint; }
	
	public boolean parse(File file)
	{
		boolean isError = false;
		
		try{
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String line;
			int lineCount = 0;
			
			while( (line = br.readLine()) != null)
			{
				lineCount++;
				line = line.trim();
				if( line.length() == 0) continue;
				if( line.startsWith("!")) continue;  // remove comment line
				
				if( line.indexOf("!") > 0 ) // remove comment in the line
				{
					line = line.substring(0,line.indexOf("!"));
					line = line.trim();
				}
				
				if( !line.startsWith("{")  || !line.endsWith("}"))
				{
					System.out.printf("ERROR! Line Number %d. Each meaningful line must have { and } .\n", lineCount);
					isError = true;
					break;
				}
				
				line = line.substring(1, line.length()-1).trim(); // remove brace
				
				int firstComma = line.indexOf(",");
				String type = line.substring(0, firstComma);
				String arg  = line.substring(firstComma+1).trim();
				
				switch(type.toUpperCase())
				{
				    case "TITLE" :
				    	procTitle(arg, lineCount);
				    	break;
					case "RECT" :
						procRect(arg, lineCount);
						break;
				
					case "POINT" :
						procPoint(arg, lineCount);
						break;
						
					case "MAN" :
						procMan(arg, lineCount);
						break;

					default:
						System.out.printf("ERROR Line %d : Undefined Type..:%s\n", lineCount, line);
				   
				}
				
			}  // end of while
			
			
			br.close();
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
			isError = true;
		}
		
		return !isError;
	}
	
	
	public String getTitle() { return title; }
	
	private void procTitle(String arg, int lineCount)
	{
		title = arg; 
	}
	
	private void procRect(String arg, int lineCount)
	{
		StringTokenizer st = new StringTokenizer(arg, ",");
		if( st.countTokens() != 2) {
			System.out.printf("ERROR Line %d : Tokens are not valid : %s\n", lineCount, arg);
			return;
		}
		
		String rectXStr = st.nextToken().trim();
		double rectX = 0;
		
		try{
			rectX = Double.parseDouble(rectXStr);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.printf("ERROR Line %d : Wrong rectX value : %s\n", lineCount, rectXStr);
			return;
		}
		
		String rectYStr = st.nextToken().trim();
		double rectY = 0;
		
		try{
			rectY = Double.parseDouble(rectYStr);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.printf("ERROR Line %d : Wrong rectY value : %s\n", lineCount, rectYStr);
			return;
		}
		 
		endPoint = new Pnt(rectX, rectY);		
	}
	
	
	private void procPoint(String arg, int lineCount)
	{
		StringTokenizer st = new StringTokenizer(arg, ",");
		if( st.countTokens() != 2) {
			System.out.printf("ERROR Line %d : Tokens are not valid : %s\n", lineCount, arg);
			return;
		}
		
		String rectXStr = st.nextToken().trim();
		double rectX = 0;
		
		try{
			rectX = Double.parseDouble(rectXStr);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.printf("ERROR Line %d : Wrong rectX value : %s\n", lineCount, rectXStr);
			return;
		}
		
		String rectYStr = st.nextToken().trim();
		double rectY = 0;
		
		try{
			rectY = Double.parseDouble(rectYStr);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.printf("ERROR Line %d : Wrong rectY value : %s\n", lineCount, rectYStr);
			return;
		}
		 
		pointList.add(new Pnt(rectX, rectY));		
	}
	
	
	private void procMan(String arg, int lineCount)
	{
		StringTokenizer st = new StringTokenizer(arg, ",");
		if( st.countTokens() != 4) {
			System.out.printf("ERROR Line %d : Tokens are not valid : %s\n", lineCount, arg);
			return;
		}
		
		manPoint = new ArrayList<Integer>();
		
		for(int i=0;i<4;i++)
		{
		
			String valueStr = st.nextToken().trim();
			int value = 0;
			
			try{
				value = Integer.parseInt(valueStr);
			} catch (Exception e)
			{
				e.printStackTrace();
				System.out.printf("ERROR Line %d : Wrong index : %s\n", lineCount, valueStr);
				return;
			}
			
			manPoint.add(value);
		}
		
		isManDeclared = true;	
	}
	
	public ArrayList<Integer> getMan()
	{
		if( isManDeclared )  return manPoint;
		else return null;
	}
	 
}
