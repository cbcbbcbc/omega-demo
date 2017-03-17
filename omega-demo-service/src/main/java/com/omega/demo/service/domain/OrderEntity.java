package com.omega.demo.service.domain;

import com.omega.demo.api.bean.OrderDetail;
import com.omega.demo.api.bean.OrderForm;
import com.omega.demo.api.bean.OrderListModel;
import com.omega.demo.service.dao.OrderDao;
import com.omega.framework.index.IndexCommandService;
import com.omega.framework.index.IndexWorker;
import com.omega.framework.index.bean.IndexCommand;
import com.omega.framework.util.CacheClient;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackychenb on 25/12/2016.
 */

@Service
public class OrderEntity {

    private static final String INDEX_COMMAND_TYPE = "orderform";
    private static final String DEFAULT_INDEX_NAME = "orderform";

    @Autowired
    private OrderDao dao;

    @Autowired
    private IndexCommandService indexCommandService;

    @Autowired
    private TransportClient elasticsearchClient;

    @Autowired
    private CacheClient cacheClient;

    /**
     * @return 索引任务ID，用于后续实时搜索
     */
    @Transactional
    public String createOrder(OrderForm o) {
        dao.createOrderForm(o);

        for (OrderDetail d : o.getDetailList()) {
            dao.createOrderDetail(d);
        }

        return updateIndex(o.getId());
    }

    private String updateIndex(String orderId) {
        IndexCommand cmd = new IndexCommand(INDEX_COMMAND_TYPE, DEFAULT_INDEX_NAME, IndexCommand.OP_ADD);
        cmd.data("id", orderId);
        return indexCommandService.exec(cmd);
    }

    private String removeIndex(String orderId) {
        IndexCommand cmd = new IndexCommand(INDEX_COMMAND_TYPE, DEFAULT_INDEX_NAME, IndexCommand.OP_DELETE);
        cmd.data("id", orderId);
        return indexCommandService.exec(cmd);
    }

    @IndexWorker(INDEX_COMMAND_TYPE)
    public void index(IndexCommand cmd) {
        String id = cmd.data("id");
        if (IndexCommand.OP_DELETE == cmd.getOp()) {
            elasticsearchClient.prepareDelete(DEFAULT_INDEX_NAME, DEFAULT_INDEX_NAME, id).get();
        } else {
            OrderForm o = getOrderById(id);

            Map<String, Object> m = new HashMap<String, Object>();
            m.put("id", id);
            m.put("userId", o.getUserId());
            m.put("number", o.getNumber());
            m.put("amount", o.getAmount());
            m.put("gmtCreated", o.getGmtCreated());

            List<String> itemNoList = new ArrayList<String>();
            List<String> itemNameList = new ArrayList<String>();
            for (OrderDetail detail : o.getDetailList()) {
                itemNoList.add(detail.getItemNo());
                itemNameList.add(detail.getItemName());
            }

            m.put("itemNo", itemNoList);
            m.put("itemName", itemNameList);

            elasticsearchClient.prepareIndex(DEFAULT_INDEX_NAME, DEFAULT_INDEX_NAME, id)
                    .setSource(m).get();
        }
    }

    private String getCacheKey(String id) {
        return "order" + id;
    }

    public OrderForm getOrderById(String id) {
        String key = getCacheKey(id);
        OrderForm o = (OrderForm) cacheClient.get(key);
        if (o != null) {
            return o;
        }

        o = dao.getOrderFormById(id);
        if (o == null) {
            return null;
        }

        o.setDetailList(dao.getOrderDetailListByOrderFormId(id));

        cacheClient.set(key, o);
        return o;
    }

    public List<OrderForm> getOrderFormListByUserId(String userId) {
        return dao.getOrderFormListByUserId(userId);
    }

    public OrderListModel search(OrderListModel listModel) {
        if (StringUtils.isNotBlank(listModel.getLastIndexCommandId())) {
            indexCommandService.ensureRefresh(DEFAULT_INDEX_NAME, listModel.getLastIndexCommandId());
        }

        elasticsearchClient.prepareSearch(DEFAULT_INDEX_NAME).setTypes(DEFAULT_INDEX_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        return listModel;
    }


}
