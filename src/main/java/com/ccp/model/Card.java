package com.ccp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits.")
    @Column(name = "card_number")
    private String cardNumber;

    @NotEmpty(message = "Card type is required.")
    @Pattern(regexp = "Visa|Master Card|Amex", message = "Card type must be either Visa, Master Card, or Amex.")
    private String cardType;

    @NotNull
    @Future(message = "Expiration date must be in the future.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;

    @NotEmpty(message = "Name on the card is required.")
    @Size(min = 2, max = 20, message = "Name length must be between 2 and 20 characters.")
    private String nameOnCard;

    @NotEmpty(message = "CVV is required.")
    @Pattern(regexp = "\\d{3}", message = "CVV number must be exactly 3 digits.")
    private String cvv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Customer customer;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Payment> payments;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Card card)) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        return getCardNumber() != null && Objects.equals(getCardNumber(), card.getCardNumber());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy
                        .getHibernateLazyInitializer()
                        .getPersistentClass()
                        .hashCode()
                : getClass().hashCode();
    }
}
