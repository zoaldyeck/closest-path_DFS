import java.util.ArrayList;

//
// SHORTESTPATHS.JAVA
// Compute shortest paths in a graph.
//
// Your constructor should compute the actual shortest paths and
// maintain all the information needed to reconstruct them.  The
// returnPath() function should use this information to return the
// appropriate path of edge ID's from the start to the given end.
//
// Note that the start and end ID's should be mapped to vertices using
// th graph's get() function.
//
// You can ignore the input and startTime arguments to the constructor
// unless you are doing the extra credit.
//
class ShortestPaths {
	
    public class Path{
    	public ArrayList<Integer> path = new ArrayList<Integer>();
    	}
    
    public Path[] allPath;
    //
    // constructor
    //
    
    public ShortestPaths(Multigraph G, int startId, 
			 Input input, int startTime) 
    {
    	/*
    	 * passed part1 test query 1 2 3
    	 */
    	if(startTime == 0){
    	//pq is a heap of vertex
    	PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>(); 
    	
    	//handles to store the position in the pq 
    	Handle handles[] = new Handle[G.nVertices()];
    	allPath = new Path[G.nVertices()];
    	//initializing
    	for(int i = 0; i < G.nVertices(); i ++){
    		if(G.get(i).id() != startId){
    			handles[i] = pq.insert(Integer.MAX_VALUE, G.get(i));
    			
    			//every airport has a integer arraylist to store the edges,
    			//at first, the path which is an arraylist is empty.
    			allPath[i] = new Path();
    		}
    	}
    	//the begin point
    	handles[startId] = pq.insert(0, G.get(startId));
    	allPath[startId] = new Path();
    	
    	while(!pq.isEmpty()){
    		//pop out the smallest (uDis, u.vertex) pair
    		int uDis = pq.min();
    		Vertex parent = pq.extractMin();
    		
    		if(uDis == Integer.MAX_VALUE)
    			break;
    		
    		//get children of u
    		Vertex.EdgeIterator children = parent.adj();
    		while(children.hasNext()){
    			//children is edge:(id,from,to,weight)
    			Edge edge = children.next();
    			Vertex child = edge.to();
    			int weight = edge.weight();
    			int flightId = edge.id();
    			//update the path if needed
    			if(pq.decreaseKey(handles[child.id()], uDis + weight)){
    				ArrayList<Integer> childPath = allPath[child.id()].path;
    				ArrayList<Integer> parentPath = allPath[parent.id()].path;
    				childPath.clear();
    				//copy its parent's past path to it
    				for(int i = 0; i <  parentPath.size(); i ++){
    					childPath.add(parentPath.get(i));
    				}
    				//parent's past path + (parent to child) becomes child's new path
    				childPath.add(flightId);
    		}
    		}
    	}
    	}
    	
    	/**
        query extra 1 2 3 test, passed 1 and 3, failed 2.
       **/
    	else{
    		//pq is a heap of vertex
    	   	PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>(); 
    	   	
    	   	//handles to store the position in the pq 
    	   	Handle handles[] = new Handle[G.nVertices()];
    	   	allPath = new Path[G.nVertices()];
    	   	//initializing
    	   	for(int i = 0; i < G.nVertices(); i ++){
    	   		if(G.get(i).id() != startId){
    	   			handles[i] = pq.insert(Integer.MAX_VALUE, G.get(i));
    	   			
    	   			//every airport has a integer arraylist to store the edges,
    	   			//at first, the path which is an arraylist is empty.
    	   			allPath[i] = new Path();
    	   		}
    	   	}
    	   	//the begin point
    	   	handles[startId] = pq.insert(0, G.get(startId));
    	   	allPath[startId] = new Path();
    	   
    	   	
    	   	while(!pq.isEmpty()){
    	   		
    	   		//pop out the smallest (uDis, u.vertex) pair
    	   		int uDis = pq.min();
    	   		Vertex parent = pq.extractMin();
    	   		
    	   		if(uDis == Integer.MAX_VALUE)
    	   			break;
    	   		int parentArrivalTime = uDis;
    	   		//get children of u
    	   		Vertex.EdgeIterator children = parent.adj();
    	   		
    	   		while(children.hasNext()){
    	   			//children is edge:(id,from,to,weight)
    	   			Edge edge = children.next();
    	   			int departFromParent = input.flights[edge.id()].startTime;
    	   			int endToChildren = input.flights[edge.id()].endTime;
    	   			
    	   			int truecost = 0;
    	   			int waitTillDepart = 0;
    	   			/*
    	   			 * judge if we take this flight or the flight the next day
    	   			 * waitTillDepart is the time we wait for the flight taking off
    	   			 */
    	   			if((departFromParent - 45) > (parentArrivalTime + startTime)%2400){
    	   				waitTillDepart = (departFromParent  - (parentArrivalTime + startTime)%2400)%2400;
    	   			
    	   			}
    	   			else
    	   				waitTillDepart = (departFromParent  - (parentArrivalTime + startTime)%2400)%2400 +2400;
    	   			/*
    	   			 * actualFlightTime is the actual flying time
    	   			 */
    	   			int actualFlightTime = (endToChildren - departFromParent + 2400)%2400;
    	   			
    	   			/*
    	   			 * add together
    	   			 */
    	   			truecost = waitTillDepart + actualFlightTime;
    	   			
    	   			
    	   			
    	   			Vertex child = edge.to();
    	   			int weight = edge.weight();
    	   			int flightId = edge.id();
    	   		    
    	   			//update the path if needed
    	   			if(pq.decreaseKey(handles[child.id()], truecost+uDis)){
    	   				ArrayList<Integer> childPath = allPath[child.id()].path;
    	   				ArrayList<Integer> parentPath = allPath[parent.id()].path;
    	   				childPath.clear();
    	   				//copy its parent's past path to it
    	   				for(int i = 0; i <  parentPath.size(); i ++){
    	   					childPath.add(parentPath.get(i));
    	   				}
    	   				//parent's past path + (parent to child) becomes child's new path
    	   				childPath.add(flightId);
    	   		}
    	   		}
    	   	}
    		
    	}
	// your code here
    }
    
    //
    // returnPath()
    // Return an array containing a list of edge ID's forming
    // a shortest path from the start vertex to the specified
    // end vertex.
    //
   
    public int [] returnPath(int endId) 
    { 
	// your code here
	ArrayList<Integer> pforend = allPath[endId].path;
	int[] ans = new int[pforend.size()];
	for(int i = 0 ; i < ans.length; i++){
		ans[i] = pforend.get(i);
	}
	return ans;
    }
}
