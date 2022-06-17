import java.util.LinkedList;
import java.util.Queue;
public class BTree {
    private Node root;  // root of B-Tree
    private int order;  // even number 

    // Constructor of B-Tree
    public BTree(int order){
        this.order = order;
        root = new Node(order-1);
    }

    //Return index of parent's childArr of current node (location of current node in parent's childArr)
    public int findIndex(Node parent, Node current){
        Node[] pChildArr = parent.getChildArr(); // parent's child array
        int pKeyCnt = parent.getKeyCnt();   // parent's key count 
        int index=-1;
        //find location of current node and copy it to index
        for(int i=0;i<=pKeyCnt;i++){
            if(pChildArr[i]==current)
                index=i;
        }
        return index;
    }
        
    // for spliting Node when the node is fulled 
    // split current node "n" by center 
    // second argument is a current node 'n' and first argument is parent of 'n' 
    public Node split(Node parent, Node n, Integer e){
        Integer[] keyArr = n.getKey();  // copy keyArr of current node 'n' to keyArr
        Node leftChild = new Node(order-1); // will be left child of spilted node
        Node rightChild = new Node(order-1); //// will be right child of spilted node
        int mid = order/2-1; // middle index of keyArr

        // if currnet node 'n' is root node
        if(n == root){
            root = new Node(order-1);   // create new root 
            root.insertKey(keyArr[mid]);    // insert middle value of current node into root
            // insert left keys of mid into left child, right key of mid into right child
            for(int i=0;i<mid;i++){
                leftChild.insertKey(keyArr[i]);
                rightChild.insertKey(keyArr[mid+i+1]);
            }
            // current node 'n' is root and have children (or has a child)
            if(!n.isLeaf()){
                Node[] childArr = n.getChildArr();  // copy childArr of 'n' to childArr
                // loop for child's child
                for(int i=0;i<=mid;i++){
                    leftChild.insertChild(i,childArr[i]);
                    rightChild.insertChild(i,childArr[mid+i+1]);
                }
                root.insertChild(0,leftChild);
                root.insertChild(1,rightChild);
            }
            // current node 'n' is root and leaf node
            else{
                root.insertChild(leftChild);    // connect root and leftChild
                root.insertChild(rightChild);   // connect root and rightChild
            }
            // return node that 'e' can be located
            if(e<keyArr[mid])
                return leftChild;
            else
                return rightChild;
        }
        // if currnet node 'n' is not root node
        else{
            int pKeyCnt = parent.getKeyCnt();
            int index = findIndex(parent, n);
            parent.insertKey(keyArr[mid]);
            for(int i=pKeyCnt;i>index;i--)
                parent.moveChild(i, i+1);
            // copy key values
            for(int i=0;i<mid;i++){
                leftChild.insertKey(keyArr[i]);
                rightChild.insertKey(keyArr[mid+i+1]);
            }
            // if currnet node 'n' is not root node and have children(or has a child)
            if(!n.isLeaf()){
                Node[] childArr = n.getChildArr();
                for(int i=0;i<=mid;i++){
                    leftChild.insertChild(i,childArr[i]);
                    rightChild.insertChild(i,childArr[mid+i+1]);
                }
            }
            parent.insertChild(index,leftChild);    // connect parent node and leftChild
            parent.insertChild(index+1,rightChild); // connect parent node and rightChild

            // return node that 'e' can be located
            if(e<keyArr[mid])
                return leftChild;
            else
                return rightChild;
        }
    }
    
    // insert the value 'e' into Tree
    public void insert(Integer e){
        insertItem(null,root,e);    // call insertItem function
    }
    
