package com.inverted.tech.mission2048.supportClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.os.Environment;

public class storePersistanceData {
	private static String dirPath = Environment.getExternalStorageDirectory()
			+ "/Android/data/com.inverted.tech.mission2048";
	private static String filePath = "/screenShot.txt";

	public static void writeData(dataStorageClass dataObj) throws IOException {
		//
		// if (!Environment.MEDIA_MOUNTED.equals(Environment
		// .getExternalStorageState()))
		// return;

		new File(dirPath).mkdirs();

		new File(dirPath + filePath).createNewFile();
		File file = new File(dirPath + filePath);
		if (file.exists()) {

			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream objOpStrm = new ObjectOutputStream(fout);

			objOpStrm.writeObject(dataObj);
			objOpStrm.close();
			fout.close();
		}
	}

	public static void writeData(Object dataObj) throws IOException {
		//
		// if (!Environment.MEDIA_MOUNTED.equals(Environment
		// .getExternalStorageState()))
		// return;

		new File(dirPath).mkdirs();

		new File(dirPath + filePath).createNewFile();
		File file = new File(dirPath + filePath);
		if (file.exists()) {

			FileOutputStream fout = new FileOutputStream(file);
			ObjectOutputStream objOpStrm = new ObjectOutputStream(fout);

			objOpStrm.writeObject(dataObj);
			objOpStrm.close();
			fout.close();
		}
	}

	public static dataStorageClass readData() throws IOException,
			ClassNotFoundException {
		//
		// if (!Environment.MEDIA_MOUNTED.equals(Environment
		// .getExternalStorageState())) {
		// return null;
		// }

		new File(dirPath).mkdirs();

		new File(dirPath + filePath).createNewFile();
		File file = new File(dirPath + filePath);
		if (file.exists()) {

			FileInputStream fin = new FileInputStream(file);
			ObjectInputStream objIpStrm = new ObjectInputStream(fin);

			dataStorageClass data = null;

			if (objIpStrm.available() != 0)
				data = (dataStorageClass) objIpStrm.readObject();

			objIpStrm.close();
			fin.close();
			return data;
		} else
			return null;

	}
}
