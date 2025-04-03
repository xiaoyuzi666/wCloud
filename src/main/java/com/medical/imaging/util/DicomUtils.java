package com.medical.imaging.util;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DicomUtils {
    
    public static Attributes readDicomAttributes(InputStream inputStream) throws IOException {
        try (DicomInputStream dis = new DicomInputStream(inputStream)) {
            return dis.readDataset();
        }
    }
    
    public static LocalDateTime parseDateTime(String dateStr, String timeStr) {
        if (dateStr == null || timeStr == null) {
            return null;
        }
        
        String dateTimeStr = dateStr + timeStr;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
    
    public static String getStringValue(Attributes attrs, int tag) {
        return attrs.getString(tag, "");
    }
    
    public static LocalDateTime getStudyDateTime(Attributes attrs) {
        String date = attrs.getString(Tag.StudyDate);
        String time = attrs.getString(Tag.StudyTime);
        return parseDateTime(date, time);
    }
} 