#DELIMITER $$

create procedure novel_blockchain.sp_token_dist(in user_buy_record_id int, in dist_percentage decimal(10, 2))
begin
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
        set account_balance = account_balance - dist_amount
        where id = author_id;

        # add corresponding owning part of dist amount to current owners
        update user a
        join (select user_id, buy_amount/total_buy_amount as award_percetage
        from user_buy_record
        where book_index_id = book_index_id and id != user_buy_record_id) b
        on a.id=b.user_id
        set a.account_balance = a.account_balance + dist_amount * b.award_percetage;
    commit;

end #$$
#DELIMITER ;