/**Shortest path tree for DAG*/

/** tinyEWDAG.txt 5
 5 to 0 (0,73) 5->4  0,35 4->0  0,38
 5 to 1 (0,32) 5->1  0,32
 5 to 2 (0,62) 5->7  0,28 7->2  0,34
 5 to 3 (0,61) 5->1  0,32 1->3  0,29
 5 to 4 (0,35) 5->4  0,35
 5 to 5 (0,00)
 5 to 6 (1,13) 5->1  0,32 1->3  0,29 3->6  0,52
 5 to 7 (0,28) 5->7  0,28
 */

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

import java.util.ArrayDeque;
import java.util.Deque;

public class AcyclicSP {

    private DirectedEdge[] edgeTo;
    private double[] distTo;

    // constructor
    public AcyclicSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        Topological topological = new Topological(G);
        for (int v : topological.order()) {
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }
    }

    // edge relaxation
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }
    }

    // has path to given vertex?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // path to given vertex
    public Iterable<DirectedEdge> pathTo(int v) {
        Deque<DirectedEdge> path = new ArrayDeque<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    // distance to given vertex
    public double distTo(int v) {
        return distTo[v];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        AcyclicSP spt = new AcyclicSP(G, s);

        for (int v = 0; v < G.V(); v++) {
            System.out.printf("%d to %d (%.2f)", s, v, spt.distTo(v));
            for (DirectedEdge e : spt.pathTo(v))
                System.out.print(" " + e);
            System.out.println();
        }
    }
}
