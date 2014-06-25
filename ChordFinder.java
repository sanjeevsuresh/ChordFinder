import list.*;
import dict.*;
import tree.*;

public class ChordFinder{

    private static String fret_repr = "-------";
    private static String fret_pressed = "---X---";
    private static String wall = "|";
    private static int fretSize = 17;
        
    public static String [] standardE = {"E", "A", "D" , "G", "B", "E"};
    public static String [] westernNotes = {"C", "D", "E", "F", "G", "A" , "B", "C"};
    public static HashTableChained hashedScale;
    public static String [] sharps;
    public static String [] flats;

    public static void drawNeck(int length){
        String [] tuning = tabVersion(standardE);
        String _repr_ = "";
        for(String openNote : tuning){
            _repr_+= ("\n" + openNote + " o");
            for(int i = 0; i < length; i++){
                _repr_ += fret_repr;
                _repr_ += wall;
            }
        }
        System.out.println(_repr_);
    }    
    
    public static Integer [] objToInt(DList shape){
        Object [] objArray = shape.toArray();
        Integer [] intArray = new Integer[objArray.length];
        int index = 5;
        for(Object obj: objArray){
            intArray[index] = (Integer) obj;
            index--;
        }
        return intArray;
    }
    
    public static Integer findMin(Integer [] array){
        Integer minItem = new Integer(9000);
        for(Integer item: array){
            if(minItem > item){
                minItem = item;
            }
        }
        return minItem;
    }
    public static void print(Integer [] array){
        String _repr_ = "[ ";
        for(Integer item : array){
            _repr_ += (" " + item.toString() + " ");
        }
        _repr_ += " ]";
        System.out.println(_repr_);
    }
    public static void print(Object obj){
        System.out.println(obj);
    }
    
    public static String drawNeckNumberings(int minItem, int neckLength){
        if(minItem <= 0){   
            String numberings = "\nX *";
            for(int i = minItem + 1; i < minItem + neckLength + 1; i++){
                numberings += "   " + i + "   " + "|";
            }
            return numberings;
        }                
        String numberings = "\nX *";
        for(int i = minItem; i < minItem + neckLength; i++){
            numberings += "   " + i + "   " + "|";
        }
        return numberings;
    }

    public static void drawShape(int neckLength, String [] tuning, DList shape){
        tuning = tabVersion(tuning);
        String _repr_ = "";
        String openNote;
        
        Integer [] shapeArray = objToInt((DList) shape);
        int minItem = findMin(shapeArray);
        print(shapeArray);
        for(int i = 0; i < tuning.length; i++){
            openNote = tuning[i];
            _repr_+= ("\n" + openNote + " o");
            if(minItem > 0){
                for(int j = 0; j < neckLength; j++){
                    if(shapeArray[i] - minItem == j){
                        _repr_ += fret_pressed;
                    }else{
                        _repr_ += fret_repr;
                    }
                        _repr_ += wall;
                }
            }else{
                for(int j = 0; j < neckLength; j++){
                    if(shapeArray[i] != 0 && (shapeArray[i] - 1) == j){
                        _repr_ += fret_pressed;
                    }else{
                        _repr_ += fret_repr;
                    }
                        _repr_ += wall;
                    }
                }
        }
        String numberings = drawNeckNumberings(minItem, neckLength);
        _repr_ += numberings;
        System.out.println(_repr_);
    }
    
    /*
     * A method that attempts to find draw all given chord shapes;
     * 
     */
    public static void drawAllShapes(int neckLength, String [] tuning, DList shapes){
        int count = 0;
        //Draw every shape
        for(Object shape: shapes){
            //System.out.println("Hit count: " + count);
            drawShape(neckLength, tuning, (DList) shape);
            count++;
        }
    }

    
    public static String [] tabVersion(String [] tuning){
        String [] tabVersion = new String[6];
        for(int i = 0; i < 6; i++){
            tabVersion[i] = tuning[(6 - i) - 1];
        }
        return tabVersion;
    }
    public static String [] grabTuning(){
        return standardE;
    }
    public static void print(String [] things){
        String _repr_ = "[";
        for(String item: things){
            _repr_ += (" " + item);
        }
        _repr_ += " ]";
        System.out.println(_repr_);
    }

    public static DList findFretNumbers(String note, String openString){
        int openStringIndex = (int) hashedScale.find(openString).value();
        int desiredNote = (int) hashedScale.find(note).value();
        DList fretNumbers = new DList();
        for(int pressDown = 0; pressDown < fretSize; pressDown++){
            int notePressed = (openStringIndex + pressDown) % 12;
            if(notePressed == desiredNote){
                fretNumbers.insertBack(new Integer(pressDown));
            }
        }
        return fretNumbers;
    }
    
    //Made of a Major Third and Fifth
    public static DList [] majorTriad(String root, String [] scale){
        DList [] notesPerString = new DList[6];

        int rootIndex = (int) hashedScale.find(root).value();
        int majThirdIndex = (rootIndex + Intervals.MIN3) % 12;
        int fifthIndex = (rootIndex + Intervals.FIFTH) % 12;
        
        String majThird = scale[majThirdIndex];
        String perFifth = scale[fifthIndex];
        
        System.out.println(root + "\n" + majThird + "\n" + perFifth);
        
        int index = 0;
        String [] tuning = grabTuning();
        for(String openString: tuning){
            DList rootList = findFretNumbers(root, openString);
            DList thirds = findFretNumbers(majThird, openString);
            DList fifths = findFretNumbers(perFifth, openString);
            
            DList allHarmonies = rootList.merge(thirds).merge(fifths);
            notesPerString[index] = allHarmonies;
            index++;
        }
        return notesPerString;
    }
    
