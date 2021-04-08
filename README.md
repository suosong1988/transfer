# transfer
数据搬运工

解决关系型数据库的数据搬运（清洗）问题。

项目中有个例子
老库中有一个表，是产品表
# 
# CREATE TABLE `old_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(16)  NOT NULL,
  `price` decimal(8,2) NOT NULL,
  `category` varchar(16)  NOT NULL COMMENT '品类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  ;
# 里面有几条数据：
INSERT INTO `test`.`old_product`(`id`, `name`, `price`, `category`) VALUES (1, '苹果', 2.00, '水果');
INSERT INTO `test`.`old_product`(`id`, `name`, `price`, `category`) VALUES (2, '香蕉', 5.00, '水果');
INSERT INTO `test`.`old_product`(`id`, `name`, `price`, `category`) VALUES (3, '菠菜', 4.00, '蔬菜');

因为项目重构，新库设计了两张表
//产品表
CREATE TABLE `new_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_code` varchar(16)  NOT NULL,
  `name` varchar(16)  NOT NULL,
  `price` decimal(8,2) NOT NULL,
  `area` varchar(16)  NOT NULL COMMENT '地区',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  ;

//产品品类表
CREATE TABLE `new_product_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_code` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `category` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

数据搬运规则有这么几条
1，把之前的产品表拆分为产品表跟产品品类2个表
2，对于产品名为苹果的数据不搬运
3，对于产品名为菠菜的，改名为库尔勒菠菜
4，对于产品为香蕉的，在搬运后要在控制台打印搬运信息
5，对于新产品表，增加了product_code字段，生成规则是“product”加老产品的id作为业务主键
6，对于新产品表，增加了area字段，默认为‘北京’






