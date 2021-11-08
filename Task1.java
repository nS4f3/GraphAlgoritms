import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;



class Node {
	
	public int fval = 0;
	public ArrayList<Integer> values;
	public ArrayList<Integer> dest;
	
	public Node(ArrayList<Integer> init,ArrayList<Integer> des) {
		this.values = init;
		this.dest = des;
	}
	
	public int getF() {
		return this.fval;
	}
	
	
	public void calcF() {
		
		
		for(int i = 0; i<values.size();i++) {
			int v = this.values.get(i);
			int d = this.dest.get(i);
			int res = v -d ;
			if(res < 0) {
				res = -res;
			}
			
			this.fval += res;
			
			
		}
		
	}
	
	private int wheel(int val,int calc,boolean up) {
		int res = 0;
		
		if(up == true) {
			if(val+calc >= 10) {
				res = val + calc - 10;
			}
			else {
				res = val + calc;
			}
			
		}
		else {
			if(val-calc < 0) {
				res = val - calc + 10;
			}
			else {
				res = val - calc;
			}
		}
		
		
		return res;
		
	}
	
	
	public ArrayList<Node> generateChild(){
		ArrayList<Node> children = new ArrayList<Node>();
		
		for(int i = 0; i<this.values.size();i++) {
			ArrayList<Integer> upper = new ArrayList<Integer>(this.values);
			ArrayList<Integer> lower = new ArrayList<Integer>(this.values);
			upper.set(i, this.wheel(upper.get(i), 1,true));
			lower.set(i, this.wheel(lower.get(i), 1,false));
			
			
			Node n1 = new Node(upper,this.dest);
			Node n2 = new Node(lower,this.dest);
			n1.calcF();
			n2.calcF();
			
			children.add(n1);
			children.add(n2);
			
			
			
		}
		
		Collections.sort(children, Comparator.comparing(Node::getF));
		return children;
	}
		
	

}

public class Task1 {
	
	
	public static void main(String[] args) {
		ArrayList<Integer> init = new ArrayList<Integer>();
		ArrayList<Integer> dest = new ArrayList<Integer>();
		
		ArrayList<ArrayList<Integer>> forbidden = new ArrayList<ArrayList<Integer>>();
		
		String filename = "inp2.txt";
		
		String fll[] = null;
		String sll[] = null;
		
		
		if(args.length > 1) {
			filename = args[1];
		}
		
		try {
		      File myObj = new File(filename);
		      Scanner myReader = new Scanner(myObj);
		      String fl = myReader.nextLine();
		      fll = fl.split(" ");
		      String sl = myReader.nextLine();
		      sll = sl.split(" ");
		      int forb = Integer.parseInt(myReader.nextLine());
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        String datas[] = data.split(" ");
		        ArrayList<Integer> arr = new ArrayList<Integer>();
		        for(String s : datas) {
		        	arr.add(Integer.parseInt(s));
		        	
		        	
		        }
		        forbidden.add(arr);
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		
		for(String i : fll) {
			init.add(Integer.parseInt(i));
			
		}
		
		for(String i : sll) {
			dest.add(Integer.parseInt(i));
		}
		Node n = new Node(init,dest);
		n.calcF();
		
		int steps = 0;
		Node curr = n;
		
		
		
		/*
		
		ArrayList<Integer> f1= new ArrayList<Integer>(Arrays.asList(8,0,5,7));
		ArrayList<Integer> f2= new ArrayList<Integer>(Arrays.asList(8,0,4,7));
		ArrayList<Integer> f3= new ArrayList<Integer>(Arrays.asList(5,5,0,8));
		ArrayList<Integer> f4= new ArrayList<Integer>(Arrays.asList(7,5,0,8));
		ArrayList<Integer> f5= new ArrayList<Integer>(Arrays.asList(6,4,0,8));
		
		forbidden.add(f1);
		forbidden.add(f2);
		forbidden.add(f3);
		forbidden.add(f4);
		forbidden.add(f5);
		*/
		
		
		ArrayList<Node> children = curr.generateChild();

		
		while(true) {
			boolean avaliable = true;
			
			if(curr.fval == 0) {
				System.out.println(steps);
				break;
			}
			else {
				for(ArrayList<Integer> a : forbidden) {
					if(a.equals(curr.values)) {
						avaliable = false;
						
					}
					
					
				}
				
				if(avaliable) {
					
					children = curr.generateChild();
					curr = children.get(0);
					steps ++;
					
					
					
					
					
					
				}
				else {
					children.remove(0);
					if(children.size() == 0) {
						System.out.println(-1);
						break;
					}
					curr = children.get(0);
					steps --;
				}
				
				
			}
			
			
			
			
			
			
		}
	}
 
}
