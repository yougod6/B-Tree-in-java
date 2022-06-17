import java.util.LinkedList;
import java.util.Queue;
public class BTree {
    private Node root;
    private int order;//even
    private int level;

    public BTree(int order){
        this.order = order;
        root = new Node(order-1);
        level = 0;
    }
        
    public Node split(Node parent, Node n, Integer e){//현재 노드(n)을 중앙을 기준으로 분할
        Integer[] keyArr = n.getKey();
        Node leftChild = new Node(order-1);
        Node rightChild = new Node(order-1);
        int mid = order/2-1;
        if(n == root){//현재 노드가 root
            root = new Node(order-1);//새로운 root 생성
            root.insertKey(keyArr[mid]);//새로운 root에 n의 중앙값 삽입
            for(int i=0;i<mid;i++){//key값 복사
                leftChild.insertKey(keyArr[i]);
                rightChild.insertKey(keyArr[mid+i+1]);
            }
            if(!n.isLeaf()){//child가 있는 경우 child 복사
                Node[] childArr = n.getChildArr();
                for(int i=0;i<=mid;i++){
                    leftChild.insertChild(i,childArr[i]);
                    rightChild.insertChild(i,childArr[mid+i+1]);
                }
                root.insertChild(0,leftChild);
                root.insertChild(1,rightChild);
            }
            else{
                root.insertChild(leftChild);
                root.insertChild(rightChild);
            }
            if(e<keyArr[mid])//e가 위치할 수 있는 노드 반환
                return leftChild;
            else
                return rightChild;
        }
        else{//현재 노드가 root가 아닌 경우
            int pKeyCnt = parent.getKeyCnt();
            int index = findIndex(parent, n);
            parent.insertKey(keyArr[mid]);
            for(int i=pKeyCnt;i>index;i--)
                parent.moveChild(i, i+1);
            for(int i=0;i<mid;i++){//key값 복사
                leftChild.insertKey(keyArr[i]);
                rightChild.insertKey(keyArr[mid+i+1]);
            }
            if(!n.isLeaf()){//child가 있는 경우 child 복사
                Node[] childArr = n.getChildArr();
                for(int i=0;i<=mid;i++){
                    leftChild.insertChild(i,childArr[i]);
                    rightChild.insertChild(i,childArr[mid+i+1]);
                }
            }
            parent.insertChild(index,leftChild);
            parent.insertChild(index+1,rightChild);
            if(e<keyArr[mid])
                return leftChild;
            else
                return rightChild;
        }
    }
    
    public void insert(Integer e){//트리에 e 삽입
        insertItem(null,root,e);
    }
    
