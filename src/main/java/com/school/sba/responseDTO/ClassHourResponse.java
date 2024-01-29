package com.school.sba.responseDTO;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.school.sba.Enum.ClassStatus;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClassHourResponse {
private int classHourId;
private LocalDateTime beginsAt;
private LocalDateTime endsAt;
private String roomNo;
private ClassStatus classStatus;
private UserResponce teacher;
private String subject;
}
