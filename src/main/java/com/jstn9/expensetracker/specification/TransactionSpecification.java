package com.jstn9.expensetracker.specification;

import com.jstn9.expensetracker.dto.transaction.TransactionFilter;
import com.jstn9.expensetracker.models.Transaction;
import com.jstn9.expensetracker.models.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {
    public static Specification<Transaction> filter(TransactionFilter filter, User user){
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("user"), user));

            if(filter.getType() != null){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("type"), filter.getType()));
            }

            if(filter.getCategoryId() != null){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            if(filter.getMinAmount() != null){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));
            }

            if(filter.getMaxAmount() != null){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));
            }

            if(filter.getStartDate() != null){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));
            }

            if(filter.getEndDate() != null){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
            }

            return predicate;
        };
    }
}
