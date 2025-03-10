package gettothepoint.unicatapi.application.service.payment;

import gettothepoint.unicatapi.domain.constant.payment.SubscriptionStatus;
import gettothepoint.unicatapi.domain.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import gettothepoint.unicatapi.domain.entity.Member;
import gettothepoint.unicatapi.domain.entity.Order;
import gettothepoint.unicatapi.domain.entity.Subscription;
import gettothepoint.unicatapi.domain.repository.SubscriptionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public void createSubscription(Member member, Order order , Payment payment) {
        Subscription subscription = Subscription.builder()
                .member(member)
                .order(order)
                .payment(payment)
                .build();
        subscriptionRepository.save(subscription);
    }
    @Transactional
    public void cancelSubscriptionByPayment(Payment payment) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByPayment(payment);
        if (subscriptionOpt.isPresent()) {
            Subscription subscription = subscriptionOpt.get();
            subscription.setStatus(SubscriptionStatus.CANCELLED);
            subscriptionRepository.save(subscription);
        }
    }
}
