package storeCluster;

import java.util.ArrayList;

public class HierarchyCluster {
	private double[][] matrix, r;
	ArrayList<ArrayList<Integer>> clusterList;
	public HierarchyCluster(double[][] r, int targetClusterNum) {
		this.r = r;
		matrix = new double[r.length][r.length];
		clusterList = new ArrayList<ArrayList<Integer>>();
		
		//init
		for (int i = 0; i < r.length; i++) {
			ArrayList<Integer> tpList = new ArrayList<Integer>();
			tpList.add(i);
			clusterList.add(tpList);
			for (int j = 0; j < r.length; j++) {
				matrix[i][j] = r[i][j];
			}
		}
		int clusterNum = r.length;
		while (clusterNum > targetClusterNum) {
			if (clusterNum % 100 == 0)
				System.out.println("clusterNum:" + clusterNum );
			int minX = 0, minY = 0;
			Double minDis = Double.MAX_VALUE;
			
			//find min distance
			for (int i = 0; i < matrix.length; i++) {
				for (int j = i + 1; j < matrix.length; j++) {
					if (matrix[i][j] >= 0 &&  matrix[i][j] < minDis){
						minDis = matrix	[i][j];
						minX = i;
						minY = j;
					}
				}
			}
			//merge two vector.
//			System.out.println("  " + minX + ", " + minY);
			merge(minX, minY);
			clusterNum--;
		}

	}
	
	public void merge(int x, int y) {
		clusterList.get(x).addAll(clusterList.get(y));
		clusterList.get(y).clear();
		for (int i = 0; i < r.length; i++) {
			matrix[i][y] = -1;
			matrix[y][i] = -1;
		}
		for (int i = 0; i < r.length; i++) {
			if (matrix[i][x] >= 0)
				matrix[i][x] = matrix[x][i] = getMeanClusterDistance(i, x);
		}
	}
	
	public double getMinClusterDistance(int x, int y) {
		double minDis = Double.MAX_VALUE; 

		for (int i : clusterList.get(x)) {
			for (int j : clusterList.get(y)) {
				if (clusterList.get(x).size() == 0 || clusterList.get(y).size() == 0)
					System.out.println("error!");
				if (r[i][j] < minDis)
					minDis = r[i][j];
			}
		}
		return minDis;
	}
	
	public double getMaxClusterDistance(int x, int y) {
		double maxDis = 0; 
		for (int i : clusterList.get(x)) {
			for (int j : clusterList.get(y)) {
				if (clusterList.get(x).size() == 0 || clusterList.get(y).size() == 0)
					System.out.println("error!");
				if (r[i][j] > maxDis)
					maxDis = r[i][j];
			}
		}
		return maxDis;
	}
	
	public double getMeanClusterDistance(int x, int y) {
		double meanDis = 0;
		int count = 0;
		for (int i : clusterList.get(x)) {
			for (int j : clusterList.get(y)) {
				if (clusterList.get(x).size() == 0 || clusterList.get(y).size() == 0)
					System.out.println("error!");
				meanDis += r[i][j];
				count++;
			}
		}
		return meanDis / count;
	}	
	
	public ArrayList<ArrayList<Integer>> getClusterList() {
		return clusterList;
	}
}
