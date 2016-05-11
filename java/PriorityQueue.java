import java.util.ArrayList;


//
// PRIORITYQUEUE.JAVA
// A priority queue class supporting sundry operations needed for
// Dijkstra's algorithm.
//

class PriorityQueue<T> {
	
	private ArrayList<Combine<T>> pq;
	
    // constructor
    public PriorityQueue()
    {
    	pq = new ArrayList<Combine<T>>();
    	pq.add(null);
    }
    
    // Return true iff the queue is empty.
    public boolean isEmpty() {return (pq.size()==1);}
    
   //return leftchild's key if it exists
    private int leftchild(int pos){
    	if(2*pos < pq.size())
    		return pq.get(2*pos).getKey();
    	else
    		return Integer.MAX_VALUE;
    }
  //return rightchild's key if it exists
    private int rightchild(int pos){
    	if((2*pos + 1) < pq.size())
    		return pq.get(2*pos+1).getKey();	
    	else
    		return Integer.MAX_VALUE;
    }
    //return it's parent
    private int parent(int pos){
    	return pq.get(pos/2).getKey();
    }
    
    private boolean isleaf(int pos){
    	return ((pos > (pq.size() - 1)/2)&& pos < pq.size());
    }
    // Insert a pair (key, value) into the queue, and return
    // a Handle to this pair so that we can find it later in
    // constant time.
    //
    Handle insert(int key, T value)
    {
    	int pos = pq.size();
    	Handle handle = new Handle(pos);
    	pq.add(new Combine<T>(key, handle, value));
    	
    	while(pos > 1){
    		int pKey = parent(pos);	
    		if(key < pKey){
    			int pPos = pos/2;
    			swap(pos,pPos);
    			pos = pPos;	
    			}
    		else
    			return handle;
    	}//bubble up until it's smaller than its parent.
    	
    	return handle;
    }
    
    public void swap(int i, int j){
        Combine<T> iItem = pq.get(i);
        Combine<T> jItem = pq.get(j);
        
        pq.remove(i);
        pq.add(i,jItem);
        pq.remove(j);
        pq.add(j, iItem);
        
        pq.get(i).handle.setIndex(i);
        pq.get(j).handle.setIndex(j);
    }//swap item i and item j and set the handle right
    
    public void Heapify(int index){ 
    	
    	if(!isleaf(index)){
    		int lkey = leftchild(index);
    		int rkey = rightchild(index);
    		
    		//if it has only 1 child, the non-exist one 
    		//is Integer.Max_value, and won't affect the next step
    		//j is the smallest child's index
    		int j = (lkey <= rkey)?(2*index):(2*index + 1);
    		
    		//swap when the smallest child is smaller than itself
    		if(pq.get(j).getKey() < pq.get(index).getKey()){
    			swap(index,j);
    			
    			//Heapify at the new position
    			if(!pq.isEmpty())
    				Heapify(j);	
    		}
    	}
    }
    
    // Return the smallest key in the queue.
    public int min(){return pq.get(1).getKey();}
    
    // Extract the (key, value) pair associated with the smallest
    // key in the queue and return its "value" object.
    public T extractMin()
    {
    	//the smallest is the first item in the pq Heap.
    	int n = pq.size() - 1;
    	T min = pq.get(1).getValue();
    	
    	//swap the 1st and the last one, set the handle -1, and remove the last one.
    	swap(1,n);
    	pq.get(n).getHandle().setIndex(-1);
    	pq.remove(n);
    	
  	    //heapify from the top
    	if(!this.isEmpty())
    		Heapify(1);
    	
    	return min;
    }
    
    
    // Look at the (key, value) pair referenced by Handle h.
    // If that pair is no longer in the queue, or its key
    // is <= newkey, do nothing and return false.  Otherwise,
    // replace "key" by "newkey", fixup the queue, and return
    // true.
    //
    public boolean decreaseKey(Handle h, int newkey)
    {
    	//the position
    	int pos = h.getIndex();
    	if(pos > 0 && pos < pq.size() && pq.get(pos).getKey() > newkey){
    		pq.get(h.getIndex()).setKey(newkey);
    		
    		while(pos > 1){
    			int pKey = parent(pos);	
        		if(newkey < pKey){
        			int pPos = pos/2;
        			swap(pos,pPos);
        			pos = pPos;	
        		}
        		else
        			return true; //find a proper place in the tree
    		}
    		return true;	//it's in the 1st position now.
    	}
    	
    	//can't decrease key,
    	else
    		return false;
    }
    
    
    // Get the key of the (key, value) pair associated with a 
    // given Handle. (This result is undefined if the handle no longer
    // refers to a pair in the queue.)
    //
    public int handleGetKey(Handle h)
    {
    	if(h.getIndex() <= (pq.size() - 1)&&h.getIndex()!=-1)
    		return pq.get(h.getIndex()).getKey();
    	return 0;
    }

    // Get the value object of the (key, value) pair associated with a 
    // given Handle. (This result is undefined if the handle no longer
    // refers to a pair in the queue.)
    //
    public T handleGetValue(Handle h)
    {
    	if(h.getIndex() < pq.size() && h.getIndex() > 0)
    		return pq.get(h.getIndex()).getValue();
    	return null;
    }
    
    // Print every element of the queue in the order in which it appears
    // in the implementation (i.e. the array representing the heap).
    public String toString()
    {
    	String ans = "";
    	if(!this.pq.isEmpty()){
    		for(int i = 1; i < this.pq.size(); i++)
    			ans += this.pq.get(i).toString(); 
    		return ans;
    		}
    	return ans;
    }
    
    //pqNode
    public class Combine<T>{
    	private int key;
    	private Handle handle;
    	private  T value;
    	
    	public Combine(int key, Handle handle, T value){
    		this.key = key;
    		this.handle = handle;
    		this.value = value;	
    	}
    	
    	public int getKey(){return this.key;}
    	public Handle getHandle(){return this.handle;}
    	public T getValue(){return this.value;}
    	
    	public void setKey(int newKey){this.key = newKey;}
    	
    	public String toString(){return "(" + key + ", " + value.toString() +")\n";}
    }
}
