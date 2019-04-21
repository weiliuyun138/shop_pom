package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shopcart implements Serializable{

    private int id;
    private int gid;
    private int uid;
    private int gnumber;//商品个数
    private BigDecimal allprice;//商品的小计

    @TableField(exist = false)
    private Goods goods;

}