    // main algorithm of insert key in BTree
    public void insertItem(Node parent, Node n, Integer e){
        // if current node 'n' is fulled
        if(n.isFull()){
            n=split(parent,n,e);    // call split function and copy function's return value to n
        }
        // if  current node 'n' is leaf node and not fulled.
        if(n.isLeaf()){
            n.insertKey(e); // insert 'e' into 'n' node
            return; // end function
        }
        // if current node is not leaf node, go to child node
        else{
            Integer[] keyArr = n.getKey();
            Node[] childArr = n.getChildArr();
            int keyCnt = n.getKeyCnt();

            // Find the correct child node and call insertItem function
            if(e<keyArr[0]){
                insertItem(n,childArr[0],e);
                return;
            }
            if(keyArr[keyCnt-1]<e){
                insertItem(n,childArr[keyCnt],e);
                return;
            }
            for(int i=1;i<keyCnt;i++){
                if(keyArr[i-1]<e && e<keyArr[i]){
                    insertItem(n,childArr[i],e);
                    return;
                }
            }
            return; // end function
        }
    }
    

    // delete 'e' from tree
    public void delete(Integer e){
        deleteItem(null, root, e);
    }
   
    // delete 'e' from tree 
    // second argument is a current node 'n' and first argument 'parent' is parent of 'n' 
    public void deleteItem(Node parent, Node current, Integer e){
        int KeyCnt = current.getKeyCnt();
        // if the current node has a minimum number of keys
        if(KeyCnt<=order/2-1){
            // and if current node is root node
            if(current==root){
                Node[] ChildArr = root.getChildArr();
                int childCnt = root.getChildCnt();
                int cnt = root.getKeyCnt();
                for(int i=0;i<childCnt;i++)
                    cnt += ChildArr[i].getChildCnt();
                // If the root node can be merged with the child node
                if(cnt<order)
                    current = getFromParent(root, ChildArr[0]); // merge
            }
            // if current node is not root (has a parent)
            else{
                Node[] pChildArr = parent.getChildArr();
                int index = findIndex(parent, current);
                // can get a key from the left sibling node
                if(index != 0 && pChildArr[index-1].getKeyCnt()>order/2-1)  
                    getFromLeft(parent, current); // get key from the left sibling node
                // can get a key from the right sibling node    
                else if(index != parent.getChildCnt()-1 && pChildArr[index+1].getKeyCnt()>order/2-1)
                    getFromRight(parent, current);// get key from the right sibling node
                // cannot get from any sibling node    
                else{
                    current = getFromParent(parent, current);   // merge
                }
            }
        }
        // if current node is leaf node
        if(current.isLeaf()){
            // and if the current node has 'e'
            if(current.findKeyIndex(e) != -1)
                current.deleteKey(e); // delete 'e' from current node
            return; // end function
        }
        // if current node is not leaf node but has 'e'
        if(current.findKeyIndex(e)!=-1){
            // replace 'e' with the value of the leaf node
            // recursive within the replace function
            replace(current, current.findKeyIndex(e));
            return;
        }
        // Find child nodes in range and perform recursive
        else{
        Integer[] keyArr = current.getKey();
        Node[] childArr = current.getChildArr();
        int keyCnt = current.getKeyCnt();
        if(e<keyArr[0])
            deleteItem(current, childArr[0], e);
        else if(keyArr[keyCnt-1]<e)
            deleteItem(current, childArr[keyCnt], e);
        else{
            for(int i=1;i<keyCnt;i++){
                if(keyArr[i-1]<e && e<keyArr[i])
                    deleteItem(current, childArr[i], e);
            }
        }
        }
    }
  
    //Replace the value of the current node n with 'the minimum value of the right subtree' of n
    public void replace(Node n, int index){
            Integer e = n.getKey(index);
            Node temp = n.getchild(index+1);
            // find the leftmost node (temp) in the right subtree
            while(!temp.isLeaf()){
                temp = temp.getchild(0);
            }
            Integer re = temp.getKey(0);
            temp.deleteKey(0);  // delete minimum vlaue of temp
            temp.insertKey(e);  // insert e into temp 
            n.deleteKey(e); // delete e from current node
            n.insertKey(re);    // Insert minimum value of found temp 
            deleteItem(n, n.getchild(index), e);
        }
    
