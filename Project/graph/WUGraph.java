/* WUGraph.java */

package graph;

import java.util.Hashtable;
import list.*;
import java.lang.Math.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

  private static final int TABLE_SIZE = 101;
  SList[] hashTable;
  SList[] hashTableEdge;
  Object[] edge;
  SList verticesList;
  int num_vertex;
  int num_edge;
  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph() {
    hashTable = new SList[TABLE_SIZE];
    for (int i=0; i<hashTable.length; i++) {
      hashTable[i] = new SList();
    }
    hashTableEdge = new SList[TABLE_SIZE];
    for (int i=0; i<hashTableEdge.length; i++) {
      hashTableEdge[i] = new SList();
    }
    verticesList = new SList();
    num_vertex = 0;
    num_edge = 0;
  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount() {
    return num_vertex;
  }

  /**
   * edgeCount() returns the number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount() {
    return num_edge;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices() {
    try {
      Object[] verticesArray = new Object[num_vertex];
      ListNode node = verticesList.front();
      for (int i=0; i<num_vertex; i++) {
        verticesArray[i] = ((SList) node.item()).front().item();
        node = node.next(); 
      }
      return verticesArray;
    } catch (InvalidNodeException e) {
      System.err.println("Catch InvalidNodeException in getVertices().");
      return null;
    }
  }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.  The
   * vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex) {
    if (isVertex(vertex)) {
      return;
    }
    else {
      int compress = vertex.hashCode() % TABLE_SIZE;
      compress = Math.abs(compress);
      Entry entry = new Entry();
      SList adjacencyList = new SList();
      entry.key = vertex;                            //entry.key = vertex, entry.pointer = vertex's adjacencyList
      adjacencyList.insertFront(vertex);             //vertex's adjacencyList front = vertex.
      hashTable[compress].insertBack(entry);
      verticesList.insertBack(adjacencyList);
      entry.pointer = verticesList.back();
      num_vertex++;
    }
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex) {
    SList s = findVertexNode(vertex);
    SListNode edge;
    try {
      if (s == null) {
        return;
      }
      while (s.length() > 1) {         // Removing related edges of the vertex
        edge = s.back();
        Object object2 = ((VertexPair) edge.item()).object2;
        removeEdge(vertex,object2);
      }
      s = null;
      int compress = vertex.hashCode() % TABLE_SIZE;
      compress = Math.abs(compress);
      SListNode node = hashTable[compress].front();
      while (node.isValidNode()) {
       if (((Entry) node.item()).key().hashCode() == vertex.hashCode()) {
         ((SListNode) ((Entry) node.item()).pointer()).remove();
         node.remove();
         num_vertex--;
         return;
       }
       node = node.next();
      }
      return;
    } catch (InvalidNodeException e) {
      System.err.println("Catch InvalidNodeException in removeVertex().");
    }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex) {
    int compress = vertex.hashCode() % TABLE_SIZE;
    compress = Math.abs(compress);
    SListNode node = hashTable[compress].front();
    try {
      while (node.isValidNode()) {
//        if (((Entry) node.item()).key().hashCode() == vertex.hashCode()) {
        if (((Entry) node.item()).key().equals(vertex)) {
          return true;
        }
        System.out.println("I'm here1.");
        node = node.next();
        System.out.println("I'm here2.");
      }
      return false;
    } catch (InvalidNodeException e) {
      System.err.println("Catch InvalidNodeException in isVertex().");
      return false;
    }
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex) {
    if (!isVertex(vertex)) {
      return 0;
    }
    return findVertexNode(vertex).length()-1;
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex) {
    if (!isVertex(vertex) || degree(vertex) == 0) {
      return null;
    }
    Neighbors neighbor = new Neighbors();
    neighbor.weightList = new int[degree(vertex)];
    neighbor.neighborList = new Object[degree(vertex)];
    for (int i=0; i<degree(vertex); i++) {
      neighbor.neighborList[i] = new Object();
    }
    SListNode node = findVertexNode(vertex).front();
    try {
      if (node.isValidNode()) {
        node = node.next();
        int i=0;
        while (node.isValidNode()) {
          neighbor.neighborList[i] = ((VertexPair) node.item()).object2;
          neighbor.weightList[i] = ((VertexPair) node.item()).weight;
          i++;
          node = node.next();
        }
      }
      return neighbor;
    } catch (InvalidNodeException e) {
      System.err.println("Catch InvalidNodeException in getNeighbors().");
      return null;
    }
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the edge is already
   * contained in the graph, the weight is updated to reflect the new value.
   * Self-edges (where u == v) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight) {
    if ((!isVertex(u)) || (!isVertex(v))) {
      return;
    }
    else {
      if (isEdge(u,v)) {
        findEdge(u,v).weight = weight;
      }
      else if (u.equals(v)) {
        SList adjacencyList1 = findVertexNode(u);
        VertexPair edge = new VertexPair(u,v,weight);
        adjacencyList1.insertBack(edge);
        int compress = edge.hashCode() % TABLE_SIZE;
        compress = Math.abs(compress);
        Entry entry = new Entry();
        entry.key = edge;
        hashTableEdge[compress].insertBack(entry);
        entry.pointer = adjacencyList1.back();
        num_edge++;
      }
      else {
        SList adjacencyList1 = findVertexNode(u);        //find the u's adjacency list
        SList adjacencyList2 = findVertexNode(v);        //find the v's adjacency list
        VertexPair edge1 = new VertexPair(u,v,weight);   //create edge1
        VertexPair edge2 = new VertexPair(v,u,weight);   //create edge2
        adjacencyList1.insertBack(edge1);                 //put edge in adjacency list
        adjacencyList2.insertBack(edge2);
        int compress = edge1.hashCode() % TABLE_SIZE;    // compress edge into hashTableEdge
        compress = Math.abs(compress);
        Entry entry = new Entry();                      
        entry.key = edge1;                               //entry.key = edge, entry.pointer = edge in adjacency list
        hashTableEdge[compress].insertBack(entry);
        entry.pointer = adjacencyList1.back();
        try {
          ((VertexPair) adjacencyList1.back().item()).partner = adjacencyList2.back();
          ((VertexPair) adjacencyList2.back().item()).partner = adjacencyList1.back();
        } catch (InvalidNodeException e) {
          System.err.println("Catch InvalidNodeException in addEdge().");
        }
        num_edge++;
      }
    }
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v) {
    if ((!isVertex(u) || !isVertex(v)) || !isEdge(u,v)) {
      return;
    }
    else {
      VertexPair edge = new VertexPair(u,v);
      int compress = edge.hashCode() % TABLE_SIZE;
      compress = Math.abs(compress);
      SListNode node = hashTableEdge[compress].front();
      try {
        while (node.isValidNode()) {
          if (((Entry) node.item()).key().hashCode() == edge.hashCode()) {
            SListNode nodeToRemove = (SListNode) ((Entry) node.item()).pointer();
            if (!u.equals(v)) {
              ((VertexPair) nodeToRemove.item()).partner().remove();
            }
            nodeToRemove.remove();
            node.remove();
            num_edge--;
            return;
          }
          System.out.println("I'm here5.");
          node = node.next();
        }
      } catch (InvalidNodeException e) {
        System.err.println("Catch InvalidNodeException in removeEdge().");
      }
      return;
    }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v) {
    VertexPair edge = new VertexPair(u,v);
    int compress = edge.hashCode() % TABLE_SIZE;
    compress = Math.abs(compress);
    SListNode node = hashTableEdge[compress].front();
    try {
      while (node.isValidNode()) {
        if (((Entry) node.item()).key().hashCode() == edge.hashCode()) {
          return true;
        }
        node = node.next();
      }
    } catch (InvalidNodeException e) {
      System.out.println("Catch InvalidNodeException in isEdge().");
    }
    return false;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but
   * also more annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v) {
    if ((!isVertex(u) || !isVertex(v)) || !isEdge(u,v)) {
//      System.out.println("I'm here.");
      return 0;
    }
    else {
      VertexPair edge = findEdge(u,v);
      return edge.weight();
    }
  }

  private SList findVertexNode(Object vertex) {
    int compress = vertex.hashCode() % TABLE_SIZE;
    compress = Math.abs(compress);
    SListNode node = hashTable[compress].front();
    try {
      while (node.isValidNode()) {
        if (((Entry) node.item()).key().hashCode() == vertex.hashCode()) {
          return (SList) ((SListNode) ((Entry) node.item()).pointer()).item();
        }
        node = node.next();
      }
    } catch (InvalidNodeException e) {
      System.err.println("Catch InvalidNodeException in findVertexNode().");
    }
    return null;
  }

  private VertexPair findEdge(Object u, Object v) {
    if (!isEdge(u,v)) {
      return null;
    }
    else {
      VertexPair edge = new VertexPair(u,v);
      int compress = edge.hashCode() % TABLE_SIZE;
      compress = Math.abs(compress);
      SListNode node = hashTableEdge[compress].front();
      try {
        while (node.isValidNode()) {
          if (((Entry) node.item()).key().hashCode() == edge.hashCode()) {
            return (VertexPair) ((SListNode) ((Entry) node.item()).pointer()).item();
          }
          node = node.next();
        }
      } catch (InvalidNodeException e) {
        System.out.println("Catch InvalidNodeException in findEdge().");
      }
      return null;
    }
  }
}