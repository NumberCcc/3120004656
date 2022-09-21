package similarity;

import java.util.*;
import java.util.regex.Pattern;
import java.io.*;


public class SimilarityComparison {

 /**
 * ����˹̹����
 * @param a
 * @param b
 * @return
 */
	public static String txt2String(String filename){
		 File file= new File(filename);
		 StringBuilder result = new StringBuilder();
		 try{
		 BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ�
		 String s = null;
		 while((s = br.readLine())!=null){//ʹ��readLine������һ�ζ�һ��
		 result.append(System.lineSeparator()+s);
		 }
		 br.close(); 
		 }catch(Exception e){
		 return "error";
		 }
		 if (result.toString()==null) return "error";
		 return result.toString();
		 }
	
	
	
 public static float Levenshtein(String str_a, String str_b) { 
 if (str_a == null && str_b == null) {  //�������ڱȶԵ��ĵ���Ϊ��
 return 1f;
 }
 if (str_a == null || str_b == null) {  //����һ���ĵ�Ϊ��
 return 0f;
 }
 int editDistance = editDis(str_a, str_b);
 return 1 - ((float) editDistance / Math.max(str_a.length(), str_b.length()));
   //ʹ��'1-(�༭����/�����ַ�������󳤶�)'����ʾ���ƶ�
 }
 
 private static int editDis(String a, String b) {  //����༭����
 int aLen = a.length();
 int bLen = b.length();
 if (aLen == 0) return aLen;
 if (bLen == 0) return bLen;
 int[][] v = new int[aLen + 1][bLen + 1];
 for (int i = 0; i <= aLen; ++i) {
 for (int j = 0; j <= bLen; ++j) {
 if (i == 0) {
 v[i][j] = j;
 } else if (j == 0) {
 v[i][j] = i;
 } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
 v[i][j] = v[i - 1][j - 1];
 } else {
 v[i][j] = 1 + Math.min(v[i - 1][j - 1], Math.min(v[i][j - 1], v[i - 1][j]));
 }
 }
 }
 return v[aLen][bLen];
 }

 public static String delSpecialChar(String str) {  //���˱�����
	 String regEx = "\\pP|\\pS|\\s+";
	 str = Pattern.compile(regEx).matcher(str).replaceAll("").trim();
	 return str;
	}
 
 
 public static void main(String[] args) {
 System.out.println("�������һƪ�ĵ�·����");
 Scanner sc1 = new Scanner (System.in);
 String Text_A = sc1.nextLine();
 System.out.println("������ڶ�ƪ�ĵ�·����");
 String Text_B = sc1.nextLine();
 sc1.close();

 String F1,F2; //���ĵ���Ϊ�������쳣��������ݣ�������ʾ�����쳣
 float t1;
 F1=txt2String(Text_A);F2=txt2String(Text_B);

 if(F1 == "error") System.out.println("��һƪ�ĵ��쳣������ʧ��");
 else if(F2 == "error") System.out.println("�ڶ�ƪ�ĵ��쳣������ʧ��");
 else {
 delSpecialChar(F1); delSpecialChar(F2);
 t1=SimilarityComparison.Levenshtein(F1,F2);
 if(t1!=1f || t1!=0f)
 System.out.println("��ƪ�������ƶ�Ϊ��" + t1);
  }
 }
 
}