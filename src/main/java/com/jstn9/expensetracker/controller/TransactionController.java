package com.jstn9.expensetracker.controller;

import com.jstn9.expensetracker.dto.transaction.TransactionRequest;
import com.jstn9.expensetracker.dto.transaction.TransactionResponse;
import com.jstn9.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping()
    public ResponseEntity<List<TransactionResponse>> getTransactions(){
        return ResponseEntity.ok(transactionService.getTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping()
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request){
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request){
        return ResponseEntity.ok(transactionService.updateTransaction(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionResponse> deleteTransaction(@PathVariable Long id){
        transactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
