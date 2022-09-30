package tp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
public class all {
    static String str = "";
    @SuppressWarnings("null")




    // 计算后缀
    public static String PRNCal(String PRN) {
        Stack<String> stack1 = new Stack<String>();
        String[] ss = PRN.split(" ");
        for (int i = 0; i < ss.length; i++) {
            if (ss[i].equals("+") || ss[i].equals("-") || ss[i].equals("*")
                    || ss[i].equals("÷")) {
                String b = stack1.pop();
                String a = stack1.pop();
                int fenmu;
                int fenzi;
                // 转换
                fen f1 = new fen();
                f1 = zh(a);
                fen f2 = new fen();
                f2 = zh(b);
                int fenmu1 = f1.getFenmu();
                int fenzi1 = f1.getFenzi();
                int fenmu2 = f2.getFenmu();
                int fenzi2 = f2.getFenzi();

                if (ss[i].equals("+")) {
                    int gcm = getGCM(fenmu1, fenmu2);
                    fenzi1 = gcm / fenmu1 * fenzi1;
                    fenzi2 = gcm / fenmu2 * fenzi2;
                    fenzi = fenzi1 + fenzi2;
                    fenmu = gcm;
                    if (fenzi != 0 && fenmu != 0) {
                        int lcd = getLCD(fenzi, fenmu);
                        fenmu = fenmu / lcd;
                        fenzi = fenzi / lcd;
                    }
                    stack1.push(fenzi + "/" + fenmu);
                }
                if (ss[i].equals("-")) {
                    int gcm = getGCM(fenmu1, fenmu2);
                    fenzi1 = gcm * fenzi1 / fenmu1;
                    fenzi2 = gcm * fenzi2 / fenmu2;
                    fenzi = fenzi1 - fenzi2;
                    fenmu = gcm;
                    if (fenzi != 0 && fenmu != 0) {
                        int lcd = getLCD(fenzi, fenmu);
                        fenmu = fenmu / lcd;
                        fenzi = fenzi / lcd;
                    }
                    stack1.push(fenzi + "/" + fenmu);
                }
                if (ss[i].equals("*")) {
                    fenmu = fenmu1 * fenmu2;
                    fenzi = fenzi1 * fenzi2;
                    if (fenzi != 0 && fenmu != 0) {
                        int lcd = getLCD(fenzi, fenmu);
                        fenmu = fenmu / lcd;
                        fenzi = fenzi / lcd;
                    }
                    stack1.push(fenzi + "/" + fenmu);
                }
                if (ss[i].equals("÷")) {
                    fenmu = fenmu1 * fenzi2;
                    fenzi = fenmu2 * fenzi1;
                    if (fenzi != 0 && fenmu != 0) {
                        int lcd = getLCD(fenzi, fenmu);
                        fenmu = fenmu / lcd;
                        fenzi = fenzi / lcd;
                    }
                    stack1.push(fenzi + "/" + fenmu);
                }
            } else {
                stack1.push(ss[i]);
            }
        }
        return stack1.pop();
    }
    // 求最大公约数
    public static int getLCD(int num1, int num2) {
        if (num1 == num2)
            return num1;
        int max = Math.max(num1, num2);
        int min = Math.min(num1, num2);
        while (max % min != 0) {
            int temp = max;
            max = min;
            min = temp % min;
        }
        return min;
    }
    // 求最小公倍数
    public static int getGCM(int num1, int num2) {
        return num1 * num2 / getLCD(num1, num2);
    }
    // 转换
    public static fen zh(String b) {
        fen f = new fen();
        if (b.indexOf("'") != -1) {
            String[] ary = b.split("'");
            f.setFenshu(Integer.parseInt(ary[0].trim()));
            b = ary[1].trim();
            String[] ary1 = b.split("/");
            int c = Integer.parseInt(ary1[0].trim())
                    + Integer.parseInt(ary1[1].trim())
                    * Integer.parseInt(ary[0].trim());
            f.setFenzi(c);
            f.setFenmu(Integer.parseInt(ary1[1].trim()));
        } else if (b.indexOf("/") != -1) {
            f.setFenshu(0);
            String[] ary1 = b.split("/");
            f.setFenzi(Integer.parseInt(ary1[0].trim()));
            f.setFenmu(Integer.parseInt(ary1[1].trim()));
        } else {
            f.setFenshu(0);
            f.setFenzi(Integer.parseInt(b));
            f.setFenmu(1);
        }
        return f;
    }
    // 生成后缀
    public static String bracketGet(String s, int k) {
        int m = 0;
        int i;
        String[] arr = convert(s);
        for (i = k; i < arr.length; i++) {
            if (arr[i].equals("(")) {
                m++;
                continue;
            }
            if (arr[i].equals(")")) {
                m--;
                if (m == 0)
                    break;
                else
                    continue;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int j = k + 1; j < i; j++) {
            sb.append(arr[j]);
        }

        return sb.toString();
    }
    public static int bracketGet1(String s, int k) {
        int m = 0;
        int i;
        String[] arr = convert(s);
        for (i = k; i < arr.length; i++) {
            if (arr[i].equals("(")) {
                m++;
                continue;
            }
            if (arr[i].equals(")")) {
                m--;
                if (m == 0)
                    break;
                else
                    continue;
            }
        }
        int f = i - k - 1;
        return f;
    }
    public static String generate(String formula) {
        String[] arr = convert(formula);
        StringBuffer buffer = new StringBuffer();
        Stack<String> op = new Stack<String>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("(")) {
                buffer.append(generate(bracketGet(formula, i)));
                i = i + bracketGet1(formula, i) + 1;
            } else if (arr[i].equals("+") || arr[i].equals("-")
                    || arr[i].equals("*") || arr[i].equals("÷")) {
                while (!op.isEmpty() && opcompare2(op.lastElement(), arr[i])) {
                    buffer.append(op.pop() + " ");
                }
                op.push(arr[i]);
            } else
                buffer.append(arr[i] + " ");
        }
        while (!op.isEmpty()) {
            buffer.append(op.pop() + " ");
        }
        return buffer.toString();
    }
    public static String[] convert(String s) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i = 0; i < s.length(); i++) {
            if (!opjudge(s.charAt(i))) {//检索判断这个字符是否为运算符或者括号
                //当为运算数的时候执行以下语句
                int j = i;
                while ((i < s.length()) && !opjudge(s.charAt(i)))
                    i++;
                arrayList.add(s.substring(j, i));
                i--;
            } else
                arrayList.add(String.valueOf(s.charAt(i)));//当为运算符或括号时执行
        }
        return arrayList.toArray(new String[arrayList.size()]);
    }
    public static boolean opjudge(char c) {//判断读入是否为运算符或者括号
        if (c == '+' || c == '-' || c == '*' || c == '÷' || c == '('
                || c == ')')
            return true;
        else
            return false;
    }
    public static int opvalue2(String s) {//+-*÷的优先级定义
        if (s.equals("+")) {
            return 1;
        } else if (s.equals("-")) {
            return 1;
        } else if (s.equals("*")) {
            return 3;
        } else if (s.equals("÷")) {
            return 3;
        } else {
            return -1;
        }
    }
    public static boolean opcompare2(String s1, String s2) {//运算符优先级比较
        if (opvalue2(s1) >= opvalue2(s2))
            return true;
        else {
            return false;
        }
    }

    // 查重
    static class Node {
        String value;
        Node leftchild;
        Node rightchild;
        Node(String value) {
            this.value = value;
            leftchild = null;
            rightchild = null;
        }
    }

    // 比较两个分数的大小
    public static int check(String a, String b) {
        int fenmu;
        int fenzi;
        // 转换
        fen f1 = new fen();
        f1 = zh(a);
        fen f2 = new fen();
        f2 = zh(b);
        int fenmu1 = f1.getFenmu();
        int fenzi1 = f1.getFenzi();
        int fenmu2 = f2.getFenmu();
        int fenzi2 = f2.getFenzi();
        int gcm = getGCM(fenmu1, fenmu2);//得到两个分母的最小公倍数
        fenzi1 = gcm * fenzi1 / fenmu1;
        fenzi2 = gcm * fenzi2 / fenmu2;
        if (fenzi1 > fenzi2)
            return 1;
        else if (fenzi1 < fenzi2)
            return -1;
        else
            return 0;
    }
    /**
     * 后缀表达式转二叉表达式树
     *
     * @param suffixStr
     */
    public static void suffixExpression2Tree(String suffixStr) {
        String[] chs = suffixStr.split(" ");
        // 用于临时存储节点的栈
        Stack<Node> stack = new Stack<Node>();
        // 遍历所有字符，不是运算符的入栈，是运算符的，将栈中两个节点取出，合成一颗树然后入栈
        for (int i = 0; i < chs.length; i++) {
            String c = chs[i] + "";
            if (c.equals(" "))
                continue;
            if (isOperator(c)) {
                if (stack.isEmpty() || stack.size() < 2) {
                    System.err.println("输入的后缀表达式不正确");
                    return;
                }
                if (c.equals("+") || c.equals("*")) {
                    Node root = new Node(c);
                    Node a = stack.pop();
                    Node b = stack.pop();
                    if (isOperator(a.value) && isOperator(b.value)) {
                        if (opvalue2(a.value) > opvalue2(b.value)) {
                            root.leftchild = a;
                            root.rightchild = b;
                        } else if (opvalue2(a.value) < opvalue2(b.value)) {
                            root.leftchild = b;
                            root.rightchild = a;
                        } else {
                            if (check(a.leftchild.value, b.leftchild.value) == 1) {
                                root.leftchild = a;
                                root.rightchild = b;
                            } else if (check(a.leftchild.value,
                                    b.leftchild.value) == -1) {
                                root.leftchild = b;
                                root.rightchild = a;
                            } else {
                                if (check(a.rightchild.value,
                                        b.rightchild.value) == 1) {
                                    root.leftchild = a;
                                    root.rightchild = b;
                                } else if (check(a.rightchild.value,
                                        b.rightchild.value) == -1) {
                                    root.leftchild = b;
                                    root.rightchild = a;
                                } else {
                                    root.leftchild = a;
                                    root.rightchild = b;
                                }
                            }
                        }
                    } else if (isOperator(a.value) && (!isOperator(b.value))) {
                        root.leftchild = a;
                        root.rightchild = b;
                    } else if ((!isOperator(a.value)) && isOperator(b.value)) {
                        root.leftchild = b;
                        root.rightchild = a;
                    } else {
                        if (check(a.value, b.value) == 1
                                || check(a.value, b.value) == 0) {
                            root.leftchild = a;
                            root.rightchild = b;
                        } else if (check(a.value, b.value) == -1) {
                            root.leftchild = b;
                            root.rightchild = a;
                        }
                    }
                    stack.push(root);
                } else {
                    Node root = new Node(c);
                    root.leftchild = stack.pop();
                    root.rightchild = stack.pop();
                    stack.push(root);
                }
            } else {
                Node root = new Node(c);
                stack.push(root);
            }
        }
        if (stack.isEmpty() || stack.size() > 1) {
            System.err.println("输入的后缀表达式不正确");
            return;
        }
        preOrderTravels(stack.pop());
    }
    public static boolean isOperator(String s) {//判断是否为+-*÷
        if (s.equals("+")) {
            return true;
        } else if (s.equals("-")) {
            return true;
        } else if (s.equals("*")) {
            return true;
        } else if (s.equals("÷")) {
            return true;
        } else {
            return false;
        }
    }
    private static void visit(Node node) {//访问结点
        if (node == null) {
            return;
        }
        String value = node.value;
        str = str + value + " ";

    }
    private static void preOrderTravels(Node node) {//遍历函数
        if (node == null) {
            return;
        } else {
            preOrderTravels(node.leftchild);
            preOrderTravels(node.rightchild);
            visit(node);
        }
    }



    public static void main(String[] args) {
        int wrong = 0;//校对后的判错计数
        int correct = 0;//校对后的判对计数
        String corr = "";
        String wro = "";

        String[] repeat = null;
        int re = 0;
        Scanner scanner = new Scanner(System.in);

        if(args[0].equals("-n"))
        {
            try {
                System.out.println("输入生成题目的个数为："+args[1]);

                int n = Integer.parseInt(args[1]);

                String[] strr = new String[n + 1];
                String[] strr2 = new String[n + 1];
                repeat = new String[n + 1];
                re = 0;

                if(args[2].equals("-r")) {
                    System.out.println("输入生成题目中数值的范围："+args[3]);
                    int r = Integer.parseInt(args[3]);
                    char[] ch = { '+', '-', '*', '÷' };
                    // 清除之前的文件内容
                    try {
                        FileWriter fw = new FileWriter(
                                "./Exercises.txt");
                        fw.write("");
                        fw.close();
                    } catch (IOException e) {
                        System.out.println("文件写入失败！" + e);
                    }
                    try {
                        FileWriter fw = new FileWriter(
                                "./Answers.txt");
                        fw.write("");
                        fw.close();
                    } catch (IOException e) {
                        System.out.println("文件写入失败！" + e);
                    }

                    // 生成表达式
                    for (int i = 1; i <= n; i++) {
                        str = "";
                        int x = (int) (Math.random() * 2);
                        int y = (int) (Math.random() * 3);
                        //y用于判定加不加括号
                        int c = 0;
                        int p = 0;
                        String num = "";
                        String opera = "";
                        String ex = "";
                        for (int j = 0; j <= x; j++) {
                            int a = (int) (Math.random() * 2);
                            //随机生成范围取值为0或1的整形数据a，用于决定生成的数num是整数还是分数
                            if (a == 0) {
                                //若a为0，生成一个范围为[1,r)的随机数num
                                num = (1 + (int) (Math.random() * (r - 1))) + "";
                            } else if (a == 1) {
                                //若a为1，生成一个真分数num
                                int b = (int) (Math.random() * (r));
                                //b取值为[0,r)，是真分数'前的数字
                                int q = 2 + (int) (Math.random() * (r - 1));
                                //真分数分母
                                if (b == 0) {
                                    num = (1 + (int) (Math.random() * (q - 1)))
                                            + "/" + q;
                                } else {
                                    num = b + "'"
                                            + (1 + (int) (Math.random() * (q - 1)))
                                            + "/" + q;
                                }
                            }
                            opera = ch[(int) (Math.random() * 4)] + "";
                            //运算符不超过3个
                            if (c != 0) {
                                if ((int) (Math.random() * 2) == 1) {
                                    ex = ex + num + ")" + opera;
                                    c--;
                                }
                            }
                            if (y == 0) {
                                //y为0时，不加括号
                                ex = ex + num + opera;
                            } else if (y == 1) {
                                //y为1时，在已有算式后加左括号(
                                ex = ex + "(" + num + opera;
                                c++;
                            } else if (y == 2) {
                                //y为2时，在已有算式前加左括号(
                                ex = "(" + ex + num + opera;
                                c++;
                            }
                        }
                        ex = ex + (1 + (int) (Math.random() * (r - 1)));
                        while (true) {
                            if (c == 0)
                                break;
                            ex = ex + ")";
                            c--;
                        }
                        for (int u = 0; u <= x; u++) {
                            //p进行括号数目匹配
                            if (ex.substring(0, 1).equals("(")) {
                                p++;
                                for (int k = 1; k < ex.length(); k++) {
                                    if (ex.substring(k, k + 1).equals("("))
                                        p++;
                                    if (ex.substring(k, k + 1).equals(")")) {
                                        p--;
                                        if (k != ex.length() - 1) {
                                            if (p == 0)//p为0时，匹配正常
                                                break;
                                        } else {
                                            ex = ex.substring(1, ex.length());
                                            ex = ex.substring(0, ex.length() - 1);
                                        }
                                    }
                                }
                            }
                        }
                        String ex1 = generate(ex);
                        suffixExpression2Tree(ex1);
                        strr[i] = str;
                        strr2[i] = ex;
                        int xy = 1;
                        for (int h = 1; h < i; h++) {
                            if (strr[h].equals(str)) {//题目查重记录
                                xy = 0;
                                repeat[re] = h + "," + strr2[h] + " Repeat " + i
                                        + "," + ex;
                                re++;
                                break;
                            }
                        }
                        if (xy == 0) {
                            i--;
                            continue;
                        } else {

                            String result = PRNCal(ex1);
                            String[] ary = result.split("/");
                            int a = Integer.parseInt(ary[0].trim());
                            int b = Integer.parseInt(ary[1].trim());
                            if ((Math.abs(a) < Math.abs(b)) && (a != 0) && (b != 0)) {
                                result = a + "/" + b;
                            } else if (a == b) {
                                result = 1 + "";
                            } else if (a == -b) {
                                result = -1 + "";
                            } else {
                                int yu;
                                int da;
                                if (a == 0 || b == 0) {
                                    yu = 0;
                                    da = 0;
                                } else {
                                    yu = a % b;
                                    da = a / b;
                                }
                                if (yu == 0)
                                    result = da + "";
                                else
                                    result = da + "'" + Math.abs(yu) + "/"
                                            + Math.abs(b);
                            }
                            // 存入文件
                            try {
                                FileWriter fw = new FileWriter(
                                        "./Exercises.txt", true);
                                fw.write(i + ".");
                                fw.write(ex);
                                fw.write("=" + " " + "\r\n");
                                fw.close();
                            } catch (IOException e) {
                                System.out.println("文件写入失败！" + e);
                            }

                            try {
                                FileWriter fw = new FileWriter(
                                        "./Answers.txt", true);
                                fw.write(i + ":");
                                fw.write(result);
                                fw.write("\r\n");
                                fw.close();
                            } catch (IOException e) {
                                System.out.println("文件写入失败！" + e);
                            }

                        } //else
                    } //生成表达式for
                } //if(-r)

                else {
                    System.out.println("输入的参数错误，请重新输入！");
                }

            } //if(-n)后的try
            catch(Exception e) {
                e.printStackTrace();
            }

        }//if(-n)

        ///////////////////////

        else if(args[0].equals("-e"))//开始校对程序
        {
            try {
                InputStreamReader reader = new InputStreamReader(
                        new FileInputStream(
                                "./"+ args[1]), "UTF-8");
                BufferedReader br = new BufferedReader(reader);
                String s = null;
                if(args[2].equals("-a")) {
                    try {
                        InputStreamReader reader1 = new InputStreamReader(
                                new FileInputStream(
                                        "./"+args[3]),
                                "UTF-8");

                        BufferedReader br1 = new BufferedReader(reader1);
                        String s1 = null;
                        int i = 0;
                        corr = "";
                        wro = "";

                        while (((s = br.readLine()) != null)
                                && (s1 = br1.readLine()) != null) {
                            i++;

                            String[] st =  s.split("=",2);     //分割字符串

                            String[] st1 = s1.split(":",2);

                            if (st[1].trim().equals(st1[1].trim())) {
                                //判断题目文件“=”后的答案与答案文件“：”后的答案是否相等
                                correct++;
                                corr = corr + i + ",";
                                //校对正确，则判对计数+1
                            } else {
                                wrong++;
                                wro = wro + i + ",";
                                //校对错误，则判错计数+1
                            }
                        }//////

                        //输出批改结果
                        try {
                            FileWriter fw=new FileWriter("./Grade.txt");
                            fw.write("Correct:"+correct);
                            if(corr.length()<=1) fw.write("("+")");
                            else{
                                fw.write("(");
                                fw.write(corr.substring(0,corr.length()-1)+")");
                            }
                            fw.write("\r\n");
                            fw.write("Wrong:"+wrong);
                            if(wro.length()<=1) fw.write("("+")");
                            else{
                                fw.write("(");
                                fw.write(wro.substring(0,wro.length()-1)+")");
                            }
                            fw.write("\r\n");
                            fw.close();
                            System.out.println("批改结果输出成功！");
                        }
                        catch (IOException e){
                            System.out.println("文件写入失败！");
                        }


                    }
                    catch(IOException e) {
                        System.out.println("文件写入失败！");
                    }


                }

                else {
                    System.out.println("输入的参数错误，请重新输入！");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {
            System.out.println("输入的参数错误，请重新输入！");
        }

    }

    public static class fen {
        private int fenshu;
        private int fenmu;
        private int fenzi;
        public int getFenshu() {
            return fenshu;
        }
        public void setFenshu(int fenshu) {
            this.fenshu = fenshu;
        }
        public int getFenmu() {
            return fenmu;
        }
        public void setFenmu(int fenmu) {
            this.fenmu = fenmu;
        }
        public int getFenzi() {
            return fenzi;
        }
        public void setFenzi(int fenzi) {
            this.fenzi = fenzi;
        }

    }

}
