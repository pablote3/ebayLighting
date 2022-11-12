package com.rossotti.lighting.sale.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class CsvUtil {
    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);

    public static <T> List<T> loadObjectList(Class<T> type, String fileName) {
        try {
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.TRIM_SPACES);
            mapper.enable(CsvParser.Feature.INSERT_NULLS_FOR_MISSING_COLUMNS);

            File file = new ClassPathResource(fileName).getFile();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            schema = schema.withColumnSeparator(',');

            MappingIterator<T> readValues = mapper.readerFor(type).with(schema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            logger.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }

}
