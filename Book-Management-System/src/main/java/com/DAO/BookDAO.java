package com.DAO;

import com.entity.BookDetails;

import java.awt.print.Book;
import java.util.List;

public interface BookDAO {
    public boolean addBooks(BookDetails b);

    public List<BookDetails> getAllBooks();

    public BookDetails getBookById(int id);

    public boolean updateEditBooks(BookDetails b);

    public boolean deleteBooks(int id);

    public List<BookDetails> getNewBooks();

    public List<BookDetails> getRecentBooks();

    public List<BookDetails> getOldBooks();

    public List<BookDetails> getAllRecentBooks();

    public List<BookDetails> getAllNewBooks();

    public List<BookDetails> getAllOldBooks();

    public List<BookDetails> getBookByOld(String email, String category);

    public boolean oldBookDelete(String email, String category, int id);

    public List<BookDetails> getBookBySearch(String ch);


}
