package huffmanCoding;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Encode {
    private String string, code;
    private Map<Character, Integer> data;
    private int n;//字符种数
    private HuffmanTree huffmanTree;

    public Encode(String string) {
        RunTimeStatistics runTimeStatistics=new RunTimeStatistics("文本加密");
        runTimeStatistics.start();
        this.string = string;
        huffmanTree = new HuffmanTree();
        data = new HashMap<>();
        n = 0;
        init();//字符统计并建树
        code();
        runTimeStatistics.end();
    }

    public String getCode() {
        return code;
    }

    //对文本进行哈夫曼编码
    public void code() {
        RunTimeStatistics runTimeStatistics=new RunTimeStatistics("文本编码");
        runTimeStatistics.start();
        code = "";
        int index;
        char in;
        String[] codes= new String[string.length()];
        //对每个字符进行转化
        for (int i = 0; i < string.length(); i++) {
            in = string.charAt(i);
            index = data.get(in);
            codes[i] = huffmanTree.getCode(index);
        }
        code = String.join("", codes);
        runTimeStatistics.end();
    }

    public void init() {
        RunTimeStatistics runTimeStatistics=new RunTimeStatistics("文本统计");
        runTimeStatistics.start();
        //字符统计
        for (int i = 0; i < string.length(); i++) {
            if (data.containsKey(string.charAt(i))) {//出现过
                huffmanTree.setHT(data.get(string.charAt(i)),1);
            } else {//未出现过
                data.put(string.charAt(i), n);
                huffmanTree.setHT(string.charAt(i) + "");
                n++;//目前字符种数
            }
        }
        runTimeStatistics.end();
        javax.swing.JOptionPane.showMessageDialog(null, "文件字符个数字符数：" + string.length() + "，文件字符种数：" + data.keySet().size(), "字符统计", JOptionPane.INFORMATION_MESSAGE);
        huffmanTree.CreateHuffmanTree();//建树
    }

    @Override
    public String toString() {
        return huffmanTree.toString();
    }
}
