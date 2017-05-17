/* Edge.java */

public class Edge implements Comparable<Edge> {
	Object object1;
	Object object2;
	int weight;

	public Edge(Object object1, Object object2, int weight) {
		this.object1 = object1;
		this.object2 = object2;
		this.weight = weight;
	}

	public int weight() {
		return weight;
	}

  	public boolean equals(Object o) {
  		if (o instanceof Edge) {
      		return ((object1.equals(((Edge) o).object1)) &&
              (object2.equals(((Edge) o).object2))) ||
             ((object1.equals(((Edge) o).object2)) &&
              (object2.equals(((Edge) o).object1)));
    	} else {
      		return false;
    	}
  }

  public int compareTo(Edge e) {
  	int weight2 = e.weight;
  	if (weight > e.weight) {
  		return 1;
  	} else if (weight == e.weight) {
  		return 0;
  	} else {
  		return -1;
  	}
  }
}