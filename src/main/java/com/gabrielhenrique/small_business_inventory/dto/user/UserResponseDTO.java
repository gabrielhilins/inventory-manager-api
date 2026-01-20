package com.gabrielhenrique.small_business_inventory.dto.user;

import com.gabrielhenrique.small_business_inventory.model.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
