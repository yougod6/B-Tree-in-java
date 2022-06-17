// Node Class for using in BTree Class
public class Node{
	private Integer size;   // max size of Integer array (will be order-1 in BTree class)  
	private Integer keyArr[]; // array for keys in node 
	private Node childArr[]; // array for children nodes of this node
    private Integer keyCnt; // counting keyArr's element(key)
    private Integer childCnt;   // counting childArr's element(Node) 

    // Node Class Constructor
	public Node(Integer size) {
        this.size = size;
		keyArr = new Integer[size]; //Initialize  keyArr as an array of "size"(=order-1 in BTree) sizes    
	    childArr= new Node[size+1]; //Initialize  childArr as an array of "size+1"(=order in BTree) sizes
        keyCnt = 0; // Initialize keyCnt as 0
        childCnt = 0; // Initialize childCnt as 0
	}

    // return whethere this node (keyArr) is empty(return True) or not(return False).
    public boolean isEmpty(){
        return keyCnt==0;
    }

    // return whethere this node (keyArr) is fulled(return True) or not(return False).
    public boolean isFull(){
        return keyCnt>=size;
    }

    // return whethere this node is leaf node(has no child, return True) or not(return False).
    public boolean isLeaf(){
        return childCnt==0; 
    }

    // return keyArr 
    public Integer[] getKey(){
        return keyArr;
    }

    // return how many keys in this node 
    public Integer getKeyCnt(){
        return keyCnt;
    }

    // return a specific key value of keyArr by receiving an index.
    public Integer getKey(int index){
        return keyArr[index];
    }
    
    // return first element of keyArr
    public Integer getFirst(){
        if(isEmpty())
            return null;
        return keyArr[0];
    }
    
    // return how many child nodes of this node 
    public Integer getChildCnt(){
        return childCnt;
    }
    
    // return childArr (this array's element is Node object)
    public Node[] getChildArr(){
        return childArr;
    }

    // return a specific node of childArr by receiving an index.
    public Node getchild(int i){
        return childArr[i];
    }

    //Insert key to keyArr by receiving a vlaue (e) 
	public void insertKey(Integer e) {
        // if keyArr is fulled or argument 'e' is null, end function
        if(isFull() || e==null)
            return;
        // if keyArr is empty, insert 'e' into keyArr and increase keyCnt by 1    
        if(keyCnt==0){
            keyArr[0]=e;
            keyCnt++;
            return;
        }
        // insert key 'e' in keyArr (start with last index in keyArr)
        for(int i=keyCnt-1;i>=0;i--){
            // if the argument 'e' is greater than keyArr[i]
            if(keyArr[i]<e){
                keyArr[i+1]=e;  // insert key 'e' into keyArr[i+1]
                break;  // break for loop 
            }
            //sorting
            else{
                keyArr[i+1]=keyArr[i];
            }
        }
        //if the argument 'e' is smaller than keyArr[0]
        if(e<keyArr[0])
            keyArr[0]=e;    //insert 'e' into keyArr[0]
        keyCnt++;   //increase keyCnt by 1    
	}

    // connect this node (parent) and child node by receiving child Node
    public void insertChild(Node c){
        // if argument Node 'c' is null, end function
        if(c==null)
            return;
        Integer head = c.getFirst();    // value of first element of 'c' node (criteria of location)

        // if head is minimum value 
        if(keyArr[0] > head){
            // if this location(childArr[0]) has no element (null)
            // To avoid increasing childCnt when an element changes
            if(childArr[0]==null)
                childCnt++; // increas childCnt by 1
            childArr[0] = c; // insert 'c' node into childArr[0]
        }
        // if head is maximum value
        else if(keyArr[keyCnt-1] < head){
            // if this location(childArr[keyCnt]) has no element (null)
            // To avoid increasing childCnt when an element changes
            if(childArr[keyCnt]==null)
                childCnt++; // increas childCnt by 1
            childArr[keyCnt] = c; // insert 'c' node into childArr[keyCnt]
        }
        else{
            for(int i=0; i<childCnt; i++){
                // If the head is neither minimum and maximum
                if(keyArr[i] < head && keyArr[i+1] > head ){
                    // To avoid increasing childCnt when an element changes
                    if(childArr[i]==null)
                        childCnt++; // increas childCnt by 1
                    childArr[i] = c; // insert 'c' node into childArr[i]
                    return;
                }
            }
        }
	}

    // linking this node (parent) and child node by receiving an index and child node
    public void insertChild(int index, Node c){
        // If the index is valid
        if(index<=size){
            // To avoid increasing childCnt when an element changes
            if(childArr[index] ==null)
                childCnt++;
            childArr[index]=c;
        }
    }

    // change a specific child node's index from first argument to second argument
    public void moveChild(int from, int to){
        childArr[to] = childArr[from];
    }
    
    // find index of key(e) in keyArr
    public int findKeyIndex(Integer e){
        if(isEmpty() || e==null){
            return -1;
        }
        for(int i=0;i<keyCnt;i++){
            if(keyArr[i]==e)
                return i;
        }
        return -1;
    }

    // delete a specific key at keyArr by receiving a value (e).
    public void deleteKey(Integer e){
        int index = findKeyIndex(e);
        if(index==-1)
            return;
        for(int i=index;i<keyCnt-1;i++)
            keyArr[i]=keyArr[i+1];
        keyArr[keyCnt-1]=null;
        keyCnt--;
        return;
    }

    // delete a specific key at keyArr by receiving an index 
    public Integer deleteKey(int index){
        Integer temp = keyArr[index];
        deleteKey(temp);
        return temp;
    }

    // delete a child node of this node by receiving a Node (c).
    public void deleteChild(Node c){
        int index=0;
        for(int i=0;i<childCnt;i++){
            if(childArr[i]==c){
                childArr[i]=null;
                index=i;
            }
        }
        for(int i=index;i<childCnt-1;i++){
            childArr[i]=childArr[i+1];
        }
        childCnt--;
    }

    // delete a child node of this node by receiving an index.(function overloading)
    public Node deleteChild(int index){
        Node temp = childArr[index];
        deleteChild(temp);
        return temp;
    }

    
}
