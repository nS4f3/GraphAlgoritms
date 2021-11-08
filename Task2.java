import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Edge{
	public int value;
	public ArrayList<Edge> neeighbor;
	public int f;
	
	public Edge(int v) {
		this.value =v ;
		this.neeighbor = new ArrayList<Edge>();
		
	}
	
	public void addNeig(Edge neig) {
		this.neeighbor.add(neig);
		neig.neeighbor.add(this);
		
		
	}
	
	public void removeNeig(Edge neig) {
		this.neeighbor.remove(neig);
		neig.neeighbor.remove(this);
	}

	public int getF() {
		return this.f;
	}
	public void calcF(int initial, int col) {
		if(this.neeighbor.size()>0) {
			for(Edge e : this.neeighbor) {
				
				int v = e.value - initial;
				if(v < 0) {
					v = -v;
				}
				if(e.value == initial) {
					e.f = 2;
				}
				else if((v)%col == 0 ) {
					e.f = 1;
				}
				else if((v) < col) {
					e.f = 1;
				}
			}
		}
		
	}
	
}


class Graph{
	ArrayList<Edge> edges = new ArrayList<Edge>();
	public int cols;
	public int rows;
	
	public Graph(int col,int row) {
		this.cols = col;
		this.rows = row;
		for(int i = 0;i<row*col;i++) {
			Edge e = new Edge(i);
			edges.add(e);
			
		}
	
	}
	
	public void ConnetEdges(String indicNow, int point) {
		if(indicNow.equals("/")) {
			edges.get(point).addNeig(edges.get(point+this.cols-1));
			
		}
		else {
			edges.get(point).addNeig(edges.get(point+this.cols+1));
		}
		
		
		
	}
}





public class Task2 {
	
	public static Graph conn(String filename) {
		Graph grap = null;
		try {
		      File myObj = new File(filename);
		      Scanner myReader = new Scanner(myObj);
		      String datafirst = myReader.nextLine();
 		      String[] colrow = datafirst.split(" ");
 		      int col = Integer.parseInt(colrow[0]) +1;
 		      int row = Integer.parseInt(colrow[1]) +1;
 		      
 		      grap = new Graph(col,row);
 		      
 		      int point = -1;
 		      boolean lastback = false;
 		      
		      while (myReader.hasNextLine()) {
		    	if(lastback) {
		    		point+=2;
		    	}
		    	else {
		    		point++;
		    	}
		        String data[] = myReader.nextLine().split("");
		        if(data[data.length-1].equals("\\")) {
		        	lastback=true;
		        }
		        else {
		        	lastback =false;
		        }
		        String before = "";
		        int indic = 0;
		        for(String s : data) {
		        	
		        	if(indic == 0 && s.equals("\\")) {

		        		indic ++;
		        		grap.ConnetEdges(s, point);
		        		before = s;
		        		
		        	}
		        	else if(indic == 0 && s.equals("/")){

		        		indic++;
		        		point++;
		        		grap.ConnetEdges(s, point);
		        		before = s;
		        		
		        	}
		        	else {
		        		indic++;
		        		if(before.equals(s)) {
			        		point ++;
			        		grap.ConnetEdges(s, point);
			        	}
			        	else if(!before.equals(s) && s.equals("/")) {
			        		point += 2;
			        		grap.ConnetEdges(s, point);
			        		before = s;
			        		
			        	}
			        	else if(!before.equals(s) && s.equals("\\")) {
			        		grap.ConnetEdges(s, point);
			        		before = s;
			        	}
		        	}
		        	
		        }
		        
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		return grap;
	}
	
	public static ArrayList<Edge> hasExit(Edge edge,Edge before,int col, int initial) {
		ArrayList<Edge> exits = new ArrayList<Edge>();
		edge.calcF(initial, col);
		edge.neeighbor.sort(Comparator.comparing(Edge::getF).reversed());
		if(edge.neeighbor.size() > 0) {
			for(Edge e : edge.neeighbor) {
				if(e.value != before.value) {
					exits.add(e);
				}
			}
		}
		exits.sort(Comparator.comparing(Edge::getF).reversed());
		return exits;
	}
	
	
	public static void findCircle(Graph grap,int initial,int pointer, int bee,ArrayList<Edge> passed,int col) {
		while(true) {
			if(grap.edges.get(pointer).neeighbor.size() == 0 || (pointer == initial && bee!= 0)) {
				break;
			}
			else {
				ArrayList<Edge> ext = hasExit(grap.edges.get(pointer),grap.edges.get(bee), col,initial);
				
				if(ext.size() == 0) {
					grap.edges.get(pointer).removeNeig(grap.edges.get(bee));
					passed.clear();
					findCircle(grap,initial,initial,0,passed,col);
					
					
					
				}
				else if(passed.indexOf(grap.edges.get(pointer)) != -1) {
					grap.edges.get(initial).removeNeig(grap.edges.get(initial).neeighbor.get(0));
					passed.clear();
					break;
				}
				else {
					passed.add(grap.edges.get(pointer));
					findCircle(grap,initial,ext.get(0).value,pointer,passed,col);
					break;
					
				}
				
			}
		}
		
		
		
		
		
		
		
		
	}
	public static void main(String args[]) {
		
		String filename = "inp.txt";
		
		if(args.length > 1) {
			filename = args[1];
		}

		
		Graph g = conn(filename);
		

		
		Map<Integer,ArrayList<Edge>> circles = new HashMap<Integer,ArrayList<Edge>>();
		
		for(int i = 0; i< g.cols * (g.rows-1);i++) {
			ArrayList<Edge> pas = new ArrayList<Edge>();
			findCircle(g,i,i,0,pas,g.cols);

			
			if(pas.size()>0) {
				circles.put(pas.size(), pas);
			}
		}
		
		
		if(circles.size() > 0) {
			ArrayList<Integer> keys = new ArrayList<Integer>(circles.keySet());
			Collections.sort(keys);
			Collections.reverse(keys);
			
			if(keys.get(0)>4) {
				System.out.println(circles.size() + " Cycles; the longest has lenght " + (keys.get(0)-2)*2);
			}
			else {
				System.out.println(circles.size() + " Cycles; the longest has lenght " + (keys.get(0)));
			}
		}
		else {
			System.out.println("There are no cycles.");
		}
		
		
		
		
		
		
		
	}

}
