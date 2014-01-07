package storeCluster;

import java.io.IOException;

import struct.ReadData;

public class Main {
	public static void main(String[] args) throws IOException {
		ReadData data = new ReadData();
		System.out.println("read data finish");
//		MatrixCalculate mc = new MatrixCalculate(data);
//		MonthCount mc = new MonthCount(data);
		CalculateStoreRate cs = new CalculateStoreRate(data);
	}
}
