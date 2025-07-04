package com.example.demo;

import com.example.demo.controller.UserController;
import com.example.demo.dto.TransferRequest;
import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionRecord;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckBalance() {
        when(userService.checkBalance("rinesh")).thenReturn(250.0);

        double result = userController.checkBalance("rinesh");

        assertEquals(250.0, result, 0.01);
        verify(userService).checkBalance("rinesh");
    }

    @Test
    void testRedeemCode() {
        when(userService.redeemCode("rinesh", "CODE123")).thenReturn("Code redeemed successfully. ₹100.0 added.");

        String result = userController.redeemCode("rinesh", "CODE123");

        assertEquals("Code redeemed successfully. ₹100.0 added.", result);
        verify(userService).redeemCode("rinesh", "CODE123");
    }

    @Test
    void testGetUserTransactions() {
        Transaction t1 = new Transaction("rinesh", "debit", 50.0, new Timestamp(System.currentTimeMillis()));
        Transaction t2 = new Transaction("rinesh", "credit", 100.0, new Timestamp(System.currentTimeMillis()));
        List<Transaction> mockList = Arrays.asList(t1, t2);

        when(userService.getUserTransactions("rinesh")).thenReturn(mockList);

        List<Transaction> result = userController.getUserTransactions("rinesh");

        assertEquals(2, result.size());
        assertEquals("debit", result.get(0).getAction());
        verify(userService).getUserTransactions("rinesh");
    }

    @Test
    void testTransferFundsSuccess() {
        TransferRequest request = new TransferRequest("alice", "bob", 50.0);

        when(userService.transferFunds("alice", "bob", 50.0)).thenReturn("Transfer successful");

        ResponseEntity<String> response = userController.transferFunds(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transfer successful", response.getBody());
        verify(userService).transferFunds("alice", "bob", 50.0);
    }

    @Test
    void testTransferFundsFailure() {
        TransferRequest request = new TransferRequest("alice", "bob", 5000.0);

        when(userService.transferFunds("alice", "bob", 5000.0)).thenReturn("Insufficient balance");

        ResponseEntity<String> response = userController.transferFunds(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Insufficient balance", response.getBody());
        verify(userService).transferFunds("alice", "bob", 5000.0);
    }

    @Test
    void testGetTransactionHistory() {
        TransactionRecord tr1 = new TransactionRecord("alice", "debit", 100.0, new Timestamp(System.currentTimeMillis()));
        TransactionRecord tr2 = new TransactionRecord("bob", "credit", 50.0, new Timestamp(System.currentTimeMillis()));
        List<TransactionRecord> mockHistory = Arrays.asList(tr1, tr2);

        when(userService.getTransactionHistory("admin", true)).thenReturn(mockHistory);

        ResponseEntity<List<TransactionRecord>> response = userController.getTransactionHistory("admin", true);

        assertEquals(2, response.getBody().size());
        assertEquals("debit", response.getBody().get(0).getAction());
        verify(userService).getTransactionHistory("admin", true);
    }
}
