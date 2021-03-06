package com.thoughtworks.dddttt.onlinemarket.order.domain.entity

import com.thoughtworks.dddttt.onlinemarket.order.domain.entity.entity.Account
import com.thoughtworks.dddttt.onlinemarket.order.domain.entity.entity.Order
import com.thoughtworks.dddttt.onlinemarket.order.domain.entity.entity.OrderItem
import com.thoughtworks.dddttt.onlinemarket.order.domain.entity.entity.Product
import com.thoughtworks.dddttt.onlinemarket.order.domain.entity.exception.OrderItemCapacityLimitationException
import spock.lang.Specification

import java.util.stream.IntStream

import static com.thoughtworks.dddttt.onlinemarket.order.domain.entity.entity.OrderItemSizeLimitation.SIZE_LIMITATION

class OrderTest extends Specification {

    def account = new Account("A User");

    def "order can add order items"() {
        given: "we have an order"
          Order order = new Order(account,
           [new OrderItem(new Product("3","Cellphone", BigDecimal.TEN), 1)])

        and: "we have some order items"
          def items = [
                  new OrderItem(new Product("2","Cellphone", BigDecimal.TEN), 1),
                  new OrderItem(new Product("1","Cellphone Pro", BigDecimal.TEN), 1)
          ]

        when: "we add order items to order"
          order.addItems(items)

        then: "the order items have been added to the order"
          order.getItems().containsAll(items)
    }

    def 'we have a limited order items in an order'() {
        given: "we have an order"
          Order order = new Order(account,
          [new OrderItem(new Product("2","Cellphone", BigDecimal.TEN), 1)])

        and: "the items size in this order has already reached the limitation "
          List<OrderItem> items = IntStream.range(0, SIZE_LIMITATION - 1)
                  .mapToObj { i -> new OrderItem(new Product("1","Cellphone", BigDecimal.TEN), 1) }
                  .collect()
          order.addItems(items)

        and: "we have a new order item"
          OrderItem item = new OrderItem(new Product("1","Cellphone", BigDecimal.TEN), 1)
        when: "we try to add the new item to this order"
          order.addItems([item])

        then: "there will be an OrderItemCapacityLimitation error"
          OrderItemCapacityLimitationException exception = thrown()
          exception != null
    }
}
