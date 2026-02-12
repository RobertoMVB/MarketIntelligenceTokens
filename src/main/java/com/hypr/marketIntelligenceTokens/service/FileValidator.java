package com.hypr.marketIntelligenceTokens.service;

import java.nio.file.Path;

public class FileValidator {
    public static FileType detectFileType(Path path) {
        if (path == null || !path.toFile().exists()) {
            return FileType.INVALID;
        }

        String fileName = path.getFileName().toString().toLowerCase();

        if (fileName.endsWith(".csv")) {
            return FileType.CSV;
        } else if (fileName.endsWith(".json")) {
            return FileType.JSON;
        } else {
            return FileType.INVALID;
        }
    }
}
