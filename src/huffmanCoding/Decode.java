package huffmanCoding;

public class Decode {
    private HuffmanTree huffmanTree;
    private String code; //解密前密文
    private String text; //解密后文本

    public Decode(String tree, String code) {
        RunTimeStatistics runTimeStatistics=new RunTimeStatistics("文本解密");
        runTimeStatistics.start();
        int i = 0;
        this.code = code;
        huffmanTree = new HuffmanTree();
        String[] strings = tree.split("data: | weight: | parent: | lchild: | rchild: | code: |\\n\\n",-1);
        for (; i < strings.length-1; i++) {
            HuffmanNode huffmanNode=new HuffmanNode(strings[++i], Integer.parseInt(strings[++i]), Integer.parseInt(strings[++i]), Integer.parseInt(strings[++i]), Integer.parseInt(strings[++i]), strings[++i]);
            huffmanTree.setHT(huffmanNode);
        }
        decode();
        runTimeStatistics.end();
    }

    public void decode() {
        text = huffmanTree.TransHuffmanCode(code);
    }

    public String getText() {
        return text;
    }
}
