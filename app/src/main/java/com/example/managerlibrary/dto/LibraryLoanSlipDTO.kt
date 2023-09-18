package com.example.managerlibrary.dto

data class LibraryLoanSlipDTO(
    val id: Int,
    val idBook: Int,
    val idLibrarian: String,
    val idReader: Int,
    val dateLoan: String,
    val datePay: String,
    val status: Int
)