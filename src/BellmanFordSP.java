/** Bellman-Ford (queue based) Algorithm for SPT
 * NO NEGATIVE CYCLES
 *
 * % tinyEWDn.txt
 * 0 to 0 (0.00)
 * 0 to 1 (0.93) 0->2  0,26 2->7  0,34 7->3  0,39 3->6  0,52 6->4 -1,25 4->5  0,35 5->1  0,32
 * 0 to 2 (0.26) 0->2  0,26
 * 0 to 3 (0.99) 0->2  0,26 2->7  0,34 7->3  0,39
 * 0 to 4 (0.26) 0->2  0,26 2->7  0,34 7->3  0,39 3->6  0,52 6->4 -1,25
 * 0 to 5 (0.61) 0->2  0,26 2->7  0,34 7->3  0,39 3->6  0,52 6->4 -1,25 4->5  0,35
 * 0 to 6 (1.51) 0->2  0,26 2->7  0,34 7->3  0,39 3->6  0,52
 * 0 to 7 (0.60) 0->2  0,26 2->7  0,34
 *
 * (if negative cycle)
 * %tinyEWDnc.txt
 * 4->5  0,35
 * 5->4 -0,66
 * */



import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public class BellmanFordSP {

    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private boolean[] onQueue;
    Queue<Integer> q = new ArrayDeque<>();
    private int cost;                      // number of calls to relax()
    private Iterable<DirectedEdge> cycle;  // negative cycle (or null if no such cycle)

    // constructor
    public BellmanFordSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        onQueue = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        q.add(s);
        onQueue[s] = true;

        while (!q.isEmpty() && !hasNegativeCycle()) {
            int v = q.remove();
            onQueue[v] = false;
            relax(G, v);
        }
    }

    // relaxation of edges pointing from given vertex
    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;

                if (!onQueue[w]) {
                    q.add(w);
                    onQueue[w] = true;
                }
            }
            if (++cost % G.V() == 0) {
                findNegativeCycle();
                if (hasNegativeCycle()) return;  // found a negative cycle
            }
        }
    }

    // has path to given vertex?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // distance to given vertex
    public double distTo(int v) {
        return distTo[v];
    }
    // path to given vertex
    public Iterable<DirectedEdge> path(int v) {
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        if (!hasPathTo(v)) return null;
        Deque<DirectedEdge> path = new ArrayDeque<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    // find negative cycle
    private void findNegativeCycle() {
        int V = edgeTo.length;
        EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);

        for (int v = 0; v < spt.V(); v++)
            if (edgeTo[v] != null)
                spt.addEdge(edgeTo[v]);

        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
        cycle = finder.cycle();
    }

    //is there a Negative cycle?
    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    // negative cycle if exist
    public Iterable<DirectedEdge> negativeCycle() {
        return cycle;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        BellmanFordSP spt = new BellmanFordSP(G, s);

        // if negative cycle - print it
        if (spt.hasNegativeCycle()) {
            for (DirectedEdge e : spt.negativeCycle())
                StdOut.println(e);
        }
        else {
            for (int v = 0; v < G.V(); v++) {
                StdOut.printf("%d to %d (%.2f)", s, v, spt.distTo(v));
                for (DirectedEdge e : spt.path(v))
                    StdOut.print(" " + e);
                StdOut.println();
            }
        }

    }
}
