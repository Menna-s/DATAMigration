package ExtractTable.ExtractTable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompareFiles {

	static BufferedReader dataBr1;
	static BufferedReader dataBr2;
	static FileOutputStream outputStream;
	static PrintWriter out;
	static String[] dataArr = new String[400];
	static String line1;
	static String line2;
	static String sourceFileName;
	static String testedFileName;

	public CompareFiles(String sourceFile, String testedFile, String resultFile)
			throws UnsupportedEncodingException, FileNotFoundException {
		sourceFileName = sourceFile;
		testedFileName = testedFile;
		dataBr1 = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "cp1256"));
		dataBr2 = new BufferedReader(new InputStreamReader(new FileInputStream(testedFile), "cp1256"));
		outputStream = new FileOutputStream(resultFile);
		out = new PrintWriter(new OutputStreamWriter(outputStream, "cp1256"), true);
	}

	public CompareFiles(String sourceFile, String testedFile)
			throws UnsupportedEncodingException, FileNotFoundException {
		dataBr1 = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "cp1256"));
		dataBr2 = new BufferedReader(new InputStreamReader(new FileInputStream(testedFile), "cp1256"));
	}

	public void CompareFileLength() throws IOException {

		String currentLine;
		int baseFileLength = 0;
		int testedFileLength = 0;

		while ((currentLine = dataBr1.readLine()) != null) {
			baseFileLength++;
		}
		while ((currentLine = dataBr2.readLine()) != null) {
			testedFileLength++;
		}
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
		out.append("-->> Comparing Length of both files " + "_ " + timeStamp);
		out.append('\n');
		out.append('\n');
		if (baseFileLength == testedFileLength) {
			out.append("INFO: Number of Records in both files is the same " + "[" + (baseFileLength - 1) + "] records");
			out.append('\n');
		} else {
			out.append("ERROR: Number of Records in both files is NOT the same ");
			out.append('\n');
			out.append("#records in " + sourceFileName + "[" + (baseFileLength - 1) + "] columns , " + "#columns in "
					+ testedFileName + "[" + (testedFileLength - 1) + "] columns");
			out.append('\n');
		}
		out.append('\n');
		out.flush();
	}

	public void CompareData() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

		Map<String, String> rData1 = new LinkedHashMap<String, String>();
		Map<String, String> rData2 = new LinkedHashMap<String, String>();
		// insert keys of each file
		String hLine = dataBr1.readLine();
		dataArr = hLine.split(",");
		for (String item1 : dataArr) {
			rData1.put(item1, "");
		}
		hLine = dataBr2.readLine();
		hLine = hLine.replaceAll("\"", "");
		dataArr = hLine.split(",");
		for (String item1 : dataArr) {
			rData2.put(item1, "");
		}
		timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
		out.append("-->> Comparing COLUMNS in both files " + "_ " + timeStamp);
		out.append('\n');
		out.append('\n');
		if (rData1.size() == rData2.size()) {
			out.append("INFO: Number of columns in both files is the same " + "[" + rData1.size() + "] columns " + "_ "
					+ timeStamp);
			out.append('\n');
		} else {
			out.append("ERROR: Number of columns in both files is NOT the same " + "_ " + timeStamp);
			out.append('\n');
			out.append("#columns in " + sourceFileName + "[" + rData1.size() + "] columns , " + "#columns in "
					+ testedFileName + "[" + rData2.size() + "] columns");
			out.append('\n');
			
			System.out.println("ERROR: Number of columns in both files is NOT the same " + "_ " + timeStamp);
			System.out.println("#columns in " + sourceFileName + "[" + rData1.size() + "] columns , " + "#columns in "
					+ testedFileName + "[" + rData2.size() + "] columns");
		}
		out.append('\n');
		out.flush();

		timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
		for (Map.Entry<String, String> entry1 : rData1.entrySet()) {
			if (!rData2.keySet().contains(entry1.getKey())) {
				out.append("ERROR: Column " + entry1.getKey() + " missing");
				System.out.println("ERROR: Column " + entry1.getKey() + " missing");
				out.append('\n');
			}
		}
		out.append('\n');
		out.flush();
		
		
		out.append("-->> Comparing DATA in both files " + "_ " + timeStamp);
		out.append('\n');
		out.append('\n');
		while ((line1 = dataBr1.readLine()) != null) {
			byte[] byteStr1 = line1.getBytes();
			line1 = new String(byteStr1, "cp1256");
			dataArr = line1.split(",");
			int count = 0;
			String isFound = "missed";
			for (Map.Entry<String, String> entry : rData1.entrySet()) {
				rData1.put(entry.getKey(), dataArr[count]);
				if (dataArr.length - 1 != count)
					count++;
			}

			outerLoop: while ((line2 = dataBr2.readLine()) != null) {
				count = 0;

				if (line2.contains(rData1.get(rData1.keySet().toArray()[0]))) {
					isFound = "found";
					byte[] byteStr2 = line2.getBytes();
					line2 = new String(byteStr2, "cp1256");
					line2 = line2.replaceAll("\"", "");
					dataArr = line2.split(",");
					for (Map.Entry<String, String> entry : rData2.entrySet()) {
						rData2.put(entry.getKey(), dataArr[count]);
						if (dataArr.length - 1 != count)
							count++;
					}
//			
					timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
					out.append("************************ NEW RECORD ***************************");
					out.append('\n');
					out.append(
							"Verifying Record of ID : " + rData1.get(rData1.keySet().toArray()[0]) + "_ " + timeStamp);
					out.append('\n');
					out.append("-----------------------");
					out.append('\n');
					for (Map.Entry<String, String> entry1 : rData1.entrySet()) {
						{
							if (rData2.keySet().contains(entry1.getKey())) {

								innerLoop1: for (Map.Entry<String, String> entry2 : rData2.entrySet()) {
									if (entry1.getKey().equals(entry2.getKey())) {
										if (entry1.getValue().equals(entry2.getValue())) {

											out.append("INFO: Column " + entry1.getKey() + " for ID "
													+ rData1.get(rData1.keySet().toArray()[0]) + " matched");
											out.append('\n');
											out.append('\n');
										} else {
											out.append("ERROR: " + "Column " + entry1.getKey() + " for ID "
													+ rData1.get(rData1.keySet().toArray()[0]) + " not matched");
											out.append('\n');
											out.append("Base val : " + entry1.getValue() + " ,Tested val : "
													+ entry2.getValue());
											out.append('\n');
//											out.append("-----------------------");
											out.append('\n');
										}
										break innerLoop1;
									}

								}
							}
						}
					}
					break outerLoop;
				}
			}
			out.append("Record of ID : " + rData1.get(rData1.keySet().toArray()[0]) + " " + isFound);
			out.append('\n');
			out.append('\n');
			out.flush();
		}

	}

	public void GetNewAddedRecords() throws IOException {
		Map<String, String> rData2 = new LinkedHashMap<String, String>();
		line1 = dataBr1.readLine();

		line2 = dataBr2.readLine();
		dataArr = line2.split(",");
		for (String item1 : dataArr) {
			rData2.put(item1, "");
		}

		while ((line2 = dataBr2.readLine()) != null) {
			line2 = line2.replaceAll("\"", "");
			dataArr = line2.split(",");
			int count = 0;
			String isNew = "yes";
			for (Map.Entry<String, String> entry : rData2.entrySet()) {
				rData2.put(entry.getKey(), dataArr[count]);
				if (dataArr.length - 1 != count)
					count++;
			}

			while ((line1 = dataBr1.readLine()) != null) {
				count = 0;

				if (line1.contains(rData2.get("ACCT_ID"))) {
					isNew = "no";
				} else {
					isNew = "yes";
				}
			}
			if (isNew == "yes") {
				System.out
						.println("ERROR: The following account wasn't exist before migration " + rData2.get("ACCT_ID"));
			}
		}
	}

	public void Close() throws IOException {
		dataBr1.close();
		dataBr2.close();
		out.close();
		outputStream.close();
	}

}
