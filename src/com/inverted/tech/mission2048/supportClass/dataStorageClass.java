package com.inverted.tech.mission2048.supportClass;

import java.io.Serializable;

@SuppressWarnings("serial")
public class dataStorageClass implements Serializable {

	public int normalBestScore[] = new int[11];
	public int timerBestScore[] = new int[11];
	public int limitedBestScore[] = new int[11];

	public dataStorageClass() {
		for (int i = 0; i < normalBestScore.length; i++) {
			normalBestScore[i] = 0;
			timerBestScore[i] = 0;
			limitedBestScore[i] = 0;
		}
	}
}
