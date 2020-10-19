import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class FileIO {
    /**
     * Read tsv file into database
     * @param path - Location of TSV file in String
     * @param tree - BPlusTree that data will be inserted into
     * @param blocks - Mimic blocks on a Main Memory
     */
    public static void readTSV(String path, BPlusTree tree, List<Blocks> blocks){
        try (BufferedReader tsvReader = new BufferedReader(new FileReader(path))){
            String line = null;
            Blocks blk= new Blocks();
            blocks.add(blk);
            int i=0;
            while ((line = tsvReader.readLine()) != null){
                if(i!=0){
                    String[] lineItems = line.split("\t"); //splitting the line and adding its items in String[]
                    Records rdata=new Records(lineItems[0],Double.parseDouble(lineItems[1]), Integer.parseInt(lineItems[2]));
                    tree.insert(Double.parseDouble(lineItems[1]), rdata);
                        
                    if(blk.recordlist.size()==4)
                    {
                        blk=new Blocks();
                    
                        blocks.add(blk);
                        blk.recordlist.add(rdata);
                    }
                    else
                    {
                        blk.recordlist.add(rdata);
                    }
                }
                i++;
                // Data.add(lineItems); //adding the splitted line array to the ArrayList
            }
            // BPlusTree.printTree();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}