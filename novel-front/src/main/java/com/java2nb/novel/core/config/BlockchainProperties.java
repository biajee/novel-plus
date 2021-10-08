package com.java2nb.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 区块链配置
 * @ConfigurationProperties作用：
 * 将配置文件中配置的每一个属性的值，映射到这个组件中；
 * 告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定
 * 参数 prefix = “blockchain” : 将配置文件中的person下面的所有属性一一对应
 * @Component 注册 bean
 */
@Component
@Data
@ConfigurationProperties(prefix="blockchain")
public class BlockchainProperties {

    // blockchain network settings
    private String name;
    private String network;
    private String description;
    private Integer defaultBlockchainID;// = 100;

    // transaction settings
    private Integer defaultGasPrice;
    private Integer defaultGasLimit;// = 21000;// for moac TX only, Ether is 21000

    // contract settings
    private Integer defaultTokenGasLimit;// = 60000;// for moac TX only, Ether is 21000
    private Integer defaultTokenDecimal;// = 18;

}
