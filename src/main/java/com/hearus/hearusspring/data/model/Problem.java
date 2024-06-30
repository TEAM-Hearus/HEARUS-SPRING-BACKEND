package com.hearus.hearusspring.data.model;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {
    private String type;

    private String direction;

    private List<String> options;

    private String answer;
}