/** % tinyEWD.txt
 8 15
 0: 0->4  0,38   0->2  0,26
 1: 1->3  0,29
 2: 2->7  0,34
 3: 3->6  0,52
 4: 4->5  0,35   4->7  0,37
 5: 5->4  0,35   5->7  0,28   5->1  0,32
 6: 6->2  0,40   6->0  0,58   6->4  0,93
 7: 7->5  0,28   7->3  0,39
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class EdgeWeightedDigraph {

    private final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private List<DirectedEdge>[] adj;
    private int[] indegree; // indegree[v] = indegree of vertex v

    // create empty Graph with V vertices
    public EdgeWeightedDigraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertex must be nonnegative");

        this.V = V;
        this.E = 0;
        indegree = new int[V];
        adj = (List<DirectedEdge>[]) new List[V];
        for (int v = 0 ; v < V; v++)
            adj[v] = new LinkedList<>();
    }

    // create Graph from input stream
    public EdgeWeightedDigraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");

        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices must be nonnegative");
            indegree = new int[V];
            adj = (List<DirectedEdge>[]) new List[V];
            for (int v = 0; v < V; v++)
                adj[v] = new LinkedList<>();

            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges must be nonnegative");
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                double weight = in.readDouble();
                DirectedEdge e = new DirectedEdge(v, w, weight);
                addEdge(e);
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in EdgeWeightedDigraph constructor", e);
        }
    }

    // add edge v->w
    public void addEdge(DirectedEdge e) {
        int v = e.from(), w = e.to();
        adj[v].add(e);
        indegree[w]++;
        E++;
    }

    // edges pointing from v
    public Iterable<DirectedEdge> adj(int v) {
        return adj[v];
    }

    // all edges of the digraph
    public Iterable<DirectedEdge> edges() {
        Queue<DirectedEdge> q = new LinkedList<>();
        for (int v = 0; v < V; v++)
            for (DirectedEdge e : adj[v])
                q.add(e);
        return q;
    }

    // number of vertices
    public int V() {
        return V;
    }

    // number of edges
    public int E() {
        return E;
    }

    // outdegree of vertex v
    public int outdegree(int v) {
        return adj[v].size();
    }

    // indegree of vertex v
    public int indegree(int v) {
        return indegree[v];
    }

    // string representation
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (DirectedEdge e : adj[v])
                s.append(e + "   ");
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        StdOut.println(G);
    }
}
