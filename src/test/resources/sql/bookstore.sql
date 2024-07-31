-- clear tables

DELETE FROM author;

-- reset autoincrement
ALTER TABLE author ALTER COLUMN id RESTART WITH 1;

-- non bisogna aggiungere l'id altrimenti l'autoincrement non funziona
INSERT INTO author (name, surname, birthday, city) VALUES
('Arthur', 'Conan Doyle', '1859-05-22', 'Edinburgh'),
('Agatha', 'Christie', '1890-09-15', 'Torquay'),
('J.K.', 'Rowling', '1965-07-31', 'Yate'),
('George', 'Orwell', '1903-06-25', 'Motihari');
