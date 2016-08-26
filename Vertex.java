package com.graph.creater;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;




public class Vertex implements Comparable<Vertex> {
	List<Edge> edges  = new ArrayList<Edge>();
    public List<Vertex> parents = new ArrayList<Vertex>();
    
    int val;
    public boolean visited = false;
    public float flow = 1;
    public int pathCount = 0;
    public int pre = 0;
    public int post = 0;
    public int layer = 0;
    
    public Vertex(int val) {
        this.val = val;
      
    }
    
    public Vertex(Vertex v, List<Vertex> allowed) {
        this.val = v.getVal();
        edges = new ArrayList<Edge>();
        for (Edge e : v.getEdges()) {
            if (allowed.contains(e.getOtherVertex(v))) {
                edges.add(e);
            }
        }
    }

	public void visit() {
        visited = true;
    }

    public int getVal() {
        return val;
    }
    public int compareTo(Vertex v) {
        return v.layer - layer;

   }
    public void reset() {
        layer = 0;
        parents = new ArrayList<Vertex>();
        visited = false;
        flow = 1;
        pathCount = 0;
        pre = 0;
        post = 0;
    }
    public void addEdge(Vertex v) {
        Edge e = new Edge(this, v);
        edges.add(e);
        
      
    }
    public void addEdge(Edge e) {
        edges.add(e);
    }
    
    public void removeEdge(Vertex v) {
        for (Edge e : edges) {
            if (e.getOtherVertex(this).equals(v)) {
                edges.remove(e);
                return;
            }
        }
    }
    
    public int getEdgeCount(List<Vertex> allowed) {
        int ret = 0;
        for (Edge e : edges) {
            if (allowed.contains(e.getOtherVertex(this))) {
                ret += 1;
            }
        }
        return ret;
    }
    public int degree() {
        return edges.size();
    }
    
    public List<Vertex> getNeighbors() {
        List<Vertex> ret = new ArrayList<Vertex>();
        for (Edge e : edges) {
            ret.add(e.getOtherVertex(this));
        }
        return ret;
    }

    public List<Vertex> getNeighborsAndThis() {
        List<Vertex> ret = getNeighbors();
        ret.add(this);
        return ret;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Edge getEdge(Vertex v) {
        for (Edge e : edges) {
            if (e.getOtherVertex(this) == v) {
                return e;
            }
        }
        throw new IllegalArgumentException("Edge not found!");
    }
    @Override
    public boolean equals(Object o) {
        return val == ((Vertex)o).getVal();
    }

    public static Comparator<Vertex> VertexComparator = new Comparator<Vertex>() {
        public int compare(Vertex v1, Vertex v2) {
            return v1.compareTo(v2);
        }
    };
}
