package storeCluster;

import java.io.IOException;

import struct.ReadData;

public class Main {
	public static void main(String[] args) throws IOException {
		ReadData data = new ReadData();
		System.out.println("read data finish");
//		MatrixCalculate mc = new MatrixCalculate(data);
//		MonthCount mc = new MonthCount(data);
//		CalculateStoreRate cs = new CalculateStoreRate(data);
//		CalculateAccordingUser cau = new CalculateAccordingUser(data);
		
		KNNForUser kfu;
		double sumAccurate = 0;
		for (int i = 0; i < 20; i++) {
			kfu = new KNNForUser(data);
			sumAccurate += kfu.getAccurate();
		}
		System.out.println(sumAccurate / 20);
	}
}
