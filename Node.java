public class Node{
	private Integer size;
    private Integer keyCnt;
    private Integer childCnt;
	private Integer keyArr[];
	private Node childArr[];

	public Node(Integer size) {
        this.size = size;
		keyArr = new Integer[size];
	    childArr= new Node[size+1];
        keyCnt = 0;
        childCnt = 0;
	}

    //insert
	public void insertKey(Integer e) {
        if(keyCnt>=size || e==null)
            return;
        if(keyCnt==0){
            keyArr[0]=e;
            keyCnt++;
            return;
        }
        for(int i=keyCnt-1;i>=0;i--){
            if(keyArr[i]<e){
                keyArr[i+1]=e;
                break;
            }
            else{
                keyArr[i+1]=keyArr[i];
            }
        }
        if(e<keyArr[0])
            keyArr[0]=e;
        keyCnt++;
	}

    public Integer[] getKey(){
        return keyArr;
    }
    
    public Integer getKeyCnt(){
        return keyCnt;
    }

    public Integer getKey(int index){
        return keyArr[index];
    }

    public Integer getChildCnt(){
        return childCnt;
    }

    public Node getchild(int i){
        return childArr[i];
    }

    public void insertChild(Node c){
        if(c==null)
            return;
        Integer head = c.getFirst();

        if(keyArr[0] > head){
            if(childArr[0]==null)
                childCnt++;
            childArr[0] = c;
        }
        else if(keyArr[keyCnt-1] < head){
            if(childArr[keyCnt]==null)
                childCnt++;
            childArr[keyCnt] = c;
        }
        else{
            for(int i=0; i<childCnt; i++){
                if(keyArr[i] < head && keyArr[i+1] > head ){
                    if(childArr[i]==null)
                        childCnt++;
                    childArr[i] = c;
                    return;
                }
            }
        }
	}

    public void insertChild(int index, Node c){
        if(index<=size){
            if(childArr[index] ==null)
                childCnt++;
            childArr[index]=c;
        }
    }

    public void moveChild(int from, int to){
        childArr[to] = childArr[from];
    }

    public Node[] getChildArr(){
        return childArr;
    }

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

    public Integer deleteKey(int index){
        Integer temp = keyArr[index];
        deleteKey(temp);
        return temp;
    }

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

    public Node deleteChild(int index){
        Node temp = childArr[index];
        deleteChild(temp);
        return temp;
    }


    public Integer getFirst(){
        if(isEmpty())
            return null;
        return keyArr[0];
    }

    public boolean isEmpty(){
        return keyCnt==0;
    }

    public boolean isFull(){
        return keyCnt>=size;
    }

    public boolean isLeaf(){
        return childCnt==0; 
    }
}
