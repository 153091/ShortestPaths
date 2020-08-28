/**
 2->3 10,20
 * */

public class DirectedEdge {

    private final int v, w;
    private final double weight;

    // constructor
    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    // vertex from
    public int from() {
        return v;
    }

    // vertex to
    public int to() {
        return w;
    }

    // weight of the edge
    public double weight() {
        return weight;
    }

    // string representation
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", weight);
    }

    public static void main(String[] args) {
	DirectedEdge e = new DirectedEdge(2, 3, 10.2);
	System.out.print(e);
    }
}
