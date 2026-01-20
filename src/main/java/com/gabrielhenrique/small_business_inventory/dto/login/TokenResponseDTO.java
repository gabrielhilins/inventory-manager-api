package com.gabrielhenrique.small_business_inventory.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {
    private String token;
    private String email;
    private String type;
    private String role;
}