    public void insertItem(Node parent, Node n, Integer e){//삽입 메인
        if(n.isFull()){//현재 노드(n)이 가득 찬 경우 분할
            n=split(parent,n,e);//split 함수의 반환값을 현재 노드 n에 저장
        }
        if(n.isLeaf()){//만약 현재 노드가 leaf이면
            n.insertKey(e);//e 삽입
            return;//종료
        }
        else{//현재 노드가 leaf 노드가 아니면 child 노드로 이동
            Integer[] keyArr = n.getKey();
            Node[] childArr = n.getChildArr();
            int keyCnt = n.getKeyCnt();
            if(e<keyArr[0]){//맞는 child 노드 찾아서 insertItem 함수 실행
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
            return;
        }
        
    }

    public int findIndex(Node parent, Node current){//현재 노드(current)가 부모 노드(parent)의 몇 번째 자식인지 인덱스 반환
        Node[] pChildArr = parent.getChildArr();
        int pKeyCnt = parent.getKeyCnt();
        int index=-1;
        for(int i=0;i<=pKeyCnt;i++){
            if(pChildArr[i]==current)
                index=i;
        }
        return index;
    }

    public void getFromLeft(Node parent, Node current){//현재 노드(current)의 왼쪽 노드에서 key 빌리기
        Node[] pChildArr = parent.getChildArr();
        int index = findIndex(parent, current);
        Node left = pChildArr[index-1];
        int leftKeyCnt = left.getKeyCnt();
        int currentChildCnt = current.getChildCnt();
        Integer temp = parent.deleteKey(index-1);//부모 노드의 index에 해당하는 key값 삭제
        parent.insertKey(left.deleteKey(leftKeyCnt-1));//왼쪽 노드의 마지막 key값을 부모 노드에 삽입
        current.insertKey(temp);//부모 노드의 key값 받기
        if(!current.isLeaf()){//현재 노드가 leaf 노드가 아니면 자식 옮기기
            for(int i=currentChildCnt-1;i>=0;i--)
                current.moveChild(i, i+1);
            current.insertChild(0, left.deleteChild(leftKeyCnt));
        }

    }

    public void getFromRight(Node parent, Node current){//현재 노드(current)의 오른쪽 노드에서 key 빌리기
        Node[] pChildArr = parent.getChildArr();
        int index = findIndex(parent, current);
        Node right = pChildArr[index+1];
        Integer temp = parent.deleteKey(index);
        parent.insertKey(right.deleteKey(0));//오른쪽 노드의 첫 번째 key를 부모 노드로 옮김
        current.insertKey(temp);//부모 노드의 key를 받아옴
        if(!current.isLeaf())//현재 노드가 leaf 노드가 아니면 자식도 받아오기
            current.insertChild(current.getChildCnt(), right.deleteChild(0));
    }

    public Node getFromParent(Node parent, Node current){//부모 노드와 현재 노드 병합
        int index = findIndex(parent, current);
        Node left = new Node(order-1);
        Node right = new Node(order-1);
        if(index==0){//병합할 형제 노드 찾기
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
        for(int i=0;i<rightKeyCnt;i++)//left에 병합할거임. right에 있는 key값을 left로 복사하기
            left.insertKey(right.getKey(i));
        if(!left.isLeaf()){//leaf 노드가 아닌 경우 child 노드도 복사하기
            for(int i=0;i<=rightKeyCnt;i++)
            left.insertChild(leftKeyCnt+i,right.getchild(i));
        }
        if(index==0){//parent 노드의 자식으로 left 노드 삽입
            for(int i=parent.getKeyCnt();i>=0;i--)
                parent.moveChild(i, i+1);
            parent.insertChild(0, left);
        }
        else{
            for(int i=parent.getKeyCnt();i>=index-1;i--)
                parent.moveChild(i, i+1);
            parent.insertChild(index-1, left);
        }
        return left;//병합한 노드 반환
    }

    public void replace(Node n, int index){//현재 노드(n)의 index에 해당하는 값을 n의 오른쪽 서브트리의 최솟값과 교체
        Integer e = n.getKey(index);
        Node temp = n.getchild(index+1);
        while(!temp.isLeaf()){//오른쪽 서브트리의 가장 왼쪽 아래 노드(temp) 찾기
            temp = temp.getchild(0);
        }
        Integer re = temp.getKey(0);
        temp.deleteKey(0);//temp의 최솟값 삭제
        temp.insertKey(e);//e 삽입
        n.deleteKey(e);//현재 노드에서 e 삭제
        n.insertKey(re);//찾은 temp의 최솟값 삽입
        deleteItem(n, n.getchild(index), e);
    }

    public void delete(Integer e){//트리에서 e 삭제
        deleteItem(null, root, e);
    }

    public void deleteItem(Node parent, Node current, Integer e){
        int KeyCnt = current.getKeyCnt();
        if(KeyCnt<=order/2-1){//현재 노드(current)의 key개수가 최소인 경우
            if(current==root){//만약 현재 노드가 root 노드이면
                Node[] ChildArr = root.getChildArr();
                int childCnt = root.getChildCnt();
                int cnt = root.getKeyCnt();
                for(int i=0;i<childCnt;i++)
                    cnt += ChildArr[i].getChildCnt();
                if(cnt<order)//자식 노드와 병합할 수 있으면
                    current = getFromParent(root, ChildArr[0]);//병합
            }
            else{//현재 노드가 root 노드가 아니면
                Node[] pChildArr = parent.getChildArr();
                int index = findIndex(parent, current);
                if(index != 0 && pChildArr[index-1].getKeyCnt()>order/2-1)//왼쪽 형제에서 key를 빌릴 수 있으면
                    getFromLeft(parent, current);//왼쪽 형제에서 key 빌리기
                else if(index != parent.getChildCnt()-1 && pChildArr[index+1].getKeyCnt()>order/2-1)//오른쪽 형제에서 key를 빌릴 수 있으면
                    getFromRight(parent, current);//오른쪽 형제에서 key 빌리기
                else{//형제에서 key를 빌리지 못하면
                    current = getFromParent(parent, current);//병합
                }
            }
        }
        if(current.isLeaf()){//만약 현재 노드가 leaf 노드이면
            if(current.findKeyIndex(e) != -1)//현재 노드에 찾는 값(e)가 있으면
                current.deleteKey(e);//삭제
            return;//함수 종료
        }
        if(current.findKeyIndex(e)!=-1){//현재 노드가 leaf 노드가 아닌데 찾는 값(e)가 있으면
            replace(current, current.findKeyIndex(e));//e를 leaf 노드의 값과 바꾸기, replace 함수 안에서 재귀
            return;
        }
        else{//범위에 맞는 child 노드를 찾아서 재귀 수행
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

    public void display(){//level에 맞춰서 트리 출력
        Queue<Node> queue = new LinkedList<Node>();//util의 queue 사용
        queue.add(root);//queue에 root 삽입
        int cnt=1;//queue에 저장된 현재 level 개수
        int childCnt = 0;//queue에 저장된 다음 level 개수
        Node node = null;
        while(!queue.isEmpty()){//queue가 비어있지 않으면
            node = queue.poll();//맨 앞의 노드 꺼내기
            cnt--;
            int keyCnt = node.getKeyCnt();
            Integer[] keyArr = node.getKey();
            System.out.print("(");
            for(int i=0;i<keyCnt-1;i++)//꺼낸 노드의 key 출력
                System.out.print(keyArr[i]+", ");
            System.out.print(keyArr[keyCnt-1]+") ");
            if(!node.isLeaf()){//꺼낸 노드가 leaf 노드가 아니면 queue에 자식 삽입
                Node[] childArr = node.getChildArr();
                for(int i=0;i<=keyCnt;i++)
                    queue.add(childArr[i]);
                childCnt += keyCnt+1;
            }
            if(cnt==0){//현재 level의 노드를 다 꺼낸 경우 다음 level로 업데이트
                cnt = childCnt;
                childCnt=0;
                System.out.println();
            }
            
        }
    }

}