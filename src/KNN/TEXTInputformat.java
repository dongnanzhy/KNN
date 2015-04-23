package KNN;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;


public class TEXTInputformat extends FileInputFormat<Text, vectorList>{

	static class Reader extends RecordReader<Text, vectorList> {

        private Text key;
        private vectorList value;
        private final LineRecordReader r;
        private long start;
        
        public Reader() {
            r = new LineRecordReader();
        }
        
		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			r.close();
		}

		@Override
		public Text getCurrentKey() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return key;
		}

		@Override
		public vectorList getCurrentValue() throws IOException,
				InterruptedException {
			// TODO Auto-generated method stub
			return value;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			return r.getProgress();
		}

		@Override
		public void initialize(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
            r.initialize(split, context);
            FileSplit fs = (FileSplit) split;
            start = fs.getStart();
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
            if (r.nextKeyValue()) {
                Text line = r.getCurrentValue();
                vector<String, vectorList> v = readLine(start, line.toString());
                key = new Text(v.getV1());
                value = v.getV2();
                start += line.getLength();
                return true;
            }
            return false;
        }
	}
    public static vector<String, vectorList> readLine(long start, String line) {
    	//int key = (int) start;
    	String key;
    	String[] s = line.split(",");
    	vectorList value = new vectorList();
    	for (int i = 0; i < s.length - 1; i++) {
    		value.add(Double.valueOf(s[i]));
    	}
    	int last = s.length - 1;
    	key = start + "," + s[last];
        return new vector<String, vectorList>(key, value);
    }

	@Override
	public RecordReader<Text, vectorList> createRecordReader(InputSplit arg0,
			TaskAttemptContext arg1) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return new Reader();
	}

}
