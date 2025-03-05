package taeniverse.unicatApi.mvc.model.dto;

import lombok.Getter;
import lombok.Setter;
import taeniverse.unicatApi.payment.PayType;

@Getter
@Setter
public class OrderRequest {
    // 구독 플랜 이름, 일반 프로
    private String orderName;

    // 결제 금액
    private Long amount;

    // 결제 상태
    private String status;


    private String subscriptionId;
    private PayType payMethod;
}
