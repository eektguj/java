package com.graph.creater;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;



import com.graph.creater.Edge;
import com.graph.creater.Vertex;


import com.graphloader.*;

import com.graph.creater.NetworkGraph;





public class NetworkGraph implements Graph{
	 private Map<Integer, Vertex> vertices;
	
	 //constructor
	 public NetworkGraph() {
	        vertices = new HashMap<Integer, Vertex>();
	    }
	 
	 
	 public NetworkGraph(List<Vertex> allowed) {
	        vertices = new HashMap<Integer, Vertex>();
	        for (Vertex v : allowed) {
	            vertices.put(v.getVal(), new Vertex(v, allowed));
	        }
	    }
	 
	 
	 public boolean contains(int i) {
	        return vertices.containsKey(i);
	    }
	 
	 public boolean insert(int i) {
	        if (this.contains(i)) {
	            return false;
	        }
	        Vertex v = new Vertex(i);
	        vertices.put(i, v);
	        return true;
	    }
	 
	 public Vertex getVertex(int i) {
	        return vertices.get(i);
	    }

	@Override
	public void addVertex(int i) {
		// TODO Auto-generated method stub
		insert(i);
	}
	@Override
	public void addEdge(int from, int to) {
		// TODO Auto-generated method stub
		 this.getVertex(from).addEdge(this.getVertex(to));
	}

	
	 public Map<Integer, Vertex> getVertices() {
	        return vertices;
	    }
	
