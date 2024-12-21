package com.ccp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Pattern(
            regexp = "^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$",
            message = "Please enter a valid customerId.")
    @Column(name = "customer_id")
    private String customerId;

    @NotEmpty(message = "Name can't be null.")
    @Size(min = 2, max = 20, message = "Name length must be between 2 and 20 characters.")
    private String name;

    @NotEmpty(message = "Age can't be null.")
    @Pattern(regexp = "\\d{1,3}", message = "Age must be a numeric value.")
    private String age;

    @NotEmpty(message = "Email can't be null.")
    @Pattern(regexp = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", message = "Please enter a valid email.")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Phone number can't be null.")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must contain exactly 10 digits.")
    @Column(unique = true)
    private String phoneNo;

    @NotEmpty(message = "Address can't be null.")
    @Size(min = 5, max = 100, message = "Address length must be between 5 and 100 characters.")
    private String address;

    @NotEmpty(message = "Password can't be null.")
    @Size(min = 4, message = "Password length must be at least 4 characters.")
    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Card> cards;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Payment> payments;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Customer customer)) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        return getCustomerId() != null && Objects.equals(getCustomerId(), customer.getCustomerId());
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
