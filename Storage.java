import java.io.UnsupportedEncodingException;

/**
 * TO-DO: Read data from TSV into a "Disk Storage" Disk => Disk is made up of
 * blocks => Each Block is fixed size of 100 Bytes => Each record is fixed size
 * of 25 Bytes => Each Block can contain up to 4 records => Each record is
 * packed with a tuple of 3 fields (tconst, avrRatings, numVotes) => A record is
 * a ByteArray of (aB, bB, cB) whereby a+b+c=25B
 * 
 * 1. Read data from tsv
 * 2. Create Disk Storage using tsv data
 * 3. Insert records from disk into Bplustree
 */
public class Storage {

    public static void main(String[] args) throws UnsupportedEncodingException {
        Records r = new Records("tt0000321", (float) 8.2, 1234);
        // r.diskRecord.printRecord();
        Records r2 = new Records("tt0000123", (float) 9.9, 4321);
        Records r3 = new Records("tt0001111", (float) 2.9, 1111111);

        int blockSize = 100;
        Blocks b = new Blocks(blockSize);
        b.addRecord(r);
        b.addRecord(r2);
        b.addRecord(r3);
        for(Records records : b.recordlist){
            records.diskRecord.printRecord();
        }
        System.out.println(b.blockSize);

        Blocks b2 = new Blocks(blockSize);
        b2.addRecord(r);
        b2.addRecord(r2);
        b2.addRecord(r3);
        for(Records records : b2.recordlist){
            records.diskRecord.printRecord();
        }
    }
}