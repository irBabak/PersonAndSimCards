CREATE SEQUENCE person_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE persons
(
    personId      number PRIMARY KEY,
    first_name    nvarchar2(30),
    last_name     nvarchar2(30),
    national_code nchar(10)
);

CREATE SEQUENCE simcard_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE simcards
(
    simcardId      number PRIMARY KEY,
    simcard_number nvarchar2(11),
    ownerId        number REFERENCES persons
);

CREATE VIEW PERSON_SIMCARD_VIEW AS
SELECT simcards.simcardId      AS simcard_id,
       simcards.simcard_number AS phone_number,
       persons.personId        AS person_id,
       persons.first_name,
       persons.last_name,
       persons.national_code
FROM simcards
         JOIN persons
              ON simcards.ownerId = persons.personId;
