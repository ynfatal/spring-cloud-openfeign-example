package com.example.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Fatal
 * @date 2020/6/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomRequest {

    private String string;

    private Integer integer;

    private Long longValue;

    private Double doubleValue;

}
