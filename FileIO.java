import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileIO {
    private FileIO(){
    }
    /**
     * Read tsv file into database
     * @param path - Location of TSV file in String
     * @param tree - BPlusTree that data will be inserted into
     * @param blocks - Mimic blocks on a Main Memory
     */
 
    public static void readTSV(String path, BPlusTree tree, List<Blocks> blocks){
    	Blocks blk= new Blocks();
    	List<Records> testingRecord= new ArrayList<Records>();
        try (BufferedReader tsvReader = new BufferedReader(new FileReader(path))){
            String line = null;

            int i=0;
            while ((line = tsvReader.readLine()) != null){
                if(i!=0){
                    String[] lineItems = line.split("\t"); //splitting the line and adding its items in String[]
                    Records rdata=new Records(lineItems[0],Float.parseFloat(lineItems[1]), Integer.parseInt(lineItems[2]));
                    testingRecord.add(rdata);
                }
                i++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        // Sorting
        System.out.println("Sorting");
        Collections.sort(testingRecord,Records.AvgRatingCompare);

        for(int i=0; i<testingRecord.size();i++)
        {
        	tree.insert(testingRecord.get(i).averagerating,(Records)testingRecord.get(i) );
        	if(blk.recordlist.size()==4)
        	{
        		  blk=new Blocks();
                  
                  blocks.add(blk);
        		blk.recordlist.add(testingRecord.get(i));
        	
        	}
        	else
        	{
        		blk.recordlist.add(testingRecord.get(i));
        	}
        }
        
    }
}