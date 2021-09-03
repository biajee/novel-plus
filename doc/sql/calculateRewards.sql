#DELIMITER $$
-- ----------------------------
-- 本脚本用于计算作品分成
-- 当用户付费购买章节时，脚本根据
-- user_buy_record_id 用户购买的记录
-- dist_percentage 作品合约中设定可以分配的部分
-- ----------------------------

create procedure novel_blockchain.sp_token_dist(in user_buy_record_id int, in dist_percentage decimal(10, 2))
begin
DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;

        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Erro in distributing tokens.', MYSQL_ERRNO = 2000;
        RESIGNAL;
    END;

    declare user_id int;
    declare author_id int;
    declare dist_amount decimal(10, 2);
    declare book_index_id int;
    declare total_buy_amount decimal(12,2);

    select a.user_id, b.author_id, a.buy_amount * dist_percentage, a.book_index_id
    into user_id, author_id, dist_amount, book_index_id
    from user_buy_record a
    join book b on a.book_id = b.id
    where a.id = user_buy_record_id;

    select sum(buy_amount)
    into total_buy_amount
    from user_buy_record
    where book_index_id = book_index_id and id != user_buy_record_id;

    start transaction;
        # reduce dist_percentage of buy_amount from author's balance
        update user
        /*
        需要确保有足够的余额分配
        */
        set account_balance = account_balance - dist_amount
        where id = author_id;

        # add corresponding owning part of dist amount to current owners
        update user a
        join (select user_id, buy_amount/total_buy_amount as award_percetage
        from user_buy_record
        where book_index_id = book_index_id and id != user_buy_record_id) b
        on a.id=b.user_id
        set a.account_balance = a.account_balance + dist_amount * b.award_percetage;
        /* 存入用户的通证收入，以备查询
        user_token_list
        */

    commit;

end #$$
#DELIMITER ;