import java.util.ArrayList;
import java.util.List;

public class Blocks {
	ByteArray blk;
	byte[] header = new byte[10];
	int blockSize;
	public List<Records> recordlist= new ArrayList<Records>();
	int numBytesFilled;

	public Blocks(int blockSize){
		this.blockSize = blockSize;
		this.blk = new ByteArray(blockSize);
		blk.writeBytes(header);
		numBytesFilled = blk.getPos();
	}

	public void addRecord(Records data){
		recordlist.add(data);
		int recordSize = data.diskRecord.recordArray.getLength();
		// System.out.print(blk.getPos());
		for(int i=0; i<recordSize/2; i++){
			blk.writeChar('1'); //fill with 2 bytes
		}
	}
}