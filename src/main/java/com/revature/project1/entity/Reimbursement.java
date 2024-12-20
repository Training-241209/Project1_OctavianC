package com.revature.project1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@Entity
@Table(name = "reimbursement")
public class Reimbursement {

    @Column(name = "reimbId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reimbId;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Reimbursement() {

    }

    public Reimbursement(String description, Double amount, String status, User user) {
        this.status = status;
        this.description = description;
        this.amount = amount;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "reimbId=" + reimbId +
                ", description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
