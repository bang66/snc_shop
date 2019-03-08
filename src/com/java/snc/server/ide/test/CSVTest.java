package snc.server.ide.test;

import com.opencsv.CSVWriter;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVTest {
    @Test
    public void testWriteByStringArray() throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String dat = simpleDateFormat.format(date);
        System.out.println(dat);
//        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("/home/jac/boss/a.csv"), Charset.forName("UTF-8"));
//        CSVWriter csvWriter = new CSVWriter(out, ',');
//
//        String[] record0 = {"id", "name", "age", "birthday"};
//        csvWriter.writeNext(record0);
//
//        String[] record1 = {"1", "张三", "20", "1990-08-08"};
//        String[] record2 = {"2", "lisi", "21", "1991-08-08"};
//        String[] record3 = {"3", "wangwu", "22", "1992-08-08"};
//        List<String[]> allLines = new ArrayList<String[]>();
//        allLines.add(record1);
//        allLines.add(record2);
//        allLines.add(record3);
//        csvWriter.writeAll(allLines);
//        csvWriter.close();
    }
}
