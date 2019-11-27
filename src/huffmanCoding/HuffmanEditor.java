package huffmanCoding;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class HuffmanEditor extends JFrame implements ActionListener {
    private JMenuBar jMenuBar; //菜单栏
    private JTextArea jTextArea; //文本区域
    private JScrollPane jScrollPane; //滚动面板

    private String[] menuNameMnemonics = {"文件", "工具"};
    private String[] fileMenuNameMnemonics = {"新建", "打开", "保存", "另存为"};
    private String[] toolMenuNameMnemonics = {"编码", "解码", "压缩", "解压", "校对"};

    private static final String TITLE = "哈夫曼编码译码器"; //title
    private static final String OPEN_TITLE = " - " + TITLE;
    private static final String DEFAULT_PATH = "C:\\Users\\Vivian\\Desktop"; //默认路径

    private File currentFILE = null; //当前文件
    private String oldData = ""; //读入文本的内容
    private int openFlag = 0; //是否打开过文本
    private String opendata = "";

    public HuffmanEditor() {
        init(); //初始化界面
    }

    public void init() {
        //菜单栏初始化
        jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);      //添加菜单栏
        for (String menuNameMnemonic : menuNameMnemonics) {
            jMenuBar.add(new JMenu(menuNameMnemonic));
        }
        //文件选项初始化
        JMenu fileMenu = jMenuBar.getMenu(0); //文件选项
        for (String fileMenuNameMnemonic : fileMenuNameMnemonics) {
            JMenuItem jMenuItem = new JMenuItem(fileMenuNameMnemonic);
            fileMenu.add(jMenuItem);
            jMenuItem.addActionListener(this); //添加监听器
        }
        //工具选项初始化
        JMenu toolMenu = jMenuBar.getMenu(1);
        for (String toolMenuNameMnemonic : toolMenuNameMnemonics) {
            JMenuItem jMenuItem = new JMenuItem(toolMenuNameMnemonic);
            toolMenu.add(jMenuItem);
            jMenuItem.addActionListener(this); //添加监听器
        }
        fileMenu.add(new JSeparator()); //添加分割线
        JMenuItem jMenuItem = new JMenuItem("退出");
        fileMenu.add(jMenuItem);
        jMenuItem.addActionListener(this);

        //文本区域初始化
        jTextArea = new JTextArea();
        jTextArea.setLineWrap(true); //激活自动换行功能
        jTextArea.setWrapStyleWord(true); // 激活断行不断字功能

        //添加滑动面板
        jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(jTextArea);
        this.add(jScrollPane);

        //初始化窗体
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置窗体默认关闭方式：释放窗口
        this.setMinimumSize(new Dimension(600, 400)); //设置最小size
        this.pack(); //自动适配大小
        this.setLocationRelativeTo(null); //默认窗体居中
        this.setVisible(true); //设置窗体可见
    }

    public static void main(String[] args) {
        HuffmanEditor huffmanEditor = new HuffmanEditor();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String order = actionEvent.getActionCommand();
        if (order.equals(fileMenuNameMnemonics[0])) {
            newTextFile(); //新建
        }
        if (order.equals(fileMenuNameMnemonics[1])) {
            openTextFile("cod", "txt", "tree", "mi"); //打开
        }
        if (order.equals(fileMenuNameMnemonics[2])) {
            oldData = jTextArea.getText(); //保存
            saveFile(currentFILE, oldData);
        }
        if (order.equals(fileMenuNameMnemonics[3])) {
            saveAsFile(); //另存为
        }
        if (order.equals(toolMenuNameMnemonics[0])) {
            encode(); //编码
        }
        if (order.equals(toolMenuNameMnemonics[1])) {
            decode(); //解码
        }
        if (order.equals(toolMenuNameMnemonics[2])) {
            compression(); //压缩
        }
        if (order.equals(toolMenuNameMnemonics[3])) {
            decompression(); //解压缩
        }
        if (order.equals(toolMenuNameMnemonics[4])) {
            proofread(); //校对正确性
        }
        if (order.equals("退出")) {
            if (check())
                System.exit(0); //退出，程序结束
        }
    }

    //check文本是否发生改变，确定是否继续操作
    public boolean check() {
        //打开过文本文件
        if (openFlag == 1) {
            //发生改变
            if (!jTextArea.getText().equals(oldData)) {
                //选择操作
                Object[] objects = {"保存", "另存为", "放弃更改"};
                int response = JOptionPane.showOptionDialog(this, "请先保存文件后再进行操作", "Warning",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, objects, objects[2]); //默认放弃更改
                switch (response) {
                    case 0: //保存
                        oldData = jTextArea.getText();
                        saveFile(currentFILE, oldData);
                        break;
                    case 1: //另存为
                        saveAsFile();
                        break;
                    default: //放弃更改
                        jTextArea.setText(oldData);
                }
            }
        } else { //未打开过文本文件
            //输入过文本
            if (!jTextArea.getText().equals("")) {
                Object[] objects = {"另存为", "取消"};
                int response = JOptionPane.showOptionDialog(this, "请先保存文件后再进行操作", "Warning",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, objects, objects[1]); //默认取消操作
                switch (response) {
                    case 0:
                        saveAsFile(); //另存为
                        break;
                    default:
                        return false; //取消，不可继续操作
                }
            }
        }
        return true; //可继续进行操作
    }

    public void encode() {
        if (!check())
            return;
        RunTimeStatistics runTimeStatistics = new RunTimeStatistics("编码");
        if ((oldData.equals("") && jTextArea.isEditable() == true) || !currentFILE.getName().endsWith(".txt")) {
            JOptionPane.showMessageDialog(this, "请打开文本文件", "Warning", JOptionPane.WARNING_MESSAGE);
            openTextFile("txt");
            if (opendata.equals(""))//取消操作
                return;
        }
        runTimeStatistics.start();
        Encode encode = new Encode(oldData);
        String savePath = choose("请选择保存路径", JFileChooser.FILES_AND_DIRECTORIES, true, "cod");
        if (!savePath.endsWith(".cod")) {
            savePath += ".cod";
        }
        File code = new File(savePath);
        saveFile(code, encode.getCode());
        File tree = new File(savePath.replace(".cod", ".tree"));
        saveFile(tree, encode.toString());
        jTextArea.setEditable(false);
        runTimeStatistics.end();
        runTimeStatistics.getTime();
    }

    public void decode() {
        if (!check())
            return;
        RunTimeStatistics runTimeStatistics = new RunTimeStatistics("解码");
        String path = choose("请打开需要译码文件", JFileChooser.FILES_ONLY, false, "cod");
        if (path.equals("")) {
            return;
        }
        runTimeStatistics.start();
        File code = new File(path);
        File tree = new File(code.getPath().replace(".cod", ".tree"));
        Decode decode = new Decode(getText(tree, "UTF-8"), getText(code, "UTF-8"));
        JOptionPane.showMessageDialog(this, "译码完成", "DONE", JOptionPane.INFORMATION_MESSAGE);
        String savePath = choose("请选择保存路径", JFileChooser.FILES_AND_DIRECTORIES, true, "txt");
        if (!savePath.endsWith(".txt")) {
            savePath += ".txt";
        }
        saveTextFile(savePath, decode.getText(), true);
        runTimeStatistics.end();
        runTimeStatistics.getTime();
    }

    //压缩
    public void compression() {
        if (!check())
            return;
        RunTimeStatistics runTimeStatistics = new RunTimeStatistics("压缩");
        Object[] objects = {"txt", "cod"};
        String ans = (String) JOptionPane.showInputDialog(null, "请选择需要压缩的文件类型:",
                "编码类型", JOptionPane.QUESTION_MESSAGE, null, objects, null);
        String original;
        File file;
        runTimeStatistics.start();
        Encode encode = null;
        if (ans==null)
            return;
        if (ans.equals("txt")) {
            openTextFile("txt");
            if (opendata.equals("")) {
                return;
            }
            encode = new Encode(oldData);
            original = encode.getCode();
            file = this.currentFILE;
        } else{
            String path = choose("请选择需要压缩文件", JFileChooser.FILES_ONLY, false, "cod");
            if (path.equals("")) {
                return;
            }
            file = new File(path);
            original = getText(file, "UTF-8");
        }
        Compression compression = new Compression(original);
        String code = compression.getCode();//获取压缩后文本
        String codePath = choose("请选择压缩文件保存路径", JFileChooser.DIRECTORIES_ONLY, true, "mi");
        String fileName = file.getName().substring(0, file.getName().length() - 4);
        codePath += "\\" + fileName + ".mi";
        if (ans.equals("txt")) {
            File tree = new File(codePath.replace(".mi", ".tree"));
            saveFile(tree, encode.toString());
        }
        saveTextFile(codePath, code, false);
        //显示压缩比例
        JOptionPane.showMessageDialog(null, "压缩完成\n压缩前文件(" + file.getName() + ")大小为" +
                        UnitConversion(file) + "\n压缩后文件(" + currentFILE.getName() + ")大小为" + UnitConversion(currentFILE) + "\n压缩比："
                        + (100.0 * currentFILE.length() / file.length()) + "%"
                , "压缩结果", JOptionPane.INFORMATION_MESSAGE);
        runTimeStatistics.end();
        runTimeStatistics.getTime();
    }

    //解压
    public void decompression() {
        if (!check())
            return;
        RunTimeStatistics runTimeStatistics = new RunTimeStatistics("解压");
        String path = choose("请选择需要解压缩的文件", JFileChooser.FILES_ONLY, false, "mi");
        if (path.equals("")) {
            return;
        }
        runTimeStatistics.start();
        String mi = getText(new File(path), "UTF-8");//密文
        String tree = path.replace(".mi", ".tree");
        String huffmanTree = getText(new File(tree), "UTF-8");//哈夫曼树
        Decompression decompression = new Decompression(mi, huffmanTree);//解压
        String text = decompression.getText();//获取解压后文本
        String savePath = choose("请选择解压后文件保存路径", JFileChooser.FILES_AND_DIRECTORIES, true, "txt");
        if (!savePath.endsWith(".txt"))
            savePath += ".txt";
        saveTextFile(savePath, text, false);
        runTimeStatistics.end();
        runTimeStatistics.getTime();
    }

    //校对
    public void proofread() {
        openTextFile("txt");//打开译码前文件
        if (opendata.equals(""))//未打开，取消操作
            return;
        String string = choose("请选择译码后文件", JFileChooser.FILES_ONLY, false, "txt");
        if (string.equals(""))//未打开，取消操作
            return;
        File f = new File(string);
        String code = getText(f, "UTF-8");//获取译码后文件文本内容
        Proofread proofread = new Proofread(this.oldData, code);
        String message = "";
        if (proofread.proofread()) {
            message = "文本匹配！！！"
                    + "\n译码前文件(" + currentFILE.getName() + ")大小为： " + UnitConversion(currentFILE)
                    + "\n译码后文件(" + f.getName() + ")大小为： " + UnitConversion(f);
        } else {
            message += "文本不匹配！！!";
        }
        JOptionPane.showMessageDialog(null, message, "匹配结果", JOptionPane.INFORMATION_MESSAGE);
        jTextArea.setEditable(false);//对比完毕，不可编辑
    }

    public void openTextFile(String... extensions) {
        String path = choose("选择文件", JFileChooser.FILES_ONLY, false, extensions);
        if (path.equals("")) {
            opendata = "";
            return;
            //未选择，取消操作
        }
        currentFILE = new File(path);
        this.setTitle(currentFILE.getPath() + OPEN_TITLE);//设置title
        Object[] objs = {"UTF-8", "GBK"};//选择编码
        String code = (String) JOptionPane.showInputDialog(null, "请选择文件编码类型:", "编码类型", JOptionPane.QUESTION_MESSAGE, null, objs, null);
        opendata = oldData = getText(currentFILE, code);//获取文本
        new Runnable() {
            @Override
            public void run() {
                jTextArea.setText(oldData);
            }
        }.run();
        openFlag = 1;//标记打开过文本
        jTextArea.setEditable(true);//可编辑
    }

    //获取文件文本
    public String getText(File f, String code) {
        RunTimeStatistics runTimeStatistics = new RunTimeStatistics("读取文件(" + f.getName() + ", 大小：" + UnitConversion(f) + ")");
        runTimeStatistics.start();
        String str = "";
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(f), code);
            Long len = f.length();
            char[] fileContent = new char[len.intValue()];
            int length = inputStreamReader.read(fileContent);
            str = new String(fileContent, 0, length);//一次性读入
            inputStreamReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        runTimeStatistics.end();
        return str;
    }

    //选择文件或地址
    public String choose(String title, int fileSelectionMode, boolean isSave, String... extensions) {
        String path = "";//地址
        JFileChooser jFileChooser = new JFileChooser(DEFAULT_PATH);//默认路径设置
        jFileChooser.setDialogTitle(title);//设置title
        jFileChooser.setFileSelectionMode(fileSelectionMode);
        int returnValue;//选中或是取消
        jFileChooser.setFileFilter(new FileNameExtensionFilter("文本文件", extensions));
        if (isSave) {//保存
            returnValue = jFileChooser.showSaveDialog(this);
        } else {//打开
            returnValue = jFileChooser.showOpenDialog(this);
        }
        if (returnValue == JFileChooser.APPROVE_OPTION) {//选中
            path = jFileChooser.getSelectedFile().getPath();//记住路径
        } else {//取消
            if (isSave) {//保存必须选中
                JOptionPane.showMessageDialog(null, title, "Warning", JOptionPane.WARNING_MESSAGE);
                choose(title, fileSelectionMode, isSave, extensions);
            }//打开返回空路径
        }
        return path;
    }

    //新建
    public void newTextFile() {
        if (!check())//检查文本，是否保存
            return;
        oldData = "";
        jTextArea.setText(oldData);
        openFlag = 0;
        jTextArea.setEditable(true);
        this.setTitle("无标题" + OPEN_TITLE);
    }

    //保存文件
    public void saveFile(File f, String str) {
        try {
            RunTimeStatistics runTimeStatistics = new RunTimeStatistics("保存文件(" + f.getName() + ")");
            runTimeStatistics.start();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            bufferedWriter.write(str);
            bufferedWriter.close();
            runTimeStatistics.end();
        } catch (Exception ex) {
            saveAsFile();//文件为空指针，另存为文本文件
        }
    }

    //另存为，当前显示文本另存为文本文件
    public void saveAsFile() {
        String path = choose("另存为", JFileChooser.FILES_AND_DIRECTORIES, true, ".txt");
        if (!path.endsWith(".txt")) {
            path += ".txt";
        }
//        oldData = jTextArea.getText();
//        currentFILE = new File(path);
//        saveFile(currentFILE, oldData);
//        openFlag = 1;
//        this.setTitle(currentFILE.getPath() + OPEN_TITLE);
        saveTextFile(path, jTextArea.getText(), true);
    }

    //保存并显示该文件
    public void saveTextFile(String path, String data, boolean isEditable) {
        oldData = data;
        currentFILE = new File(path);
        saveFile(currentFILE, data);
        openFlag = 1;
        this.setTitle(currentFILE.getPath() + OPEN_TITLE);
        jTextArea.setEditable(isEditable);
        new Runnable() {
            @Override
            public void run() {
                jTextArea.setText(data);
            }
        }.run();
    }

    //计算文件大小
    public String UnitConversion(File f) {
        double size = (double) f.length();
        int i = 0;
        String[] units = {"B", "KB", "MB", "GB"};
        while (size >= 1024 || i >= units.length - 1) {
            size /= 1024;
            i++;
        }
        return String.format("%.3f", size) + units[i];
    }
}