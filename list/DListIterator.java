package list;

import java.util.*;

public class DListIterator implements Iterator<Object>{

    private DList myList; 
    private ListNode current;
    public DListIterator(DList theList){
        myList = theList; 
        current = theList.front();
    }

    public boolean hasNext(){
        try{
            if(current == null){
                return false;
            }
            current.next();
            return true;
        }catch(InvalidNodeException e){
            return false;
        }
    }
    
    public Object next() throws NoSuchElementException{
        try{
            ListNode returnVal = current;
            if(!hasNext()){
                current = null;
                return returnVal.item();
            }
            current = current.next();
            return returnVal.item();
        }catch(InvalidNodeException e){
            current = null;
            throw new NoSuchElementException();
        }
    }
    
    public void remove() throws UnsupportedOperationException{
        throw new UnsupportedOperationException();
    }
}
