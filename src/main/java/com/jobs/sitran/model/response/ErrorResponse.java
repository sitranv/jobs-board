package com.jobs.sitran.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Date timestamp;

    private String message;

    private String detail;

    private Integer status;

    Boolean success = false;
}
