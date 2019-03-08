package snc.boot.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.apache.log4j.Logger;
import org.junit.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVUtil {
    private static Logger log = Logger.getLogger(CSVUtil.class);
    public static void writeCSV(String type, List<String[]> data){
        File file = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date();
        String dt = simpleDateFormat.format(date);
        switch (type) {
            case "class":
                String classfile = FinalTable.LOG_PATH + dt+"_class.csv";
                file = new File(classfile);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        log.error("class csv write error: "+ classfile);
                    }
                }
                write(classfile,data);
                break;
            case "shop":
                String shopfile = FinalTable.LOG_PATH+dt+"_shop.csv";
                file = new File(shopfile);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        log.error("shop csv write error: "+ shopfile);
                    }
                }
                write(shopfile,data);
                break;
            case "gift":
                String giftfile = FinalTable.LOG_PATH+dt+"_gift.csv";
                file = new File(giftfile);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        log.error("gift csv write error: "+ giftfile);
                    }
                }
                write(giftfile,data);
                break;
        }
    }

    private static void write(String path, List<String[]>data){
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(path,true), StandardCharsets.UTF_8.name()), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            writer.writeAll(data);
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            log.error("csv write error"+path);
//            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    log.error("关闭文件输出流出错", e);
                }
            }
        }
    }

    public static List<String[]> readCSV(String fileName) {
        List<String[]> list = null;
        CSVReader reader = null;
        try {
            reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8.name())).build();
            list = reader.readAll();
        } catch (Exception e) {
            log.error("读取CSV数据出错", e);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("关闭文件输入流出错", e);
                }
            }
        }
        return list;
    }

    private int getFileLineNumber(File file) throws IOException {
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        lnr.skip(Long.MAX_VALUE);
        int lineNo = lnr.getLineNumber();
        lnr.close();
        return lineNo;
    }

    public static void main(String[] args) {
        List<String[]> list = new ArrayList<>();
        String[] s1 = {"333","xxxx","tooo"};
        String[] s2 = {"444","aaaa","bbbb"};
        list.add(s1);
        list.add(s2);
        CSVUtil.writeCSV("class",list);
    }
}
