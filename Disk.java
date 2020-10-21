import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class Disk{
    ByteArray diskArray;
    int blockSize;
    List<Blocks> diskBlocks;
    int numBytesFilled = 0;
    int maxDiskSize = 100000000; //100MB

    // Initialise Disk Storage
    public Disk(int blockSize) {
        this.diskArray = new ByteArray(maxDiskSize);
        this.blockSize = blockSize;
        this.diskBlocks = new ArrayList<Blocks>();
    }

    // Read tsv file, insert into Disk
    public void readTSV(String path) {
        try (BufferedReader tsvReader = new BufferedReader(new FileReader(path))) {
            String line = null;
            Blocks blk = new Blocks(blockSize);
            diskBlocks.add(blk);
            line = tsvReader.readLine(); // skip header
            // For each row in the tsv, insert record into Disk
            while ((line = tsvReader.readLine()) != null){
                //splitting the line and adding its items in String[]
                String[] lineItems = line.split("\t"); 
                // Create record
                Records rdata = new Records(lineItems[0],Float.parseFloat(lineItems[1]), Integer.parseInt(lineItems[2]));
                // If block can't accomodate another record, create new block
                if(rdata.diskRecord.numBytes > blk.blockSize-blk.numBytesFilled){
                    blk = new Blocks(blockSize);
                    diskBlocks.add(blk);
                    blk.addRecord(rdata);
                }
                // Else, add record to block
                else{
                    blk.addRecord(rdata);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getTotalBytes(){
        for (Blocks b : diskBlocks){
            numBytesFilled += b.numBytesFilled;
        }
        return numBytesFilled;
    }
}
