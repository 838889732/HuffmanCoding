package huffmanCoding;

import javax.swing.*;
import java.text.SimpleDateFormat;

public class RunTimeStatistics {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss SSSS");
    private String methodName;
    private long startTime, endTime;

    public RunTimeStatistics(String methodName) {
        this.methodName = methodName;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        System.out.println(methodName + "开始， 当前时间： " + simpleDateFormat.format(startTime));
    }

    public void end() {
        endTime = System.currentTimeMillis();
        System.out.println(methodName + "结束， 程序运行时间： " + (endTime - startTime) + "ms， 当前时间： " + simpleDateFormat.format(endTime));
    }

    public void getTime() {
        JOptionPane.showMessageDialog(null, methodName + "完毕， 所花时间" + (endTime - startTime) + "ms",methodName,JOptionPane.INFORMATION_MESSAGE);
    }
}
