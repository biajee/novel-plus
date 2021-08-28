package com.java2nb.novel.core.bean;

import lombok.Data;

/**
 * @author 11797
 * Added the accountAddress with the blockchain
 */
@Data
public class UserDetails {

    private Long id;

    private String username;

    private String nickName;

    private String accountAddress;
}
