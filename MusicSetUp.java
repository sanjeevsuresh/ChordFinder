import dict.*;
public class MusicSetUp{
        
    public static String [] standardE = {"E", "A", "D" , "G", "B", "E"};
    public static String [] westernNotes = {"C", "D", "E", "F", "G", "A" , "B", "C"};
    
    
    public static String [] withFlats = new String[13];
    public static String [] withSharps = new String[13];
        
    public static void makeSharps(){
        for(int i = 0; i < 8; i++){
            if(i < 2){
                withSharps[2*i] = westernNotes[i];
                withSharps[2*i + 1] = westernNotes[i] + "#";
            }else if (i == 2){
                //No E#
                withSharps[2*i] = westernNotes[i];
            }else if (i == 6){
                //No B#
                withSharps[2*i - 1] = westernNotes[i];
            }else if (i == 7){
                //C is last
                withSharps[2*i - 2] = westernNotes[i];
            }else{
                withSharps[2*i - 1] = westernNotes[i];
                withSharps[2*i] = westernNotes[i] + "#";
            }
        }   
    }
    public static void makeFlats(){
        for(int i = 0; i < 8; i++){
            if(i < 2){
                withFlats[2*i] = westernNotes[i];
                withFlats[2*i + 1] = westernNotes[i + 1] + "b";
            }else if (i == 2){
                //No F-flat
                withFlats[2*i] = westernNotes[i];
            }else if (i == 6){
                //No C-flat
                withFlats[2*i - 1] = westernNotes[i];
            }else if (i == 7){
                //C is last
                withFlats[2*i - 2] = westernNotes[i];
            }else{
                withFlats[2*i - 1] = westernNotes[i];
                withFlats[2*i] = westernNotes[i + 1] + "b";
            }
        }   
    }
    
    public static HashTableChained makeHashedScale(){
        HashTableChained hashedScale = new HashTableChained(24);
        for(int i = 0; i < withFlats.length; i++){
            hashedScale.insert(withFlats[i], i);
            hashedScale.insert(withSharps[i], i);
        }
        return hashedScale;
    }   
    
}
