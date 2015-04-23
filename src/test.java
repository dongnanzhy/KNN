import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import KNN.vector;
import KNN.vectorList;


public class test {
    public ArrayList<Double> readLine(String line) {
    	String[] s = line.split(",");
    	vectorList value = new vectorList();
    	for (int i = 0; i < s.length - 1; i++) {
    		value.add(Double.valueOf(s[i]));
    	}
    	int last = s.length - 1;
    	if (s[last].equals("setosa")) {
    		value.add(0.0);
    	} else if (s[last].equals("versicolor")) {
    		value.add(1.0);
    	} else {
    		value.add(2.0);
    	}
        return value;
    }
	
	public void reader(File file) {
		ArrayList<ArrayList<Double>> test = new ArrayList<ArrayList<Double>>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String line = null;
		try {
			line = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while (line != null) {
        	ArrayList<Double> list = readLine(line);
            test.add(list);
            try {
				line = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(test);
	}
	public static void main(String[] args) {
		test t = new test();
		File file = new File("iris_train.csv");
		t.reader(file);
	}
}
