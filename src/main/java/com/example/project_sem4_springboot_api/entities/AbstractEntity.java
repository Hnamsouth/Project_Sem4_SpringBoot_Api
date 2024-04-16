package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Abstract entity.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class AbstractEntity implements Serializable {
    /**
     * The Created date.
     */
    @CreatedDate
    protected LocalDateTime createdDate;
    /**
     * The Created by.
     */
    @CreatedBy
    protected String createdBy;
    /**
     * The Modified date.
     */
    @LastModifiedDate
    protected LocalDateTime modifiedDate;
    /**
     * The Modified by.
     */
    @LastModifiedBy
    protected String modifiedBy;
    /**
     * The Is deleted.
     */
    @Column(columnDefinition = "boolean default false")
    protected Boolean isDeleted = false;
}