package com.jstn9.expensetracker.service;

import com.jstn9.expensetracker.dto.category.CategoryCreateRequest;
import com.jstn9.expensetracker.dto.category.CategoryResponse;
import com.jstn9.expensetracker.dto.category.CategoryUpdateRequest;
import com.jstn9.expensetracker.exception.CategoryAlreadyExistsException;
import com.jstn9.expensetracker.exception.CategoryIsUsedInTransactionException;
import com.jstn9.expensetracker.exception.CategoryNotFoundException;
import com.jstn9.expensetracker.models.Category;
import com.jstn9.expensetracker.models.User;
import com.jstn9.expensetracker.repository.CategoryRepository;
import com.jstn9.expensetracker.repository.TransactionRepository;
import com.jstn9.expensetracker.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public CategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    public List<CategoryResponse> getAllForCurrentUser(){
        User user = userService.getCurrentUser();

        return categoryRepository.findByUserOrderByName(user).stream()
                .map(MapperUtil::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getByIdForCurrentUser(Long id){
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        return MapperUtil.toCategoryResponse(category);
    }

    public CategoryResponse createCategory(CategoryCreateRequest request){
        User user = userService.getCurrentUser();

        if(categoryRepository.existsByNameAndUser(request.getName(), user)){
            throw new CategoryAlreadyExistsException("Category already exists!");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setUser(user);
        Category savedCategory = categoryRepository.save(category);

        return MapperUtil.toCategoryResponse(savedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        category.setName(request.getName());
        Category savedCategory = categoryRepository.save(category);

        return MapperUtil.toCategoryResponse(savedCategory);
    }

    public void deleteCategory(Long id) {
        User user = userService.getCurrentUser();

        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found!"));

        boolean isUsed = transactionRepository.existsByCategory(category);
        if(isUsed){
            throw new CategoryIsUsedInTransactionException("Category is used in transactions and cannot be deleted!");
        }

        categoryRepository.delete(category);
    }


}
