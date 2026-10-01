package com.example.zwi.ingestion.rcn;

import com.example.zwi.ingestion.DataImporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RcnCsvImporter implements DataImporter<RcnRawRecord> {
    @Override
    public List<RcnRawRecord> importData(InputStream inputStream) throws IOException {
        List<RcnRawRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header == null) {
                return records;
            }
            String delimiter = header.contains(";") ? ";" : ",";
            String line;
            while ((line = reader.readLine()) != null) {
                String[] v = line.split(delimiter, -1);
                if (v.length < 10) {
                    continue;
                }
                records.add(new RcnRawRecord(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8], v[9], line));
            }
        }
        return records;
    }
}
