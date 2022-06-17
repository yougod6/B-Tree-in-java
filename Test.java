public class Test{
    public static void main(String[] args) {
        System.out.println(" ********** BTree Class Test **********");
        BTree b = new BTree(6);
        for(int i=1;i<=35;i++)
            b.insert(i);
        b.display();
        b.delete(10);
        b.display();

        
    }
}