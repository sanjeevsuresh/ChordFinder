package tree;
import list.*;

import java.util.Random;
public class TestTree{

    public static DList randomList(int length){
        Random rand = new Random();
        DList listy = new DList();
        for(int i = 0; i < length; i++){
            listy.insertFront(new Integer(rand.nextInt(17)));
        }
        return listy;
    }
    public static void main(String [] args){
        Tree practiceTree = new Tree(5);
        System.out.println(practiceTree);
        Random rand = new Random();
        Tree nextChild = practiceTree;
        for(int i = 0; i < 6; i++){
            nextChild.insertMult(randomList(2));
            nextChild = nextChild.insertChild(new Integer(rand.nextInt(17)));
            
        } 
        DList allPaths = new DList();
        
        System.out.println(practiceTree);       
        
        Tree.paths(practiceTree, allPaths);
        for(Object path: allPaths){
            System.out.println(path);
        }        
    }

}
