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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

        log.info("Fetching categories for user: {}", user.getUsername());
        List<Category> categories = categoryRepository.findByUserOrderByName(user);

        log.info("Categories fetched successfully for user: {}", user.getUsername());
        return categories.stream()
                .map(MapperUtil::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getByIdForCurrentUser(Long id){
        User user = userService.getCurrentUser();
        log.info("Start fetching category by id: {} for user: {}",
                id, user.getUsername());

        Category category = findCategoryOrThrowException(id, user);

        log.info("Category fetched successfully by id: {} for user: {}",
                category.getId(), user.getUsername());
        return MapperUtil.toCategoryResponse(category);
    }

    public CategoryResponse createCategory(CategoryCreateRequest request){
        User user = userService.getCurrentUser();
        log.info("Start creating a new category for user: {}", user.getUsername());

        if(categoryRepository.existsByNameAndUser(request.getName(), user)){
            log.warn("Category already exists with name: {} for user: {}",
                    request.getName(), user.getUsername());
            throw new CategoryAlreadyExistsException();
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setUser(user);
        Category savedCategory = categoryRepository.save(category);

        log.info("Category created successfully with id: {} for user: {}",
                savedCategory.getId(), user.getUsername());
        return MapperUtil.toCategoryResponse(savedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        User user = userService.getCurrentUser();
        log.info("Start updating category with id: {} for user: {}", id, user.getUsername());

        Category category = findCategoryOrThrowException(id, user);

        category.setName(request.getName());
        Category savedCategory = categoryRepository.save(category);

        log.info("Category updated successfully with id: {} for user: {}",
                savedCategory.getId(), user.getUsername());
        return MapperUtil.toCategoryResponse(savedCategory);
    }

    public void deleteCategory(Long id) {
        User user = userService.getCurrentUser();
        log.info("Start deleting category with id: {} for user: {}", id, user.getUsername());

        Category category = findCategoryOrThrowException(id, user);

        boolean isUsed = transactionRepository.existsByCategory(category);
        if(isUsed){
            log.warn("Cannot delete category id {} because it is used in transactions for user {}",
                    id, user.getUsername());
            throw new CategoryIsUsedInTransactionException();
        }

        categoryRepository.delete(category);
        log.info("Category deleted successfully with id: {} for user: {}", id, user.getUsername());
    }

    private Category findCategoryOrThrowException(Long id, User user){
        log.debug("Finding category with id: {} for user: {}", id, user.getUsername());
        return categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> {
                    log.warn("Category not found with id: {} for user: {}", id, user.getUsername());
                    return new CategoryNotFoundException();
                });
    }
}
