package ExtractTable.ExtractTable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class GetText_toCsv {

	public static void GetData(String sourceFile, String targetFile) throws IOException,UnsupportedEncodingException {
		String[] strArr = new String[2];
		int lastindex1 = 0;
		String lastVal = "";
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<String> recordData = new ArrayList<String>();
		BufferedReader br = new BufferedReader( new InputStreamReader(new FileInputStream(sourceFile), "cp1256"));  
		String currentLine;
		FileOutputStream outputStream = new FileOutputStream(targetFile);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream, "cp1256"), true);
		FileWriter fw = new FileWriter(targetFile);
//		PrintWriter out = new PrintWriter(fw);
//		
		// Getting column names
		while ((currentLine = br.readLine()) != null) {
			PrintWriter out0 = new PrintWriter(new OutputStreamWriter(outputStream, "cp1256"), true);
			if(currentLine.contains("Records Listed"))
			{
				System.out.println("Head line is read");
				out.append('\n');
			}
			if (currentLine.contains("COMO") || currentLine.contains("DICT")|| currentLine.startsWith("[H[2JLIST") || currentLine.trim().isEmpty())
				continue;
			else if (currentLine.length() > 0) {
				if (!currentLine.startsWith(" ")) {
//					currentLine=new String(currentLine.getBytes(), "cp1256");
                    byte[] byteStr = currentLine.getBytes();
                    currentLine = new String(byteStr, "cp1256");
					currentLine = currentLine.replaceAll("\\.", "_");
					strArr = currentLine.split(" ", -1);
					strArr[0] = strArr[0].replaceAll("__", "");
					if(strArr[0].endsWith("_")) {
						strArr[0] = strArr[0].substring(0,strArr[0].length() - 1);
					}
					if (columns.size() != 0) {
						if (strArr[0].equals(columns.get(0)))
							continue;
					}
					if (!columns.contains(strArr[0])) {
						columns.add(strArr[0]);
						out0.append(strArr[0]);
						System.out.println(strArr[0]);
						out0.append(",");
					} else {
						out0.append('\n');
						out0.flush();
						System.out.println("Head line is read");
						break;
					}
				}
			}
			out0.flush();
		}
		BufferedReader br1 = new BufferedReader( new InputStreamReader(new FileInputStream(sourceFile), "cp1256")); 
		String currentLine1;

		outerloop: while ((currentLine1 = br1.readLine()) != null) {
//			PrintWriter out1 = new PrintWriter(fw);
			PrintWriter out1 = new PrintWriter(new OutputStreamWriter(outputStream, "cp1256"), true);

			if (currentLine1.contains("COMO") || currentLine1.startsWith("[H[2JLIST")
					|| currentLine1.trim().isEmpty())
				continue;
			else if (currentLine1.length() > 0) {
				if (!currentLine1.startsWith(" ")) {
                    byte[] byteStr = currentLine1.getBytes();
                    currentLine1 = new String(byteStr, "cp1256");
					currentLine1 = currentLine1.replaceAll("\\.", "");
					if (currentLine1.contains("PAGE")) {
						currentLine1 = currentLine1.replace("[H", " ");
					}
					strArr = currentLine1.split(" ", -1);
					if (recordData.size() == 0) {
						recordData.add(strArr[1]);
					} else if (strArr[0].equals(columns.get(0))) {
						if (recordData.size() > 1) {
							for (int i = 0; i <= recordData.size() - 1; i++) {
								out1.append(recordData.get(i));
//								System.out.println(recordData.get(i));
								out1.append(",");
							}
							out1.append('\n');
							out1.flush();
							System.out.println("A record is read");
						}
						recordData.clear();
						recordData.add(strArr[1]);
						continue outerloop;
					} else
						recordData.add(strArr[1]);
						for(int i=2;i<=strArr.length-1; i++)
						{
							lastindex1 = recordData.size() - 1;
							lastVal = recordData.get(lastindex1);
							if(strArr[i].length() >0)
							recordData.set(lastindex1, lastVal + " " + strArr[i]);
							else
								break;
							
						}
//						recordData.add(strArr[1]);
				} else {
					currentLine1 = currentLine1.replaceAll("\\s+", ". ");
					strArr = currentLine1.split(" ", -1);
					lastindex1 = recordData.size() - 1;
					lastVal = recordData.get(lastindex1);
					recordData.set(lastindex1, lastVal + " " + strArr[1]);
//					System.out.println(strArr[1]);
				}
			}
		}
		for (int i = 0; i <= recordData.size() - 1; i++) {
			out.append(recordData.get(i));
//			System.out.println(recordData.get(i));
			out.append(",");
		}
		out.append('\n');
		out.flush();
		System.out.println("Last record is read");
		out.close();
//		fw.close();
		outputStream.close();

		System.out.println("done Writing!");

//		} catch (FileNotFoundException e) {
//			System.out.println(e.getMessage());
	}

}
