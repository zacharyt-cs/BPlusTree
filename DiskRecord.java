public class DiskRecord {
    // Fixed Record length of 32B
    ByteArray recordArray;
    boolean isEmpty;
    int numBytes = 32;

    public DiskRecord(){
        this.recordArray = new ByteArray(32);   // Initialise a record as ByteArray of fixed size 30B
        this.isEmpty = true;
    }

    public void setStorage(Records record){
        if (!this.isEmpty)  return;
        else this.isEmpty = false;
        ByteArray r = this.recordArray;
        r.writeBytes(record.header);            // write header into record (fixed size 4B)
        r.writeString(record.tconstant);        // write tconst into record (fixed size 20B)
        r.writeFloat(record.averagerating);     // write averageRating into record (fixed size 4B)
        r.writeInt(record.numofvote);           // write numVotes into record (fixed size 4B)
    }
    
    public void printRecord(){
        ByteArray r = this.recordArray;
        r.setPos(0);              
        r.readBytes(new byte[4]);
        String _tconst = r.readString();
        float _averageRating = r.readFloat();
        int _numVotes = r.readInt();
        System.out.println(_tconst+", "+ _averageRating + ", " + _numVotes);
    }
}

/** Num Bytes:
 * short = 2
 * long = 8
 * char = 2
 * int = 4
 * float = 4
 * double = 8
 */