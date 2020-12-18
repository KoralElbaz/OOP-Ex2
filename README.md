# ex2

### The project is divided into two parts: 

#### - part one deals with directional weighted graph, we used three  interfaces to implement the graph properties in the class. 
#### - part two deals with pokimon game שhich includes several departments within it.

   # part one: directional weighted graph
   

#### the interfces and their implements: 
 1. **`node_data`** - This interface represents the data of a node in an unweighted graph.
the class that implements the interface is NodeData which is an internal department in the class WGraph_DS.
at this department theare are function like get node tag or set him, get node info or set him, and get the node key.

 2. **`directed_weighted_graph.java`** - This interface represents an directional weighted graph.
the class that implements the interface is DWGraph_DS.
in this class, the functions of the actions on the graph are implemented, such as adding a vertex, deleting a vertex, deleting a rib, and more..
The main data structure that I chose to use to implement the project is HashMap because in average case on the methods get/put/containsKey() the ran time is O(1).
In addition, when returning values from the HashMap, we receive a collection that is one of the things we needed for certain methods.
 
 3.  **`dw_graph_algorithms`** - this interface represents the "regular" Graph Theory algorithms including:
 * **clone(); (copy)-** compute a deep copy of this weighted graph.
 * **init(graph)-** init the graph on which this set of algorithms operates on.
 * **isConnected()-** returns true if and only if  there is a valid path from each node to each other node.
 * **double shortestPathDist(int src, int dest)-** returns the length of the shortest path between src to dest, if no such path returns -1.
 * **List<node_data> shortestPath(int src, int dest)-** returns the the shortest path between src to dest - as an ordered List of nodes.
 * **Save(file)-** saves this weighted (directed) graph to the given
file name - in JSON format
 * **Load(file)-**  This method load a graph to this graph algorithm.
if the file was successfully loaded - the underlying graph of this class will be changed to the loaded one, in case the graph was not loaded the original graph should remain "as is".
 
  The class that implements this interface is DWGraph_Algo.

## Algorithms:

At  my project I used two algorithms:

1. For the method `isConnected` I used Bfs algorithm, which is an efficient algorithm in which all the vertices of the graph pass laterally.
His method starts at the graph node and goes through the collection with all of its neighbor nodes, marks that we visited them, and adds them to the list. After finishing with the current node continue to the next node in the list and repeat the same operation.
Finally we check whether the size of the list of nodes we visited is equal to the size of all the vertices in the graph.
After we run the algorithm on a specific vertex we change the direction of all the sides in the graph and check the connection again (run the algorithm again) .

2. For method  `shortestPath` I used **Dijkstra algorithm**, which solves the problem of finding the easiest route from a point on the graph to a destination in a weighted graph. 
I  choosed to use this algorithm because using this algorithm we can find, at the same time, the fast paths to all the points in the graph.
His method works as follows:
we are create priority queue of size = nomber of vertices, then we will create HashMap to save the pred nodes, and HashSet Spt to keep track of the vertices which are currently in Shortest Path Tree, and also we will Override the Comparator of priority queue to sort them based on the tag.
At the beginning for each vertex is marked what is its distance from the vertex of the source and indicate with HashSet whether we visited it or not.
At first all the vertices are marked as not visited, and their distance is defined as infinit except the first vertex for which distance will 0.
**The algorithm loop:**
while priority queue is not empty we will extract the min node from the priority queue, say it vertex u and add it to the SPT.
For adjacent vertex v, if v is not in SPT and v.tag>u.tag + edge(u,v) weight, then v is updated so that v.tag  = u.tag + edge(u,v) weight and will be added to the priority queue, and after that we will add the current vertex and its neighbor to the "pred" hashMap for save the path.
The algorithm ends when the new vertex v is the destination or when we have visited all the vertices.

# part two: pokimon game
in this part there are some departments:

 1. Arena-
 2. CL_Pokimon-
 3. CL_Agent-
 4. Ex2-
 5. MyFrame-
 6. MyPanel-

#  brief explanation of the game
## welcome to our game

In our game there are 24 stages (scenarios) each of them has a different starting point.

Every stage has its own conditions including the duration of the stage, number of agents, number of pokimons and a Dgraph representing the arena the game is at.

All the data about the game, like agents ,pokimons and the graph, have readed from the server as Json string.

The goal of the game is to get as many points as possible by eating pokimons.

The main part of this assignment was to obtain a number of points as requested, no more calls to serve than requested.

At the end of each game you can see the number of points and moves we've made.




