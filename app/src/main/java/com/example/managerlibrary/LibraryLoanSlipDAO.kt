package com.example.managerlibrary

import android.content.Context
import android.util.Log
import com.example.managerlibrary.database.ManagerBookDataBase

class LibraryLoanSlipDAO(context: Context) {
    private val db: ManagerBookDataBase = ManagerBookDataBase(context)

    //get all loan slip return list of loan slip
    fun getAllLoanSlip(): ArrayList<LibraryLoanSlipDTO> {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM LibraryLoanSlip"
        val cursor = dbReadable.rawQuery(sql, null)
        val list = ArrayList<LibraryLoanSlipDTO>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val idLoanSlip = cursor.getInt(0)
                val idLibrarian = cursor.getInt(1)
                val idMember = cursor.getInt(2)
                val idBook = cursor.getInt(3)
                val dateLoan = cursor.getString(4)
                val status = cursor.getInt(5)
                val libraryLoanSlipDTO = LibraryLoanSlipDTO(
                    idLoanSlip,
                    idBook,
                    idLibrarian,
                    idMember,
                    dateLoan,
                    status
                )
                list.add(libraryLoanSlipDTO)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return list
    }

    //delete loan slip by id
    fun deleteLoanSlip(id: Int): Boolean {
        val dbWritable = db.writableDatabase
        val sql = "DELETE FROM LibraryLoanSlip WHERE loanSlipID = ?"
        val cursor = dbWritable.rawQuery(sql, arrayOf(id.toString()))
        if (cursor.count > 0) {
            cursor.close()
            dbWritable.close()
            return true
        }
        cursor.close()
        dbWritable.close()
        return false
    }

    //get top 10 book from loan
    fun getTop10Book(): ArrayList<BookDTO> {
        val dbReadable = db.readableDatabase
        val sql =
            "SELECT LibraryLoanSlip.bookID," +
                    " COUNT(LibraryLoanSlip.bookID) AS count, " +
                    "Book.bookName," +
                    " Book.rentalFee FROM LibraryLoanSlip " +
                    "INNER JOIN Book ON LibraryLoanSlip.bookID = Book.bookID" +
                    " GROUP BY LibraryLoanSlip.bookID " +
                    "ORDER BY count DESC LIMIT 10"
        val cursor = dbReadable.rawQuery(sql, null)
        val list = ArrayList<BookDTO>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val idBook = cursor.getInt(0)
                val count = cursor.getInt(1)
                val name = cursor.getString(2)
                val rentalFee = cursor.getInt(3)
                val bookDTO = BookDTO(idBook, name, rentalFee, "", count )
                list.add(bookDTO)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return list
    }

    //thống kê doanh thu toàn bộ hóa đơn
    fun getRevenue(): Int {
        val dbReadable = db.readableDatabase
        val sql = "SELECT SUM(Book.rentalFee) FROM LibraryLoanSlip " +
                "INNER JOIN Book ON LibraryLoanSlip.bookID = Book.bookID"
        val cursor = dbReadable.rawQuery(sql, null)
        var revenue = 0
        if (cursor.count > 0) {
            cursor.moveToFirst()
            revenue = cursor.getInt(0)
        }
        cursor.close()
        dbReadable.close()
        return revenue
    }

    //thông kê doanh thu theo ngày: ngày bắt đầu và ngày kết thúc
    fun getRevenueByDate(startDate: String, endDate: String): Int {
        Log.d("getRevenueByDate", "Checking revenue from $startDate to $endDate")
        val dbReadable = db.readableDatabase
        val sql = "SELECT SUM(Book.rentalFee) FROM LibraryLoanSlip " +
                "INNER JOIN Book ON LibraryLoanSlip.bookID = Book.bookID " +
                "WHERE LibraryLoanSlip.loanDate BETWEEN ? AND ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(startDate, endDate))
        var revenue = 0
        if (cursor.count > 0) {
            cursor.moveToFirst()
            revenue = cursor.getInt(0)
        }
        cursor.close()
        dbReadable.close()

        Log.d("getRevenueByDate", "Revenue from $startDate to $endDate is: $revenue")
        return revenue
    }
}