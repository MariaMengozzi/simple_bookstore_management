USE bookstore;

CREATE TABLE IF NOT EXISTS author (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    city VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS book (
    isbn VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    publication_year YEAR NOT NULL
);

CREATE TABLE IF NOT EXISTS genre (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS book_genre (
    book_isbn VARCHAR(255) NOT NULL,
    genre_id BIGINT NOT NULL,
    FOREIGN KEY (book_isbn) REFERENCES book(isbn),
    FOREIGN KEY (genre_id) REFERENCES genre(id)
);

CREATE TABLE IF NOT EXISTS book_author (
    book_isbn VARCHAR(255) NOT NULL,
    author_id BIGINT NOT NULL,
    FOREIGN KEY (book_isbn) REFERENCES book(isbn),
    FOREIGN KEY (author_id) REFERENCES author(id)
);