package huffmanCoding;

public class Proofread {
    private String original, code;

    public Proofread(String o, String c) {
        original = o;
        code = c;
    }

    public boolean proofread() {
        if (original.equals(code)) {
            return true;
        }
        return false;
    }
}
