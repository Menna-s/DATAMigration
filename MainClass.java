package ExtractTable.ExtractTable;

import java.io.IOException;

public class MainClass {

	public static void main(String[] args) throws IOException {
		String baseFile = "ACCOUNT_ACCRUAL";
		String sourceFile = baseFile + "_S";
		String targetFile = baseFile + "_T";
		String resultFile = baseFile + "Result";
		GetText_toCsv.GetData(baseFile+".TXT","D:/T24 jBase/"+baseFile+"_S.csv");
		System.out.println("done transfering!");

//		CompareFiles compare = new CompareFiles(sourceFile + ".csv", targetFile + ".csv",
//				"D:/T24 jBase/" + resultFile + ".txt");
//		compare.CompareFileLength();
//		compare = new CompareFiles(sourceFile + ".csv", targetFile + ".csv");
////		compare.GetNewAddedRecords();
////		compare = new CompareFiles("CUSTOMER_ACCOUNT_S.csv", "CUSTOMER_ACCOUNT_T.csv");
//		compare.CompareData();
//		compare.Close();
//		System.out.println("done Comparing!");
		System.out.println("Done");
	}
	
}
