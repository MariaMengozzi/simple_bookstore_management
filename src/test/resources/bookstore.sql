-- Use the created database
USE bookstoretest;

-- Create the authors table
CREATE TABLE IF NOT EXISTS author (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    birthdate DATE,
    city VARCHAR(255)
);

-- Create the categories table
CREATE TABLE IF NOT EXISTS genre (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create the books table
CREATE TABLE IF NOT EXISTS book (
    isbn VARCHAR(13) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price FLOAT,
    publication_year YEAR NOT NULL
);

-- Create the junction table for the many-to-many relationship between books and authors
CREATE TABLE IF NOT EXISTS book_authors (
    book_isbn VARCHAR(255) NOT NULL,
    author_id BIGINT NOT NULL,
    PRIMARY KEY (book_isbn, author_id),
    FOREIGN KEY (book_isbn) REFERENCES book(isbn) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE CASCADE
);

-- Create the junction table for the many-to-many relationship between books and categories
CREATE TABLE IF NOT EXISTS book_genres (
    book_isbn VARCHAR(255) NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (book_isbn, genre_id),
    FOREIGN KEY (book_isbn) REFERENCES book(isbn) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
);
