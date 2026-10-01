package com.example.zwi.ingestion;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface DataImporter<T> {
    List<T> importData(InputStream inputStream) throws IOException;
}
