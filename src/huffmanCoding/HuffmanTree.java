package huffmanCoding;

import java.util.ArrayList;

public class HuffmanTree {
    private ArrayList<HuffmanNode> HT;
    private int n;
    private RunTimeStatistics runTimeStatistics;

    //开始建树
    public HuffmanTree() {
        runTimeStatistics = new RunTimeStatistics("哈夫曼树建树");
        runTimeStatistics.start();
        HT = new ArrayList<>();
        n = 0;
    }

    //统计节点添加
    //不存在该节点
    public void setHT(String data) {
        HT.add(new HuffmanNode(data)); //新增节点——data
        setHT(HT.size() - 1, 1);
    }

    //存在该节点
    public void setHT(int index, int weight) {
        HuffmanNode huffmanNode = HT.get(index);
        huffmanNode.setWeight(huffmanNode.getWeight() + weight);
        HT.set(index, huffmanNode); //改变哈夫曼树中该节点
    }

    //对读入的哈夫曼树建树
    public void setHT(HuffmanNode huffmanNode) {
        HT.add(huffmanNode);
    }

    //获取某个节点
    public String getHT(int index) {
        return HT.get(index).toString();
    }

    //获取哈夫曼编码
    public String getCode(int index) {
        return HT.get(index).getCode();
    }

    //统计完毕，建立哈夫曼树
    public void CreateHuffmanTree() {
        n = HT.size();
        int m = 2 * HT.size() - 1;
        ArrayList<Integer> s = new ArrayList<>();//最小未被选择节点index，QUESTION:数据类型
        s.add(0);
        s.add(1);
        for (int i = HT.size(); i < m; i++) {
            select(s); //选择
            HT.add(new HuffmanNode(HT.get(s.get(0)).getWeight() + HT.get(s.get(1)).getWeight(), s.get(0), s.get(1)));
            HT.get(s.get(0)).setParent(i); //修改其parent值
            HT.get(s.get(1)).setParent(i);
        }
        runTimeStatistics.end();
        CreateHuffmanCode();
    }

    //选择两个未被选择的weight最小的两个节点
    public void select(ArrayList<Integer> s) {
        int i = 0, flag = 0;
        //先找两个未被选择的作为初值
        for (; i < HT.size() && flag != 2; i++) {
            if (HT.get(i).getParent() == -1) {
                if (flag == 0) {
                    s.set(0, i);
                    flag = 1;
                } else if (flag == 1) {
                    s.set(1, i);
                    flag = 2;
                }
            }
        }
        //再选最小的
        for (; i < HT.size(); i++) {
            if (HT.get(i).getParent() == -1) {
                if (HT.get(i).getWeight() < HT.get(s.get(0)).getWeight()) {
                    s.set(0, i);
                } else if (HT.get(i).getWeight() < HT.get(s.get(1)).getWeight()) {
                    s.set(1, i);
                } else {
                    ;
                }
            }
        }
    }

    //求哈夫曼编码并储存，最终建树完毕
    public void CreateHuffmanCode() {
        RunTimeStatistics runTimeStatistics = new RunTimeStatistics("哈夫曼编码");
        runTimeStatistics.start();
        String code = "";
        int parent, c;
        for (int i = 0; i < n; i++) {
            code = "";
            c = i;
            parent = HT.get(i).getParent();
            while (parent != -1) {
                if (HT.get(parent).getLchild() == c) {
                    code += '0';
                } else {
                    code += '1';
                }
                c = parent;
                parent = HT.get(c).getParent();
            }
            code = new StringBuffer(code).reverse().toString();//逆置
            HT.get(i).setCode(code);
        }
        runTimeStatistics.end();
    }

    //对密文解码
    public String TransHuffmanCode(String code) {
        runTimeStatistics.end(); //哈夫曼树读入完毕
        RunTimeStatistics trans = new RunTimeStatistics("根据哈夫曼树解码");//解码开始
        trans.start();
        int tree;
        String ans = "";
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < code.length(); ) {
            tree = HT.size() - 1;//根
            while (!(HT.get(tree).getLchild() == -1 && HT.get(tree).getRchild() == -1)) {//未到叶子节点
                if (code.charAt(i) == '0') {
                    tree = HT.get(tree).getLchild();
                } else {
                    tree = HT.get(tree).getRchild();
                }
                i++;
            }
            temp.add(HT.get(tree).getData()); //获取叶子节点相应数据
        }
        String[] strings = new String[temp.size()]; //字符串拼接
        for (int i = 0; i < strings.length; i++) {
            strings[i] = temp.get(i);
        }
        ans = String.join("", temp);
        ans = String.join("", strings);
        trans.end();
        return ans;
    }

    @Override
    public String toString() {
        String[] strings = new String[2 * n - 1];
        for (int i = 0; i < 2 * n - 1; i++) {
            strings[i] = i + ". " + "  " + HT.get(i);
        }
        return String.join("", strings);
    }
}