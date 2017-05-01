create table order_main(
    id serial primary key,
    orderDate timestamp not null,
    consignee varchar(20) not null,
    phone varchar(20) not null,
    province varchar(50) null,
    city varchar(50) null,
    district varchar(50) null,
    address varchar(50) null,
    status int default 0
);

create table order_detail(
    id serial primary key,
    orderId int not null,
    giftId varchar(10) not null,
    giftName varchar(100) not null,
    num int not null,
    jifen int not null,
    supplyPrice float not null
);


insert into order_main(orderDate, consignee, phone, province, city, district, address, status)
    values('2017/4/28 12:13:14', '郝尤乾', '13501234567', '上海', '上海', '徐汇', '漕宝路22222号', 0);
insert into order_detail(orderId, giftId, giftName, num, jifen, supplyPrice)
    values(1, 'EL1122', 'iphone手机模型', 1, 10000, 122.80);
insert into order_detail(orderId, giftId, giftName, num, jifen, supplyPrice)
    values(1, 'EL3457', 'USB充电插座', 1, 150, 12.80);
