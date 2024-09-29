package uz.infinity.zero_one_demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.infinity.zero_one_demo.helper.Message;
import uz.infinity.zero_one_demo.helper.ResponseHelper;

import java.util.ArrayList;

@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ResponseHelper responseHelper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(final Exception e,
                                                    final WebRequest webRequest,
                                                    final HttpServletRequest request) {

        logger.error(e.getMessage());
        return responseHelper.prepareResponse(
                new ArrayList<>(),
                new Message(
                        "Server ichki xatoligi. Administrator bilan bog'laning!",
                        "Внутренняя ошибка сервера. Свяжитесь с администратором!",
                        "Internal server error. Contact the administrator!"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