    // get key from left node of current node   
    public void getFromLeft(Node parent, Node current){
        Node[] pChildArr = parent.getChildArr();
        int index = findIndex(parent, current);
        Node left = pChildArr[index-1];
        int leftKeyCnt = left.getKeyCnt();
        int currentChildCnt = current.getChildCnt();
        Integer temp = parent.deleteKey(index-1);//delete key value of index of parent node
        parent.insertKey(left.deleteKey(leftKeyCnt-1));//Insert last key value of left node into parent node
        current.insertKey(temp);//Receive key values for parent nodes

        //If the current node is not a leaf node, move the child
        if(!current.isLeaf()){
            for(int i=currentChildCnt-1;i>=0;i--)
                current.moveChild(i, i+1);
            current.insertChild(0, left.deleteChild(leftKeyCnt));
        }
    }

    // get key from right node of current node   
    public void getFromRight(Node parent, Node current){
        Node[] pChildArr = parent.getChildArr();
        int index = findIndex(parent, current);
        Node right = pChildArr[index+1];
        Integer temp = parent.deleteKey(index);

        parent.insertKey(right.deleteKey(0)); //Move the first key of the right node to the parent node
        current.insertKey(temp); // get key value of parent node 
        // if current node is not leaf, get child node also
        if(!current.isLeaf())
            current.insertChild(current.getChildCnt(), right.deleteChild(0));
    }
    
    // Merge the parent node with the current node
    public Node getFromParent(Node parent, Node current){
        int index = findIndex(parent, current);
        Node left = new Node(order-1);
        Node right = new Node(order-1);
        //Find sibling nodes to merge
        if(index==0){
            left = parent.deleteChild(index);
            right = parent.deleteChild(index);
            left.insertKey(parent.deleteKey(0));
        }
        else{
            left = parent.deleteChild(index-1);
            right = parent.deleteChild(index-1);
            left.insertKey(parent.deleteKey(index-1));
        }
        int leftKeyCnt = left.getKeyCnt();
        int rightKeyCnt = right.getKeyCnt();

        // Merge to left
        // copy key values of right node to left
        for(int i=0;i<rightKeyCnt;i++)
            left.insertKey(right.getKey(i));
        // if left node is not leaf, get child node also
        if(!left.isLeaf()){
            for(int i=0;i<=rightKeyCnt;i++)
                left.insertChild(leftKeyCnt+i,right.getchild(i));
        }
        // insert left node as child of parent node
        if(index==0){
            for(int i=parent.getKeyCnt();i>=0;i--)
                parent.moveChild(i, i+1);
            parent.insertChild(0, left);
        }
        else{
            for(int i=parent.getKeyCnt();i>=index-1;i--)
                parent.moveChild(i, i+1);
            parent.insertChild(index-1, left);
        }
        return left;    // return merged node
    }
    
    // display BTree 
    public void display(){
        Queue<Node> queue = new LinkedList<Node>(); // Queue for display (FIFO)
        queue.add(root); // insert root into queue
        int cnt=1;  //Current number of levels stored in the queue
        int childCnt = 0;//Number of next levels stored in the queue
        Node node = null;
        // while the queue is not empty
        while(!queue.isEmpty()){
            //poll first node in queue
            node = queue.poll();
            cnt--;  // decrease cnt by 1
            int keyCnt = node.getKeyCnt();
            Integer[] keyArr = node.getKey();
            System.out.print("(");

            //print the keys of the pulled nodes
            for(int i=0;i<keyCnt-1;i++)
                System.out.print(keyArr[i]+", ");
            System.out.print(keyArr[keyCnt-1]+") ");

            //If the pulled node is not a leaf node, insert a child into the queue
            if(!node.isLeaf()){
                Node[] childArr = node.getChildArr();
                for(int i=0;i<=keyCnt;i++)
                    queue.add(childArr[i]);
                childCnt += keyCnt+1;
            }
            // if all nodes at the current level have been polled, update to next level
            if(cnt==0){
                cnt = childCnt;
                childCnt=0;
                System.out.println();
            }
        }
    }
}