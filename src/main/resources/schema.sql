DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS order_list;
DROP TABLE IF EXISTS ordered_item;

CREATE TABLE user (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  age INT NOT NULL
);

CREATE TABLE item (
  item_id INT AUTO_INCREMENT PRIMARY KEY,
  item_name VARCHAR(250) NOT NULL,
  item_price INT NOT NULL
);

CREATE TABLE order_list (
  order_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE ordered_item (
  ordered_item_id INT AUTO_INCREMENT PRIMARY KEY,
  item_count INT NOT NULL,
  order_id INT,
  item_id INT,
  FOREIGN KEY (order_id) REFERENCES order_list(order_id),
  FOREIGN KEY (item_id) REFERENCES item(item_id)
);