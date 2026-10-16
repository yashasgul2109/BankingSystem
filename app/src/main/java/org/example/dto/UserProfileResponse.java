
package org.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
}