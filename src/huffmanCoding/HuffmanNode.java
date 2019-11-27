package huffmanCoding;

public class HuffmanNode {
    private String data;
    private int weight;
    private int parent, lchild, rchild;
    private String code;

    public HuffmanNode(String data, int weight, int parent, int lchild, int rchild, String code) {
        this.data = data;
        this.weight = weight;
        this.parent = parent;
        this.lchild = lchild;
        this.rchild = rchild;
        this.code = code;
    }

    public HuffmanNode(String data) {
        this(data, 0, -1, -1, -1, "");
    }

    public HuffmanNode(int weight, int lchild, int rchild) {
        this("", weight, -1, lchild, rchild, "");
    }


    public int getLchild() {
        return lchild;
    }

    public int getParent() {
        return parent;
    }

    public int getRchild() {
        return rchild;
    }

    public int getWeight() {
        return weight;
    }

    public String getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "data: " + data + " weight: " + weight + " parent: " + parent + " lchild: " + lchild + " rchild: " + rchild + " code: " + code + "\n\n";
    }

    public void setLchild(int lchild) {
        this.lchild = lchild;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public void setRchild(int rchild) {
        this.rchild = rchild;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }
}
