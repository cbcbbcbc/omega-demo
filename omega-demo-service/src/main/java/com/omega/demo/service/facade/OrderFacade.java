package com.omega.demo.service.facade;

import com.omega.demo.api.GUID;
import com.omega.demo.api.bean.OrderDetail;
import com.omega.demo.api.bean.OrderForm;
import com.omega.demo.api.bean.User;
import com.omega.demo.api.error.CommonError;
import com.omega.demo.api.exception.BizException;
import com.omega.demo.service.domain.OrderEntity;
import com.omega.demo.service.domain.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wuzhengtao on 16/12/6.
 */

@RestController
public class OrderFacade {

    @Autowired
    OrderEntity orderEntity;

    @Autowired
    UserEntity userEntity;

    @RequestMapping("/order/{id}")
    public OrderForm getById(@PathVariable("id") String id) {
        return orderEntity.getOrderById(id);
    }

    @RequestMapping("/orders/{userId}")
    public List<OrderForm> list(@PathVariable("userId") String userId) {
        return orderEntity.getOrderFormListByUserId(userId);
    }

    @RequestMapping(value="/testOrder/{userId}")
    public void testCreate(@PathVariable("userId") String userId) {
        User user = userEntity.getById(userId);
        if (user == null) {
            throw new BizException(CommonError.RECORD_NOT_EXIST);
        }

        OrderForm o = new OrderForm();
        o.setId(generateId(user.getZoneCode()));
        o.setUserId(userId);
        o.setNumber("xxx");
        o.setAmount(new BigDecimal(new Random().nextInt(500)));

        OrderDetail d = new OrderDetail();
        d.setId(generateId(user.getZoneCode()));
        d.setOrderFormId(o.getId());
        d.setItemNo("HK001");
        d.setPrice(new BigDecimal(100));
        d.setQty(2);
        d.setAmount(new BigDecimal(200));

        List<OrderDetail> detailList = new ArrayList<>();
        detailList.add(d);
        o.setDetailList(detailList);

        orderEntity.createOrder(o);
    }

    private String generateId(String zoneCode) {
        return GUID.get() + zoneCode;
    }

}
