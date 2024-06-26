package com.hearus.hearusspring.data.entitiy;

import com.hearus.hearusspring.data.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "user")
public class UserEntity extends BaseEntitiy{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    @Column(unique=true)
    String email;

    String password;

    String role;

    // OAuth인지의 여부 판단
    boolean isOAuth;

    // Oauth 종류
    // KAKAO, GOOGLE, NAVER
    String oauthType;

    String school;
    String major;

    // 재학생 (1, 2, 3, 4), 휴학생, 졸업생
    String grade;

    // DB에는 일반적인 String으로 저장하고
    // ','를 기준으로 ArrayList로 변환
    String savedLectures;

    String schedule;
    String usePurpose;

    public UserDTO toDTO(){
        // ','를 기준으로 split
        ArrayList<String> savedLecturesList = new ArrayList<>();
        if(savedLectures != null)
            savedLecturesList = Arrays.stream(
                    savedLectures.split(","))
                    .map(String::trim)
                    .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> savedScheduleList = new ArrayList<>();
        if(schedule != null)
            savedScheduleList = Arrays.stream(
                            schedule.split(","))
                    .map(String::trim)
                    .collect(Collectors.toCollection(ArrayList::new));

        return UserDTO.builder()
                .userId(id)
                .userName(name)
                .userEmail(email)
                .userPassword(password)
                .userRole(role)
                .userIsOAuth(isOAuth)
                .userOAuthType(oauthType)
                .userSchool(school)
                .userMajor(major)
                .userGrade(grade)
                .userSavedLectures(savedLecturesList)
                .userSchedule(savedScheduleList)
                .userUsePurpose(usePurpose)
                .build();
    }
}