	public  void printGraph(NetworkGraph graph) {
        Map<Integer, Vertex> vertices = graph.getVertices();
        
        for (Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
            Vertex v = entry.getValue();

            System.out.print("\n"+v.getVal() + " : ");
            
            for(Edge edge : v.getEdges()) {
                System.out.print(edge.getOtherVertex(v).getVal() + " ");
            }
            System.out.print("\n");
        }
    }
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
        HashMap<Integer, HashSet<Integer>> ret = new HashMap<Integer, HashSet<Integer>>();
       
        
        for (Map.Entry<Integer, Vertex> pair : vertices.entrySet()) {
            HashSet<Integer> val = new HashSet<Integer>();
            List<Edge> edges = pair.getValue().getEdges();
            for (Edge e : edges) {
                val.add(e.getV2().getVal());
            }
            ret.put(pair.getKey(), val);
        }
        return ret;
    }
	public void readEdges(String file) {
        Scanner sc;
        try {
            sc = new Scanner(new File(file));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine().replaceAll(":", "");
            Scanner numSc = new Scanner(line);
            int i1 = numSc.nextInt();
            this.insert(i1);
            while (numSc.hasNextInt()) {
                int i2 = numSc.nextInt();
                this.insert(i2);
                this.getVertex(i1).addEdge(this.getVertex(i2));
            }
            numSc.close();
        }
        sc.close();
    }
	 
	  public Graph getEgonet(int center) {
	        Vertex start = getVertex(center);
	        List<Vertex> friends = new ArrayList<Vertex>();
	        for (Edge e : start.getEdges()) {
	            friends.add(e.getV2());
	        }
	        return new NetworkGraph(friends);
	    }
	  
	  // SCC FINDING ===================================================
	  public static NetworkGraph getReverseGraph(NetworkGraph g) {
		  NetworkGraph gReverse = new NetworkGraph();
	        Vertex other  = null;

	        for (Map.Entry<Integer, Vertex> entry : g.getVertices().entrySet()) {
	            Vertex v = entry.getValue();
	            gReverse.insert(v.getVal());
	            
	            for(Edge edge : v.getEdges()) {
	                other = edge.getOtherVertex(v);
	                gReverse.insert(other.getVal());

	                gReverse.getVertex(other.getVal())
	                    .addEdge(gReverse.getVertex(v.getVal()));

	            }       
	        }

	        return gReverse;
	    }
	// Clears vertex information such as visited, layer, etc.
	    public void resetVertices() {
	        for (Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
	            Vertex v = entry.getValue();
	            v.reset();
	        }
	    }

	    public void resetEdges() {
	        for (Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
	            Vertex v = entry.getValue();
	            for (Edge e : v.getEdges()) {
	                e.setFlow(0);
	            }
	        }
	    }
	  public void dfs(List<Vertex> postList) {
	        resetVertices();
	        Integer clk = 0;
	        
	        for (Map.Entry<Integer, Vertex> entry : getVertices().entrySet()) {
	            Vertex v = entry.getValue();
	            if(!v.visited) {
	                explore(v, postList, clk);
	            }
	        }
	    }

	    public List<Graph> getSCCs() {
	        List<Graph> sccs = new ArrayList<Graph>();
	        NetworkGraph gReverse = NetworkGraph.getReverseGraph(this);
	        List<Vertex> postList = new ArrayList<Vertex>();

	        gReverse.dfs(postList);

	        this.resetVertices();

	        for(Vertex vertex : postList) {
	            // maybe use getVertex
	            if(!getVertex(vertex.getVal()).visited) {
	                sccs.add(findSCC(vertex.getVal()));
	            }
	        }

	        return sccs;
	    }

	    public static Comparator<Vertex> valComparator = new Comparator<Vertex>() {
	        public int compare(Vertex v1, Vertex v2) {
	            return v1.getVal() - v2.getVal();
	        }
	    };

	    // finds SCC given a valid vertex
	    private Graph findSCC(int val) {
	        

	        Graph scc = new NetworkGraph();
	        Stack<Vertex> stack = new Stack<Vertex>();
	        Vertex working = null;
	        
	        stack.push(getVertex(val));
	        while(!stack.empty()) {
	            working = stack.pop();

	            if(!working.visited) {
	                working.visited = true;
	                
	                scc.addVertex(working.getVal());


	                for(Edge edge : working.getEdges()) {
	                    if(!edge.getOtherVertex(working).visited) {
	                        stack.push(edge.getOtherVertex(working));
	                    }
	                }
	            }
	        }


	        return scc;
	    }
	    /**
	     * performs dfs explore from start Vertex
	     * fills postList with vertices in order of decreasing post number
	     *
	     */
	    public void explore(Vertex start, List<Vertex> postList, Integer clk) {
	        Stack<Vertex> vStack = new Stack<Vertex>();


	        Vertex working;

	        // add start vertex to stack
	        vStack.push(start);
	        while(!vStack.empty()) {
	            working = vStack.pop();

	            if(!working.visited) {
	                // push back on stack so post number will get set
	                vStack.push(working);
	                working.visited = true;
	                working.pre = clk;
	            }
	            else if(working.post == 0) {

	                working.post = clk;
	                postList.add(0, working);
	                
	            }

	            clk++;

	            for(Edge edge : working.edges) {
	                if(!edge.getOtherVertex(working).visited) {
	                    vStack.push(edge.getOtherVertex(working));
	                }
	            }
	        }       
	    }
	    
	    public void findFlow() {
	        // so in-place sort doesn't mess up old order
	        List<Vertex> vertexCopy = new ArrayList<Vertex>(((HashMap<Integer, Vertex>)vertices).values());
	        Collections.sort(vertexCopy);
	        for (Vertex v : vertexCopy) {
	            for (Vertex p : v.parents) {
	                float flowAdd = v.flow * (float)(p.pathCount / v.pathCount);
	                p.flow += flowAdd;
	                Edge e = v.getEdge(p);
	                e.flow += flowAdd;
	            }
	        }
	    }
	    
	    
	 // EASY QUESTION ======================================
	    public List<Vertex> findPossibleFriends (int vertex) {
	        Vertex start = getVertex(vertex);
	        List<Vertex> ret = new ArrayList<Vertex>();
	        List<Vertex> neighbors = start.getNeighbors();
	        resetVertices();
	        // Loop through all the neighbors, finding neighbors of
	        // neighbors who aren't friends with the starting vertex
	        for (Vertex v : neighbors) {
	            v.visited = true;
	        }
	        for (Vertex v : neighbors) {
	            for (Vertex v2 : v.getNeighbors()) {
	                if (!v2.visited && (v2 != start)) {
	                    ret.add(v2);
	                    v2.visited = true;
	                }
	            }
	        }
	        System.out.println("Neighbors size: " + neighbors.size());
	        System.out.println("Ret size: " + ret.size());
	        return ret;

	    }
	    
	    
	    
	    public static void main(String[] args) {
	        String filename = "SCC.txt";
	        NetworkGraph  g = new NetworkGraph();
	      com.graphloader.GraphLoader.loadGraph(g, "src/com/graph/ssc/" + filename);
	      
	      List<Vertex> ret = g.findPossibleFriends(1);
	        for (Vertex v : ret) {
	            System.out.println(v.getVal());
	        }
	    }
}
