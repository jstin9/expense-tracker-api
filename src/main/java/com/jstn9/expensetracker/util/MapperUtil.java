package com.jstn9.expensetracker.util;

import com.jstn9.expensetracker.dto.auth.UserResponse;
import com.jstn9.expensetracker.dto.category.CategoryResponse;
import com.jstn9.expensetracker.dto.profile.ProfileResponse;
import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.models.Category;
import com.jstn9.expensetracker.models.Profile;
import com.jstn9.expensetracker.models.Transaction;
import com.jstn9.expensetracker.models.User;

public final class MapperUtil {

    private MapperUtil() {}

    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }

    public static ProfileResponse toProfileResponse(Profile profile){
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setId(profile.getId());
        profileResponse.setName(profile.getName());
        profileResponse.setBalance(profile.getBalance());
        profileResponse.setMonthSalary(profile.getMonthSalary());
        profileResponse.setCurrencyType(profile.getCurrencyType());
        profileResponse.setCreatedAt(profile.getCreatedAt());
        profileResponse.setUpdatedAt(profile.getUpdatedAt());
        return profileResponse;
    }

    public static CategoryResponse toCategoryResponse(Category category){
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(category.getId());
        categoryResponse.setName(category.getName());
        return categoryResponse;
    }

    public static TransactionResponse toTransactionResponse(Transaction transaction) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setType(transaction.getType());
        transactionResponse.setDescription(transaction.getDescription());
        transactionResponse.setDate(transaction.getDate());
        transactionResponse.setCategory(toCategoryResponse(transaction.getCategory()));
        return transactionResponse;
    }
}
