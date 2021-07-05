package com.dongho.dev.domain.payment;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private BigDecimal mainMethodAmount;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime approvedAt;

    private long orderId = 0;
    private String mainPaymentMethodType = "";
    private String paymentActionType = "";
    private int status = 0;
    private BigDecimal subMethodAmount = BigDecimal.ZERO;
    private int pointAmount = 0;
    private String subPaymentMethodType = "";

    public Payment(long amount) {
        mainMethodAmount = BigDecimal.valueOf(amount);

        LocalDateTime now = LocalDateTime.now();
        createdAt = updatedAt = approvedAt = now;
    }


}
