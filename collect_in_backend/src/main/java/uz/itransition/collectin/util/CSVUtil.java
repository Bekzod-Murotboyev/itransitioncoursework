package uz.itransition.collectin.util;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uz.itransition.collectin.entity.collection.Collection;
import uz.itransition.collectin.entity.collection.Field;
import uz.itransition.collectin.entity.collection.FieldValue;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public final class CSVUtil extends FileUtil {

    private static final Logger log = LoggerFactory.getLogger(CSVUtil.class);

    //TODO - must fix PATH when deploying
    private static final String PATH = "../";

    /**
     * @ofCollection() - writes a file of Collection with it items as CSV format and returns absolute path in string;
     **/
    public static String ofCollection(String lang, Collection collection, List<Field> fields, List<FieldValue> filedValues) {
        final String FILE_PATH = PATH + new Date() + ".csv";
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH));
            List<String[]> data = new ArrayList<>();
            data.addAll(collection.toCSVString(lang));
            data.addAll(List.of(new String[0], new String[]{lang.equals("ENG") ? "Items" : "Элемент"}, headCollectionItemFields(fields, lang)));
            data.addAll(valuesCollectionItemFields(filedValues, fields.size()));
            writer.writeAll(data);
            writer.close();
            return FILE_PATH;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    ;

    public static String[] headCollectionItemFields(List<Field> fields, String lang) {
        String[] head = new String[fields.size() + 2];
        for (int i = 0; i < fields.size(); i++) {
            head[i] = fields.get(i).getName();
        }
        head[head.length - 2] = lang.equals("ENG") ? "likes" : "лайк";
        head[head.length - 1] = lang.equals("ENG") ? "creation date" : "дата создания";
        return head;
    }

    public static List<String[]> valuesCollectionItemFields(List<FieldValue> fieldValues, int fieldCount) {
        final List<String[]> fieldData = new ArrayList<>();
        String[] data = new String[fieldCount + 2];
        int index = 0;
        int i = 0;
        while (i < fieldValues.size()) {
            data[index++] = fieldValues.get(i++).getValue();
            if ((index & fieldCount) == fieldCount) {
                data[index] = String.valueOf(fieldValues.get(i - 1).getItem().getLikes());
                data[index + 1] = String.valueOf(fieldValues.get(i - 1).getItem().getCreationDate());
                fieldData.add(data);
                data = new String[fieldCount + 2];
                index = 0;
            }
        }
        return fieldData;
    }
}
