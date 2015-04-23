package KNN;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;


public class vectorSD extends vector<String,Double> implements Writable {
    public vectorSD() {
    }

    public vectorSD(String v1, Double v2) {
            this.v1 = v1;
            this.v2 = v2;
    }

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
        v1 = in.readUTF();
        v2 = in.readDouble();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
        out.writeUTF(v1);
        out.writeDouble(v2);
	}
}
