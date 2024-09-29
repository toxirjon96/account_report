package uz.infinity.zero_one_demo.util;

import org.springframework.stereotype.Component;
import uz.infinity.zero_one_demo.exception.GlobalExceptionHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeConvertor {
    public LocalDate convertStringToDate(String date, String format) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
            GlobalExceptionHandler.logger.warn(e.getMessage());
            return null;
        }
    }
    public String convertDateToString(LocalDate date, String format) {
        try {
            return date.format(DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
            GlobalExceptionHandler.logger.warn(e.getMessage());
            return null;
        }
    }
}
