public class Test{
    public static void main(String[] args) {
        System.out.println(" ********** BTree Class Test **********");
        BTree b = new BTree(6);
        for(int i=1;i<=35;i++)
            b.insert(i);
        System.out.println("Result of inserting 1 ~ 35 into BTree sequentially");
        b.display();
        b.delete(10);
        System.out.println("");
        System.out.println("Result of delete 10 from BTree");
        b.display();

        
    }
}