    public static boolean inRange(int i, int min, int max){
        if(i >= min && i < max){
            return true;
        }
        return false;
    }
        
    public static int abs(int i){
        if(i < 0){
            return -i;
        }
        return i;
    }
    public static DList filterByFretLength(int fret, DList fretNums, int stretch){
        DList filteredNums = new DList();
        for(Object item : fretNums){
            Integer fretNum = (Integer) item;
            if(inRange(abs(fret - fretNum), 0, stretch)){
                filteredNums.insertFront(item);
            }else{
                continue;
            }
        }
        return filteredNums;
    }

    public static Tree[] makeTrees(DList [] notesPerString){
        Object [] roots = notesPerString[0].toArray();
        Tree [] trees = new Tree[roots.length];
        for(int i = 0; i < trees.length; i++){
            trees[i] = new Tree(roots[i], null); //make Roots (doesn't have to be 6thString)
        }
        for(int i = 0; i < trees.length; i++){
           DList treeChildren = new DList();
           treeChildren.insertFront(trees[i]);
           
           for(int j = 1; j < notesPerString.length; j++){
              DList filtered = filterByFretLength((Integer) roots[i], notesPerString[j], 5);
              
              for(Object child: treeChildren){
                ((Tree) child).insertMult(filtered);
              }
              DList newTreeChildren = new DList();
              for(Object child: treeChildren){
                DList grandChildren = ((Tree)child).children();
                for(Object grandChild: grandChildren){
                    newTreeChildren.insertFront(grandChild);
                }
              }
              treeChildren = newTreeChildren;
           }
        }
        return trees;
    }
    
    public static DList filterStretch(DList paths, int stretch){
        Integer maxItem = new Integer(-9000);
        Integer minItem = new Integer(9000);
        DList filteredPaths = new DList();
        for(Object path: paths){
            DList shape = (DList) path;
            Object [] objArray = shape.toArray();
            Integer [] intArray = new Integer[objArray.length];
            int index = 0;
            for(Object obj: objArray){
                intArray[index] = (Integer) obj;
                index++;
            }
            for(int i = 0; i < intArray.length; i++){
                if(intArray[i] < minItem){
                    minItem = intArray[i];
                }
                if(intArray[i] > maxItem){
                    maxItem = intArray[i];
                }
            }
            if((maxItem - minItem) <= stretch){
               filteredPaths.insertBack(path);     
            }
            maxItem = new Integer(-1);
            minItem = new Integer(9000);
        }
        return filteredPaths;
    }


    public static void findChord(String root, String chordType, String[] scale, String [] tuning, int stretch){
        DList [] harmonies = majorTriad(root, scale);
        System.out.println("Frets with the Chord Notes: ");
        for(int i = 0; i < 6; i++){
            print(harmonies[6 - i - 1]);
        }
        
        Tree [] trees = makeTrees(harmonies);
        
        DList allPaths = new DList();
        for(Object tree: trees){
            ((Tree) tree).paths((Tree) tree, allPaths);
        }
        DList filteredPaths = filterStretch(allPaths, stretch);
        /*for(Object path: filteredPaths){
            System.out.println(path);
        }*/
        drawAllShapes(stretch, tuning, filteredPaths);

    }
    public static void main(String [] args){
        drawNeck(5);
        MusicSetUp.makeSharps();
        MusicSetUp.makeFlats();
        print(MusicSetUp.withFlats);
        print(MusicSetUp.withSharps);
        flats = MusicSetUp.withFlats;
        sharps = MusicSetUp.withSharps;
        hashedScale = MusicSetUp.makeHashedScale();

        /*
        for(String note: MusicSetUp.withSharps){
            System.out.println(note + "\t" + note.hashCode() + "\t" + (note.hashCode() % 29));
        }
        for(String note: MusicSetUp.withFlats){
            System.out.println(note + "\t" + note.hashCode() + "\t" + (note.hashCode() % 29));
        }*/

        System.out.println(findFretNumbers("G", "E"));
        System.out.println(findFretNumbers("D", "A"));
        System.out.println(findFretNumbers("B", "B"));
        System.out.println(findFretNumbers("B", "E"));
        findChord("D", "Min", flats, standardE, 5);
    
        DList openG = new DList(new Integer [] 
                                {new Integer(3),
                                 new Integer(2),
                                 new Integer(0),    
                                 new Integer(0),
                                 new Integer(0),
                                 new Integer(3)});
        DList barG = new DList(new Integer [] 
                                {new Integer(3),
                                 new Integer(5),
                                 new Integer(5),    
                                 new Integer(4),
                                 new Integer(3),
                                 new Integer(3)});

        DList shapes = new DList();
        shapes.insertFront(openG);
        shapes.insertBack(barG);
        //drawAllShapes(5,shapes);
        
    }
}
