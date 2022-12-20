package com.switchfully.eurder.model.orders;

import com.switchfully.eurder.model.items.Item;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "item_groups")
public class ItemGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemgroup_seq")
    @SequenceGenerator(name = "itemgroup_seq", sequenceName = "itemgroup_seq", allocationSize = 1)
    private long id;

    @Column(name = "order_id")
    private long orderId;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "order_amount")
    private int orderAmount;

    @Column(name = "item_price")
    private double itemPriceFrozen;
    @Column(name = "shipping_date")
    private LocalDate shippingDate;



    public ItemGroup() {
    }

    public ItemGroup(Item item, int orderAmount, double itemPriceFrozen, LocalDate shippingDate, long orderId) {
        this.item = item;
        this.orderAmount = orderAmount;
        this.itemPriceFrozen = itemPriceFrozen;
        this.shippingDate = shippingDate;
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public double getItemPriceFrozen() {
        return itemPriceFrozen;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }


}
