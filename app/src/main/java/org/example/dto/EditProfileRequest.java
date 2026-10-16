package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileRequest {
    private String name;
    private String email;
}
