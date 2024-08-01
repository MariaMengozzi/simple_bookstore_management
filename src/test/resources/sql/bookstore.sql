-- clear tables
DELETE FROM book_author;
DELETE FROM book_genre;
DELETE FROM author;
DELETE FROM genre;
DELETE FROM book;

-- reset autoincrement
ALTER TABLE author ALTER COLUMN id RESTART WITH 1;
ALTER TABLE genre ALTER COLUMN id RESTART WITH 1;

-- non bisogna aggiungere l'id altrimenti l'autoincrement non funziona
INSERT INTO author(name, surname, birthday, city) VALUES
('Arthur', 'Conan Doyle', '1859-05-22', 'Edinburgh'), --1
('Agatha', 'Christie', '1890-09-15', 'Torquay'), --2
('J.K.', 'Rowling', '1965-07-31', 'Yate'), --3
('George', 'Orwell', '1903-06-25', 'Motihari'); --4

INSERT INTO genre (name) VALUES
('Thriller'), --1
('Adventure'), --2
('Fantasy'); --3

INSERT INTO book (isbn, title, price, publication_year) VALUES
('8804780169', 'Poirot e le pietre preziose', 10.9, 2023),
('8883377311', 'Le avventure di Sherlock Holmes', 4.66, 2019),
('880472868X', '10 piccoli indiani', 11.0, 2020),
('8883379071', '1984', 4.30, 2021);


INSERT INTO book_author (book_isbn, author_id) VALUES
('8804780169', 2),
('8883377311', 1),
('880472868X', 2),
('8883379071', 4);

INSERT INTO book_genre (book_isbn, genre_id) VALUES
('8804780169', 2),
('8804780169', 1),
('8883377311', 1),
('880472868X', 2),
('880472868X', 1),
('8883379071', 2);
