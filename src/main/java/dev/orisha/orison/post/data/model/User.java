package dev.orisha.orison.post.data.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("USERS")
public class User {

    @Id
    private Long id;

    private String name;

    private String username;

    @NotBlank
    private String email;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY, prefix = "address_")
    private Address address;

    private String phone;

    private String website;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY, prefix = "company_")
    private Company company;

    @Version
    private Long version;
}
