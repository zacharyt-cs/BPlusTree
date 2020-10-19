import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static int counter=0;
    
    public static void main(String[] args) {
        BPlusTree tree=new BPlusTree();	

        System.out.println("--------------------Experiment 1 and 2----------------------");
        List<Blocks> blo = new ArrayList<Blocks>();
        
        ArrayList<String> Data = new ArrayList<>(); //initializing a new ArrayList out of String[]'s
        try (BufferedReader TSVReader = new BufferedReader(new FileReader("data.tsv"))) {
            String line = null;
            Blocks rblock= new Blocks();
            blo.add(rblock);
            int i=0;
            while ((line = TSVReader.readLine()) != null) {
                if(i!=0){
                    String[] lineItems = line.split("\t"); //splitting the line and adding its items in String[]
                    Records rdata=new Records(lineItems[0],Double.parseDouble(lineItems[1]), Integer.parseInt(lineItems[2]));
                    tree.insert(Double.parseDouble(lineItems[1]), rdata);
                    
                    if(rblock.recordlist.size()==4){
                        rblock=new Blocks();
                        blo.add(rblock);
                        rblock.recordlist.add(rdata);
                    }
                    else{
                        rblock.recordlist.add(rdata);
                    }
                        
                }
                i++;
                // Data.add(lineItems); //adding the splitted line array to the ArrayList
            }
            // tree.printTree();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        // one data block is equal to 100
        System.out.println("The number of blocks "+ blo.size());
        System.out.println("The size of database "+blo.size()*100);
        

        // int No_of_nodes= tree.printTree();
        // need multply by node size
        // int calculateSize;
        // my degree is 4 so 8 *4 bytes, Since is 8
        // calculateSize= No_of_nodes* 96;
        
        
    	// Experiment 3
        System.out.println("------------------Experiment 3-----------------");
        List<Records> tconstValue= tree.search(8);
        for(int i= 0; i<tconstValue.size();i++)
        {
            //limit the max output to be 20 entries due to the huge amount of data
            if(i<=20){
                //printing of tconst value
                System.out.println(" The t constant value is " + tconstValue.get(i).tconstant);
            }
            else{
                break;
            }
        }
        // Print out the number of blocks and the block data
        int blockCounter=0;
        for(int j=0; j<blo.size();j++){
            List<Records> dataBlo =	blo.get(j).recordlist;
        
            for(int a=0;a<dataBlo.size();a++){
                if(dataBlo.get(a).averagerating==8){
                    blockCounter++;
                    for(int b=0; b<dataBlo.size();b++){
                        Records r = dataBlo.get(b);
                        System.out.println("Tconst is "+ r.tconstant + " Average Rating is "+ r.averagerating + " Number of votes is "+ r.numofvote);
                        
                    }
                    break;
                }
            }
        }
        System.out.print("Number of blocks: " + blockCounter);
    }
}
