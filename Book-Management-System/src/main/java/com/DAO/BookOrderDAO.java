package com.DAO;

import com.entity.BookOrder;

import java.util.List;

public interface BookOrderDAO {

    public boolean saveOrder(List<BookOrder> b);

    public List<BookOrder> getBook(String email);

    public List<BookOrder> getAllOrders();

}
