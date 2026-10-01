package com.artem.market.domain.partner.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PartnerResponse {
    private Long id;
    private String code;
    private String name;
    private Boolean isGroup;
    private Boolean isDeleted;
    private Long parentId;       // Плоский ID для фронтенда
    private String parentName;   // Дополнительное удобное поле (имя папки)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}