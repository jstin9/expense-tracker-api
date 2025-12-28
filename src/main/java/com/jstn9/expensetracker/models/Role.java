package com.jstn9.expensetracker.models;

import com.jstn9.expensetracker.models.enums.RoleNames;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleNames name;
}
