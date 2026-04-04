package com.polyhub.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Tags")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // Ví dụ: Java, JavaScript (Không cần lưu dấu #)

    @Column(columnDefinition = "int default 0")
    private Integer usageCount = 0; // Lượt sử dụng trong bài viết

    @Column(columnDefinition = "boolean default true")
    private Boolean isSystem = true; // true: System tạo, false: Sinh viên/Người dùng tạo

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt = new Date();
}
