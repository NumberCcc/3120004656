| 这个作业属于哪个课程 | [广工软件工程课程学习社区](https://bbs.csdn.net/forums/gdut-ryuezh) |
| :------------------: | ------------------------------------------------------------ |
|       作业要求       | [结对编程：小学四则运算](https://bbs.csdn.net/topics/608268113) |
|       作业目标       | 通过结对编程，实现一个自动生成小学四则运算题目的命令行程序   |

***

[TOC]

---


## 1. 成员介绍

|  姓名  |    学号    |                github地址                 |
| :----: | :--------: | :---------------------------------------: |
| 李泽熙 | 3120004656 |  https://github.com/NumberCcc/3120004656  |
| 姚楚雄 | 3120004674 | https://github.com/ycxtime2022/3120004674 |



***
## 2. 需求分析

根据题目描述，该程序应该有两个功能，一是出题，二是完成对答案的校验。

1.出题目

​	程序应该可以根据控制生成的题目数量，生成可控的数值范围，可生成整数和真分数，可生成+, −, ×, ÷,(),=以及空格的分隔符。

​	对于算式，出的题目不能重复，即任何两道题目不能通过有限次交换+和×左右的算术表达式变换为同一道题目，每道题目中出现的运算符个数不超过3个，输出答案以整数或者真分数结果显示.

​	题目文件生成后，自动存入执行程序的当前目录下的Exercises.txt文件，格式：编号.运算题目+换行。

​	在生成题目的同时，计算出所有题目的答案，并存入执行程序的当前目录下的Answers.txt文件，格式如下：编号:答案+换行

​	程序可支持1万道题目的生成。

2.答案校验

​	程序支持对给定的题目文件和答案文件，判定答案中的对错并进行数量统计。

​	输入要求：题目文件Exercises.txt （应该自行在题目文件算式的=后面写上答案）答案文件Answers.txt

​	输出要求：统计结果输出到执行程序的当前目录下的文件Grade.txt。

​	输出格式：Correct: 5 (1, 3, 5, 7, 9)

​						Wrong: 5 (2, 4, 6, 8, 10)

​	其中“:”后面的数字5表示对/错的题目的数量，括号内的是对/错题目的编号。




***
## 3. 开发环境

编程语言：Java

编译器：IntelliJ IDEA Community Edition 2020.3.3 x64

性能分析工具：JProfiler 11.1.5



***
## 4. 关键代码说明

​	1.存入文件

```
							//将题目写入Exercises文件
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
							//将答案写入Answers文件
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
                            //将校对后的结果写入Grade文件
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
```

将生成的题目和对应答案利用相对路径，分别存入程序当前目录的Exercises.txt ， Answers.txt和Grade.txt文件中。

​	2.这里先引入后缀表达式的概念：后缀表达式，又称逆波兰式，指的是不包含括号，运算符放在两个运算对象的后面，所有的计算按运算符出现的顺序，严格从左向右进行(不再考虑运算符的优先规则)。

​	使用一个栈来保存字符。遍历后缀表达式，每当遇到是非运算符的字符，就将它入栈，当遇到是运算符，就将栈中前两个结点出栈，和运算符组成一棵子树，然后入栈。遍历完成后，栈中剩下的唯一的一个结点就是该后缀表达式的二叉树的根结点。

​	后缀表达式转二叉生成树

```
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
```



***

## 5. 性能分析

1. Overview情况

![](img/xingneng1.png) 

2. 调用情况

![](img/xingneng2.png) 

3. 性能分析

由上图可以看出程序运行过程中各部分对资源的占用都在合理范围之内，资源与时间充足的情况下可以不进行进一步的性能改进。

4. 项目代码覆盖率

![](img/cover.png) 




***

## 6. 运行测试

· 测试生成题目数量n=10000 数值范围r=100

![](img/test1-166451208415611.png) ![](img/test2-166451209293313.png) 

· 将Exercises.txt中第1~10题填上错误答案或乱码，将第11~20题按Answers.txt中对应的答案填上并测试所得Grade.txt中结果如下图

![](img/test2.5-166451210755615.png) ![](img/test4-166451211869817.png) 

· 可以看到除第11~20题外所有题目的回答均为错误回答。

![](img/test3-166451214716819.png) 



***

## 7. PSP表格

|               PSP2.1                |    Personal Software Process Stages    | 预估耗时（分钟） | 实际耗时（分钟） |
| :---------------------------------: | :------------------------------------: | :--------------: | :--------------: |
|              Planning               |                  计划                  |       120        |       140        |
|              Estimate               |        估计这个任务需要多少时间        |       600        |       720        |
|             Development             |                  开发                  |        60        |        90        |
|              Analysis               |       需求分析（包括学习新技术）       |        30        |        30        |
|             Design Spec             |              生成设计文档              |        45        |        60        |
|            Design Review            |     设计复审（和同事审核设计文档）     |        15        |        20        |
|           Coding Standard           | 代码规范（为目前的开发制定合适的规范） |        30        |        30        |
|               Design                |                具体设计                |        60        |        80        |
|               Coding                |                具体编码                |        90        |       100        |
|             Code Review             |                代码复审                |        20        |        30        |
|                Test                 |  测试（自我测试，修改代码，提交修改）  |        20        |        30        |
|              Reporting              |                  报告                  |        30        |        45        |
|             Test Report             |                测试报告                |        15        |        20        |
|          Size Measurement           |               计算工作量               |        10        |        10        |
| Postmortem&Process Improvement Plan |      事后总结，并提出过程改进计划      |        20        |        20        |
|                合计                 |                                        |       1165       |       1425       |





---

## 8. 项目小结

本次结对编程中，我们遇到了许多困难，例如对括号的处理实现，对分数的表示等等，但经过一定的交流讨论和查阅书籍资料后，我俩还是解决了这些问题，但由于时间和能力不足的缘故，程序还是有些不好的地方，但这次的结对编程也加强了我们的合作交流能力，比起一个人面对，两个人更容易相互促进提升。