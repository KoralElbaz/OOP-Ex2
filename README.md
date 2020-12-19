# ex2

### The project is divided into two parts: 

#### - part one deals with directional weighted graph, we used three  interfaces to implement the graph properties in the class. 
#### - part two deals with pokimon game which includes several departments within it.

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
 * **isConnected()-** returns true if and only if  there is a valid path from each node to each other node. This function uses dijkstra algorithm .
 * **double shortestPathDist(int src, int dest)-** returns the length of the shortest path between src to dest, if no such path returns -1. This function uses bfs algorithm .
 * **List<node_data> shortestPath(int src, int dest)-** returns the the shortest path between src to dest - as an ordered List of nodes.
 * **Save(file)-** saves this weighted (directed) graph to the given
file name - in JSON format
 * **Load(file)-**  This method load a graph to this graph algorithm.
if the file was successfully loaded - the underlying graph of this class will be changed to the loaded one, in case the graph was not loaded the original graph should remain "as is".
 
  The class that implements this interface is DWGraph_Algo.



# part two: pokimon game
in this part there are some departments:

 1. **Arena-** an object from the Arena class is a List of Agent and Pokemons. 
 2. **CL_Pokimon-**  this class build single pokemon. An object from this class contain the follow feature:

-   Pos - represent the location of the fruit on the axis - X, Y, Z.
-   Value - represent the point that the pokimon is worth.
-   Type - represent if the pokimon on up edge (1) or down edge (-1).

 3. **CL_Agent**- this class build single agent. An object from this class contain the follow feature:

-   Pos - represent the locatiobn of the robot on the axis - X, Y, Z.
    
-   Value - represents the points earned by the agent.
    
-   Id - represent the ID of the agent
    
-   Speed - represent the speed of the agent.
 6. **MyFrame && MyPanel-** these departments are responsible for drawing all the game data and graphics to the user .

#  brief explanation of the game
## welcome to our game

In our game there are 24 stages (scenarios) each of them has a different starting point.

Every stage has its own conditions including the duration of the stage, number of agents, number of pokimons and a Dgraph representing the arena the game is at.

All the data about the game, like agents ,pokimons and the graph, have readed from the server as Json string.

The goal of the game is to get as many points as possible by eating pokimons.

At the end of each game you can see the number of points and moves we've made.

# How to play? 
### first step : Enter your ID and the desired scenario number (Level). 
![enter ID](https://github.com/KoralElbaz/OOP-Ex2/blob/master/data/id.png)
![choose Level](https://github.com/KoralElbaz/OOP-Ex2/blob/master/data/level.png)

another way is at the terminal. :

### second step:  the window will open and the game will start: 
![for example](https://github.com/KoralElbaz/OOP-Ex2/blob/master/data/game.png)

### Last step:  at the end of game you can see the number of points and moves we've made.

![](https://github.com/KoralElbaz/OOP-Ex2/blob/master/data/grade.png)

# Enjoy !! 
