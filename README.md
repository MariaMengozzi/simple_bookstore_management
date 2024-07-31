# simple_bookstore_management

### Struttura del Progetto

Entità principali:
- Author: rappresenta un autore di uno o più libri.
- Book: rappresenta un libro, che può avere uno o più autori e appartenere a una o più categorie.
- Category: rappresenta una categoria di libri.

Modello del Database: Entità e Relazioni
- Un autore (Author) può avere più libri (Book).
- Un libro (Book) può appartenere a più categorie (Category).
- Un libro (Book) può avere più autori (Author).

Di ogni **autore** si vuole tenere traccia di un id, nome, cognome, data di nascita, città di nascita

Di ogni **libro** si vuole tenere traccia del titolo, ISBN (identificativo), autore/i, categoria/e, anno di uscita e prezzo

Di ogni **categoria** si tiene traccia del nome e un id

Note:
è necessario aver creato il db nel servizio di database, può anche essere vuoto, serve per la connessione