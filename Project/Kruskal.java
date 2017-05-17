/* Kruskal.java */

import java.util.Hashtable;
import java.util.Collections;
import java.util.LinkedList;
import graph.*;
import set.*;
import list.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   */
  public static WUGraph minSpanTree(WUGraph g) {
  	WUGraph t = new WUGraph();
  	Object[] vertices = g.getVertices();
  	LinkedList<Edge> edgeList = new LinkedList<Edge>();
  	for (int i=0; i<vertices.length; i++) {
		t.addVertex(vertices[i]);   // add g's vertices to t.
		Neighbors neighbors = g.getNeighbors(vertices[i]);
		for (int j=0; j<neighbors.neighborList.length; j++) {    // add all edges from g to SList: edges.
			Edge edge = new Edge(vertices[i], neighbors.neighborList[j], neighbors.weightList[j]);
			edgeList.add(edge);
		}
  	}
  	Collections.sort(edgeList);
  	// Create disjoint sets
  	DisjointSets sets = new DisjointSets(101);
  	for (int i=0; i<edgeList.size(); i++) {
  		Edge currentEdge = edgeList.get(i);
  		Object vertex1 = currentEdge.object1;
  		Object vertex2 = currentEdge.object2;
  		int compress1 = vertex1.hashCode() % 101;
  		int compress2 = vertex2.hashCode() % 101;
  		if (!t.isEdge(vertex1,vertex2) && (sets.find(compress1) != sets.find(compress2))) {
  			t.addEdge(vertex1, vertex2, currentEdge.weight());
  			sets.union(sets.find(compress1), sets.find(compress2));
  		}
  	}
  	return t;
  }

}