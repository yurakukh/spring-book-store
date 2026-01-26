INSERT INTO categories (id, name, description, is_deleted) VALUES (1, 'Novel', 'Classic novels', false);
INSERT INTO books (id, title, author, isbn, price, is_deleted) VALUES (1, 'Process', 'F. Kafka', '9780743273565', 66.66, false);
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);