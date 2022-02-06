package com.api.userparcelservice.dto;

import com.api.userparcelservice.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MqDTO {

    private String token;

    private UserEntity userEntity;
}
