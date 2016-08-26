/**
 * @author UCSD MOOC development team
 *
 * Grader for the SCC assignment.
 *
 */

package com.grader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import com.graphloader.*;

import sun.security.util.Length;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;
import com.graph.creater.*;


public class SCCGrader2  {

	public static void main(String[] args) {
               Graph g = new NetworkGraph();
                Set<Integer> vertices;

                String answerFile = "data/scc_answers/scc/test_5.txt";
                System.out.println("Data loading start");
                //GraphLoader.loadGraph(g, "data/scc/test_11.txt");
               GraphLoader.loadGraph(g, "data/SCC.txt");
                System.out.println("Data loaded");
                // get student SCC result
                List<Graph> graphSCCs = g.getSCCs();
                
                
                
                System.out.println("result  loaded");
                int mynum= graphSCCs.size();
                System.out.println(mynum);
                
                List<Set<Integer>> sccs = new ArrayList<Set<Integer>>();
                for(Graph graph : graphSCCs) {
                    HashMap<Integer, HashSet<Integer>> curr = graph.exportGraph();
                    TreeSet<Integer> scc = new TreeSet<Integer>();
                    for (Map.Entry<Integer, HashSet<Integer>> entry : curr.entrySet()) {
                        scc.add(entry.getKey());
                    }
                    sccs.add(scc);
                }
                
             
                
                System.out.println("SCC result  loaded");
               
                int[] myout1=new int[5];
                int k =0;
                System.out.println("SCC final  result  loaded");
                for (int i = 0; i < sccs.size(); i++) {
                	
                	if(sccs.get(i).size()>200){
                		 System.out.println(sccs.get(i).size());
                		
                	}
                }
                
              
                
                }
             
               
               
            
	
	
	public static  int[] quicksortalgo( int[] arr, int left , int right){
		int index = partition(arr, left, right);
	      if (left < index - 1)
	    	  quicksortalgo(arr, left, index - 1);
	      if (index < right)
	    	  quicksortalgo(arr, index, right);
	     
	      
	      return arr;
	      
	      
	     }
	
	public static  int partition(int[] arr, int left , int right){
		 int i = left, j = right;
	      int tmp;
	      int pivot = arr[(left + right) / 2];
	      //int pivot = arr[0];
	      while (i <= j) {
	            while (arr[i] < pivot)
	                  i++;
	            while (arr[j] > pivot)
	                  j--;
	            if (i <= j) {
	                  tmp = arr[i];
	                  arr[i] = arr[j];
	                  arr[j] = tmp;
	                  i++;
	                  j--;
	            }
	      };
	     
	      return i;
	}
	
	

}


