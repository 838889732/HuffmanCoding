package huffmanCoding;

public class Decompression {
    private String code;
    private Decode decode;

    public Decompression(String mi, String tree) {
        RunTimeStatistics runTimeStatistics = new RunTimeStatistics("文本解压");
        runTimeStatistics.start();
        RunTimeStatistics runTimeStatistics1 = new RunTimeStatistics("解压");
        runTimeStatistics1.start();
        int n = mi.charAt(mi.length() - 1)-'0';
        String[] strings = new String[mi.length() - 1];
        System.out.println(strings.length+","+mi.length()+","+n);
        byte temp;
        for (int i = 0; i < mi.length() - 1; i++) {
            temp = (byte) mi.charAt(i);
            strings[i] = Integer.toBinaryString((temp & 0xFF) + 0x100).substring(1);
        }
        strings[strings.length - 1] = strings[strings.length - 1].substring(0, 8 - n);
        runTimeStatistics1.end();
        code = String.join("", strings);
        decode = new Decode(tree, code);
        runTimeStatistics.end();
    }
    public String getText(){
        return decode.getText();
    }
}
