package com.school.sba.requestDTO;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ExelRequestDTO {
private LocalDate fromDate;
private LocalDate toDate;
private String filePath;
}
