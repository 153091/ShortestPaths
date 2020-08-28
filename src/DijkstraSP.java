/** tinyEWD.txt 0
 0 to 0 (0.00)
 0 to 1 (1.05) 0->4  0,38 4->5  0,35 5->1  0,32
 0 to 2 (0.26) 0->2  0,26
 0 to 3 (0.99) 0->2  0,26 2->7  0,34 7->3  0,39
 0 to 4 (0.38) 0->4  0,38
 0 to 5 (0.73) 0->4  0,38 4->5  0,35
 0 to 6 (1.51) 0->2  0,26 2->7  0,34 7->3  0,39 3->6  0,52
 0 to 7 (0.60) 0->2  0,26 2->7  0,34*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Deque;

public class DijkstraSP {

    private IndexMinPQ<Double> pq; // order for vertices to add in SPT
    private DirectedEdge[] edgeTo; // previous edge in Path
    private double[] distTo; // distance to that vertex from source

    // constructor
    public DijkstraSP(EdgeWeightedDigraph G, int s) {

        // Dijkstra's algo didn't work with negative weights
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<>(G.V());

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        pq.insert(s, distTo[s]);

        while (!pq.isEmpty()) {
            int v = pq.delMin();

            for (DirectedEdge e : G.adj(v))
                relax(e);
        }
    }

    // Edge relaxation
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();

        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;

            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    // shortest distance to v
    public double distTo(int v) {
        return distTo[v];
    }

    // has path to given vertex
    public boolean hasPath(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // Shortest path to given vertex
    public Iterable<DirectedEdge> pathTo(int v) {
        Deque<DirectedEdge> path = new ArrayDeque<>();

        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);

        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        DijkstraSP sp = new DijkstraSP(G, s);

        for (int v = 0; v < G.V(); v++) {
            StdOut.printf("%d to %d (%.2f)", s, v , sp.distTo(v));
            for (DirectedEdge e : sp.pathTo(v))
                StdOut.print(" " + e);
            StdOut.println();
        }
    }

}
