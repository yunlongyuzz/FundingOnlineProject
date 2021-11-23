package com.yyl.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReturnVO {

    // 回报信息主键
    private Integer returnId;
    // 当前档位需支持的金额
    private Integer supportMoney;

    // 单笔限购，取值为 0 时无限购，取值为 1 时有限购
    private Integer signalPurchase;
    // 具体限购数量
    private Integer purchase;

    //限额多少位，取值为0时无限额
    private Integer returnCount;


    // 当前该档位支持者数量
    private Integer supproterCount;
    // 运费，取值为 0 时表示包邮
    private Integer freight;
    // 众筹成功后多少天发货
    private Integer returnDate;
    // 回报内容
    private String content;


}
