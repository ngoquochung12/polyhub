package com.polyhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Categories")
public class Category implements Serializable {

    @Id
    @Column(length = 20)
    private String id; // Mã ngành (VD: UDPM, TKDH)

    @Column(columnDefinition = "nvarchar(100)", nullable = false)
    private String name; // Tên ngành đào tạo

    private Boolean active = true; // Trạng thái: true (Hoạt động), false (Đã khóa)
}
