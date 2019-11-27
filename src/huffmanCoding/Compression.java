package huffmanCoding;


public class Compression {
    private String code;

    public Compression(String original) {
        RunTimeStatistics runTimeStatistics=new RunTimeStatistics("文本压缩");
        runTimeStatistics.start();
        int i = 0;
        code = "";
        int n = (8-original.length()+original.length()/8*8)%8; //缺少的位数
        String temp="";
        for (int j=0;j<n;j++) {
            temp += '0';
        }
        original+=temp;
        String[] strings=new String[original.length()/8+1];
        for (i = 0; i < original.length(); i += 8) {
            strings[i/8]= String.valueOf((char) Integer.parseInt(original, i, i + 8, 2));
        }
        strings[strings.length-1]=String.valueOf(n);
        code=String.join("",strings);
        runTimeStatistics.end();
    }
    public String getCode() {
        return code;
    }
}