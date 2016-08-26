package com.graph.creater;

import com.graphloader.GraphLoader;



public class GraphTest {
	
	public static void main(String[] args){
		NetworkGraph graph = new NetworkGraph();
		
		 GraphLoader.loadGraph(graph, "data/mytest.txt");
	
		 System.out.println("Done");
	}

}
