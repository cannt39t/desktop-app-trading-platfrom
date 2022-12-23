package com.example.services;

import com.example.dao.LimitOrderDao;
import com.example.dao.PositionDao;
import com.example.dao.QuotationDao;
import com.example.dao.UserDao;
import com.example.dao.impl.LimitOrderDaoImpl;
import com.example.dao.impl.PositionDaoImpl;
import com.example.dao.impl.QuotationDaoImpl;
import com.example.dao.impl.UserDaoImpl;
import com.example.models.LimitOrder;
import com.example.models.Position;
import com.example.models.User;
import org.w3c.dom.ls.LSOutput;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    LimitOrderDao limitOrderDao = new LimitOrderDaoImpl();
    PositionDao positionDao = new PositionDaoImpl();

    UserDao userDao = new UserDaoImpl();

    QuotationDao quotationDao = new QuotationDaoImpl();

    public void openPosition(User user, String side, int volume) throws SQLException {
        List<LimitOrder> limitOrders = side.equals("Sell") ? limitOrderDao.getAllBuy() : limitOrderDao.getAllSell();
        int sumVolume = 0;
        List<LimitOrder> shouldChangeLimitOrders = new ArrayList<>();

        int sumPrice = 0;

        for (int i = 0; i < limitOrders.size() && sumVolume < volume; i++) {
            sumVolume += limitOrders.get(i).getVolume();
            sumPrice += limitOrders.get(i).getVolume() * limitOrders.get(i).getAtPrice();
            shouldChangeLimitOrders.add(limitOrders.get(i));
        }

        if (sumPrice > user.getBalance()) {
            return;
        }
        

        System.out.println();

        System.out.println(volume);

        System.out.println(sumVolume);

        shouldChangeLimitOrders.forEach(System.out::println);

        for (int i = 0; i < shouldChangeLimitOrders.size(); i++) {
            LimitOrder limit = shouldChangeLimitOrders.get(i);
            if(i == shouldChangeLimitOrders.size()-1) {
                if (volume == sumVolume) {
                    changeLimitForPosition(limit);
                    userDao.updateBalance(user.getId(), user.getBalance() - limit.getVolume() * limit.getAtPrice());
                    userDao.updateBalance(limit.getUserId(), userDao.get(limit.getUserId()).getBalance() + limit.getVolume() * limit.getAtPrice());
                    quotationDao.changePrice(1, limit.getAtPrice());
                } else {
                    int dif = sumVolume - volume;
                    int was = limit.getVolume();
                    int pos = was - dif;
                    System.out.println(dif);
                    System.out.println(was);
                    System.out.println(pos);
                    Position position = new Position(1, limit.getSide(), pos, limit.getAtPrice(), limit.getUserId(), limit.getQuotationId(), false);
                    positionDao.add(position);
                    limitOrderDao.updateVolume(limit.getId(), dif);
                    userDao.updateBalance(user.getId(), user.getBalance() - pos * limit.getAtPrice());
                    userDao.updateBalance(limit.getUserId(), userDao.get(limit.getUserId()).getBalance() + pos * limit.getAtPrice());
                    quotationDao.changePrice(1, limit.getAtPrice());
                }
            } else {
                changeLimitForPosition(limit);
                userDao.updateBalance(user.getId(), user.getBalance() - limit.getVolume() * limit.getAtPrice());
                quotationDao.changePrice(1, limit.getAtPrice());
                userDao.updateBalance(limit.getUserId(), userDao.get(limit.getUserId()).getBalance() + limit.getVolume() * limit.getAtPrice());
            }
        }
    }

    private void changeLimitForPosition(LimitOrder limit) throws SQLException {
        Position position = new Position(1, limit.getSide(), limit.getVolume(), limit.getAtPrice(), limit.getUserId(), limit.getQuotationId(), false);
        positionDao.add(position);
        limitOrderDao.delete(limit.getId());
    }

    public static void main(String[] args) {
        User user = new User(1, "Ilya", 10000000);


        UserService userService = new UserService();

        try {
            userService.openPosition(user, "Sell", 200);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
