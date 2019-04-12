package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Goods implements Serializable {

    @TableId(type = IdType.AUTO )
    private int id;
    private String gname;
//    private double gprice;

    private BigDecimal gprice;
    private int gsave;
    private String ginfo;
    private String gimage;
    private int status;
    @TableField("createtime")
    private Date createTime = new Date();
    private int tid;



//    public static void main(String[] args) {
//        //十进制没办法精准表示1/3
//        //二进制 - 1/10 - 二进制不能精准表示0.1
//
//        System.out.println(5.0-4.9);
//        BigDecimal a = BigDecimal.valueOf(5.0);
//        BigDecimal b = BigDecimal.valueOf(4.9);
//        BigDecimal c = a.subtract(b);//a-b
//        System.out.println(c.doubleValue());
//    }

}
