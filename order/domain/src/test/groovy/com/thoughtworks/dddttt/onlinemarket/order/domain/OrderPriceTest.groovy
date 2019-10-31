package com.thoughtworks.dddttt.onlinemarket.order.domain

import spock.lang.Specification

import java.math.RoundingMode

class OrderPriceTest extends Specification {

    def "should have a discount total price"() {
        given:
          OrderPrice orderPrice = new OrderPrice([new SubtotalPrice(new BigDecimal(81.00), null)])

        when:
          BigDecimal finalTotal = orderPrice.finalTotal()

        then:
          finalTotal < orderPrice.originTotal()
          finalTotal == new BigDecimal(90.90).setScale(2, RoundingMode.HALF_UP)
    }
}
