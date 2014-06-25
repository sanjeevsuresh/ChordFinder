package tree;
import list.*;

public class Tree{
    
    protected Object entry;
    protected Tree parent;
    protected DList children;
    
    public Tree(Object entry, Tree parent){
        this.entry = entry;
        this.parent = parent;
        children = new DList();
    }
    
    public Tree(){
        entry = null;
        parent = null;
        children = new DList();
    }
    public Tree(Object entry){
        this(entry, null);
    }
    public DList children(){
        return children;
    }
    
    public boolean isLeaf(){
        return children.isEmpty();
    }
    public Tree parent(){
        return parent;
    }
    public Tree insertChild(Object item){
        Tree newChild = new Tree(item, this);
        children.insertBack(newChild);
        return newChild;
    }
    
    public void insertMult(DList stepChildren){
        for(Object stepChild: stepChildren){
            insertChild(stepChild);
        }
    }
    public DList ancestorChain(){
        Tree ancestor = this;
        DList chain  = new DList();
        while(ancestor != null){
            chain.insertFront(ancestor.entry);
            ancestor = ancestor.parent();
        }
        return chain;
    }
    
    public static void paths(Tree tree, DList paths){
        if(tree.isLeaf()){
            paths.insertBack(tree.ancestorChain());
        }else{
            for(Object child: tree.children()){
                paths((Tree) child, paths);
            }
        }
        
    }

    public String toString(){
        String _repr_ = "Tree( ";
        
        _repr_ += entry.toString();
        if(!isLeaf()){
            _repr_+= " children: ";
            for(Object child: children){
                _repr_ += child.toString();
                _repr_ += "  ";
            }
        }
        _repr_ += " )";        
        
        return _repr_;
    }
}
