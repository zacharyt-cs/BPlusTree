import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * TO-DO: Read data from TSV into a "Disk Storage" Disk => Disk is made up of
 * blocks => Each Block is fixed size of 100 Bytes => Each record is fixed size
 * of 25 Bytes => Each Block can contain up to 4 records => Each record is
 * packed with a tuple of 3 fields (tconst, avrRatings, numVotes) => A record is
 * a ByteArray of (aB, bB, cB) whereby a+b+c=25B
 * 
 * 1. Read data from tsv
 * 2. Create Disk Storage
 *    a. Line by line, add record to Disk
 *    b. 
 */
public class Storage {

    public static void main(String[] args) throws UnsupportedEncodingException {
        // Fixed Record length of 26B
        ByteArray record = new ByteArray(26);   // Initialise a record as ByteArray of fixed size 26B
        record.writeInt(2279223);               // write numVotes into record (fixed size 4B)
        record.writeFloat((float)9.9);          // write averageRating into record (fixed size 4B)
        record.writeString("tt0000731");        // write tconst into record (fixed size 18B)
        record.setPos(0);
        int _numVotes = record.readInt();
        float _averageRating = record.readFloat();
        String _tconst = record.readString();
        System.out.println(_numVotes+", "+ _averageRating + ", " + _tconst);
        System.out.println(record.getPos());    // returns 26
    }

    // public static ByteBuffer allocate(int capacity) {
    //     byte[] byteArray = new byte[capacity];
    //     ByteBuffer buf = ByteBuffer.wrap(byteArray);
    //     System.out.println(buf);
    //     return buf;
    // }
}

/** Num Bytes:
 * short = 2
 * long = 8
 * char = 2
 * int = 4
 * float = 4
 * double
 */