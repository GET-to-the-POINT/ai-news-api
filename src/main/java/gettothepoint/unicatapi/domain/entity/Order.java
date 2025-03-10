package gettothepoint.unicatapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import gettothepoint.unicatapi.domain.constant.payment.PayType;
import gettothepoint.unicatapi.domain.constant.payment.TossPaymentStatus;

import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "purchase")
public class Order {
    @Id
    @Column(updatable = false, nullable = false)
    private final String Id = UUID.randomUUID().toString();

    @Column
    private String orderName;

    private Long amount;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnore
    private Member member;

    @Enumerated(EnumType.STRING)
    private PayType payMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private TossPaymentStatus status;

    @OneToOne
    private Subscription subscription; // 주문이 특정 구독에 연결

    @OneToMany(mappedBy = "order")
    private List<Payment> payments;

    @Builder
    public Order(String orderName, Long amount, Member member, PayType payMethod, TossPaymentStatus status, Subscription subscription) {
        this.orderName = orderName;
        this.amount = amount;
        this.member = member;
        this.payMethod = payMethod;
        this.status = status;
        this.subscription = subscription;
    }
}
